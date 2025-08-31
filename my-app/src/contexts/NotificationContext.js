import React, { createContext, useContext, useState, useCallback, useEffect, useRef } from 'react';
import api from '../api/axiosConfig';
import { useAuth } from './AuthContext';

const NotificationContext = createContext();

export const NotificationProvider = ({ children }) => {
  const { userId, role, isLoggedIn } = useAuth();
  const [notifications, setNotifications] = useState([]);
  const bookingsStatusRef = useRef(new Map());
  const hasInitializedRef = useRef(false);

  const addNotification = useCallback((notification) => {
    const id = Date.now();
    const newNotification = {
      id,
      message: notification.message,
      severity: notification.severity || 'info',
      timestamp: new Date(),
      read: false
    };

    // Keep only the latest 5 notifications and do NOT auto-remove
    setNotifications(prev => {
      const next = [newNotification, ...prev];
      return next.slice(0, 5);
    });
  }, []);

  const markAsRead = useCallback((id) => {
    setNotifications(prev => prev.map(n => n.id === id ? { ...n, read: true } : n));
  }, []);

  const markAllAsRead = useCallback(() => {
    setNotifications(prev => prev.map(n => ({ ...n, read: true })));
  }, []);

  const clearNotifications = useCallback(() => {
    setNotifications([]);
  }, []);

  const unreadCount = notifications.filter(n => !n.read).length;

  // Global poller for customer booking status changes → push header notifications
  useEffect(() => {
    let intervalId;
    const shouldPoll = isLoggedIn && role === 'CUSTOMER' && userId;

    console.log('NotificationContext: shouldPoll =', shouldPoll, 'userId =', userId, 'role =', role, 'isLoggedIn =', isLoggedIn);

    const pollBookings = async () => {
      try {
        console.log('NotificationContext: Polling bookings for user', userId);
        const response = await api.get(`/bookings/customer/${userId}`);
        const latest = Array.isArray(response.data) ? response.data : [];
        console.log('NotificationContext: Latest bookings:', latest);

        if (!hasInitializedRef.current) {
          console.log('NotificationContext: Initializing baseline');
          const baseline = new Map();
          latest.forEach(b => baseline.set(b.id, b.status));
          bookingsStatusRef.current = baseline;
          hasInitializedRef.current = true;
          return;
        }

        const prevMap = bookingsStatusRef.current;
        let statusChanged = false;
        
        latest.forEach(b => {
          const prevStatus = prevMap.get(b.id);
          console.log(`NotificationContext: Booking ${b.id} - prev: ${prevStatus}, current: ${b.status}`);
          
          // Check for status changes
          if (prevStatus && prevStatus !== b.status) {
            statusChanged = true;
            console.log(`NotificationContext: Status change detected! ${prevStatus} -> ${b.status}`);
            let message = '';
            let severity = 'info';
            const serviceName = b.serviceName || b.service?.serviceName || 'Service';
            
            if (b.status === 'ACCEPTED_BY_CONTRACTOR') {
              message = `🎉 Great news! Your booking for "${serviceName}" has been accepted by a contractor!`;
              severity = 'success';
            } else if (b.status === 'REJECTED_BY_CONTRACTOR') {
              message = `⚠️ Your booking for "${serviceName}" was rejected. Other contractors may still accept it.`;
              severity = 'warning';
            } else if (b.status === 'COMPLETED') {
              message = `✅ Your booking for "${serviceName}" has been completed!`;
              severity = 'success';
            }
            
            if (message) {
              console.log('NotificationContext: Adding notification:', message);
              addNotification({ message, severity });
            }
          }
          
          // Also check for new bookings (no previous status)
          if (!prevStatus && b.status === 'ACCEPTED_BY_CONTRACTOR') {
            statusChanged = true;
            console.log(`NotificationContext: New accepted booking detected! ${b.id}`);
            const serviceName = b.serviceName || b.service?.serviceName || 'Service';
            const message = `🎉 Great news! Your booking for "${serviceName}" has been accepted by a contractor!`;
            addNotification({ message, severity: 'success' });
          }
        });

        if (statusChanged) {
          console.log('NotificationContext: Status changes detected, updating baseline');
        }

        const nextMap = new Map();
        latest.forEach(b => nextMap.set(b.id, b.status));
        bookingsStatusRef.current = nextMap;
      } catch (e) {
        console.error('NotificationContext: Error polling bookings:', e);
      }
    };

    if (shouldPoll) {
      pollBookings();
      intervalId = setInterval(pollBookings, 5000); // Poll every 5 seconds for faster response
    }

    return () => {
      if (intervalId) clearInterval(intervalId);
      hasInitializedRef.current = false;
      bookingsStatusRef.current = new Map();
    };
  }, [isLoggedIn, role, userId, addNotification]);

  return (
    <NotificationContext.Provider value={{
      notifications,
      addNotification,
      markAsRead,
      markAllAsRead,
      clearNotifications,
      unreadCount
    }}>
      {children}
    </NotificationContext.Provider>
  );
};

export const useNotifications = () => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error('useNotifications must be used within a NotificationProvider');
  }
  return context;
};
