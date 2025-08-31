import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { FaShoppingCart, FaUserCircle, FaBell } from 'react-icons/fa';
import { useNotifications } from '../contexts/NotificationContext';

import Logo from "../assets/Logo.png";
import ServiceSearch from '../pages/ServiceSearch';
import Modal from '../pages/Modal';
import servicesData from '../data/Services';
import { addToCart } from '../redux/createSlice';
import { useAuth } from '../contexts/AuthContext';

function Header() {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);
  const [selectedService, setSelectedService] = useState(null);
  const profileRef = useRef();
  const notificationRef = useRef();
  const [isNotificationOpen, setIsNotificationOpen] = useState(false);
  const cartCount = useSelector((state) => state.cart.items.length);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const { isLoggedIn, email, role, logout, loading } = useAuth();
  const { notifications, unreadCount, markAsRead, markAllAsRead, clearNotifications } = useNotifications();

  useEffect(() => {
    const handleScroll = () => setIsScrolled(window.scrollY > 50);
    const handleClickOutside = (e) => {
      if (profileRef.current && !profileRef.current.contains(e.target)) {
        setIsProfileOpen(false);
      }
      if (notificationRef.current && !notificationRef.current.contains(e.target)) {
        setIsNotificationOpen(false);
      }
    };
    window.addEventListener('scroll', handleScroll);
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      window.removeEventListener('scroll', handleScroll);
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleServiceSelect = (serviceName) => {
    const serviceData = servicesData[serviceName];
    if (serviceData) setSelectedService(serviceData);
    else console.error(`Service "${serviceName}" not found`);
  };

  const handleCloseModal = () => setSelectedService(null);

  const handleBook = (subService) => {
    const token = localStorage.getItem('token');
    if (!token || !isLoggedIn) {
      const shouldLogin = window.confirm('⚠️ Please login to book a service. Go to login page?');
      if (shouldLogin) navigate('/login');
      return;
    }

    dispatch(addToCart({
      serviceName: selectedService.name,
      name: subService.name,
      price: subService.price,
    }));
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isAuthPage = ['/login', '/register'].includes(location.pathname);

  // ⛔️ Don't render header on login/register or while loading auth state
  if (loading || isAuthPage) return null;

  return (
    <>
      <header className={`bg-dark fixed-top py-2 ${isScrolled ? 'shadow-sm' : ''}`}>
        <div className="container d-flex align-items-center justify-content-between">
          <Link to="/" className="me-3">
            <img src={Logo} alt="FixItNow Logo" style={{ height: "50px" }} />
          </Link>

          <nav className="d-none d-md-flex gap-4 mx-auto">
            <Link className="text-white text-decoration-none" to="/">Home</Link>
            <Link className="text-white text-decoration-none" to="/services">Services</Link>
            <Link className="text-white text-decoration-none" to="/about">About Us</Link>

            {isLoggedIn && role === 'ADMIN' && (
              <Link className="text-white text-decoration-none" to="/admin/dashboard">Admin Panel</Link>
            )}
            {isLoggedIn && role === 'CONTRACTOR' && (
              <Link className="text-white text-decoration-none" to="/provider/dashboard">Contractor Dashboard</Link>
            )}
            {/* Removed My Dashboard link for customers as requested */}
          </nav>

          <div className="d-flex align-items-center gap-3" style={{ marginLeft: "auto" }}>
            {(role === 'CUSTOMER' || role === 'CONTRACTOR') && (
              <div className="d-none d-md-block">
                <ServiceSearch onServiceSelect={handleServiceSelect} />
              </div>
            )}

            {role === 'CUSTOMER' && (
              <Link to="/cart" className="text-white position-relative">
                <FaShoppingCart size={22} />
                {cartCount > 0 && (
                  <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                    {cartCount}
                  </span>
                )}
              </Link>
            )}

          {/* Notification Bell - only for customers */}
          {isLoggedIn && role === 'CUSTOMER' && (
            <div className="position-relative" ref={notificationRef}>
              <FaBell
                size={22}
                onClick={() => setIsNotificationOpen(!isNotificationOpen)}
                className="text-white"
                style={{ cursor: 'pointer' }}
              />
              {unreadCount > 0 && (
                <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-warning text-dark">
                  {unreadCount}
                </span>
              )}
              {isNotificationOpen && (
                <div
                  className="position-absolute end-0 mt-2 p-2 bg-white shadow rounded text-dark"
                  style={{ minWidth: '300px', maxHeight: '400px', overflowY: 'auto', zIndex: 1000 }}
                >
                  <div className="d-flex justify-content-between align-items-center mb-2">
                    <h6 className="mb-0">Notifications</h6>
                    <div>
                      <button
                        onClick={markAllAsRead}
                        className="btn btn-sm btn-outline-primary me-1"
                        disabled={unreadCount === 0}
                      >
                        Mark All Read
                      </button>
                      <button
                        onClick={clearNotifications}
                        className="btn btn-sm btn-outline-danger"
                        disabled={notifications.length === 0}
                      >
                        Clear All
                      </button>
                    </div>
                  </div>
                  <hr className="my-2" />
                  {notifications.length === 0 ? (
                    <p className="text-muted text-center mb-0">No notifications</p>
                  ) : (
                    notifications.map((n) => (
                      <div
                        key={n.id}
                        className={`p-2 mb-2 rounded ${!n.read ? 'bg-light' : ''}`}
                        onClick={() => markAsRead(n.id)}
                        style={{ cursor: 'pointer' }}
                      >
                        <div className="d-flex justify-content-between align-items-start">
                          <div className="flex-grow-1">
                            <p className="mb-1 small">{n.message}</p>
                            <small className="text-muted">{new Date(n.timestamp).toLocaleTimeString()}</small>
                          </div>
                          {!n.read && <span className="badge bg-primary ms-2">New</span>}
                        </div>
                      </div>
                    ))
                  )}
                </div>
              )}
            </div>
          )}

            <div className="position-relative" ref={profileRef}>
              <FaUserCircle
                size={24}
                onClick={() => setIsProfileOpen(!isProfileOpen)}
                className="text-white"
                style={{ cursor: "pointer" }}
              />
              {isProfileOpen && (
                <div
                  className="position-absolute end-0 mt-2 p-2 bg-white shadow rounded text-dark"
                  style={{ minWidth: "180px", zIndex: 1000 }}
                >
                  {isLoggedIn ? (
                    <>
                      <div className="dropdown-item text-dark fw-bold px-2 py-1">
                        👋 {email?.split('@')[0] || 'User'}
                      </div>
                      <hr className="my-1" />
                      {role === 'CUSTOMER' && (
                        <Link to="/user/dashboard" className="dropdown-item text-dark py-1 px-2">
                          📋 My Bookings
                        </Link>
                      )}
                      <button
                        onClick={handleLogout}
                        className="dropdown-item text-danger py-1 px-2"
                      >
                        Logout
                      </button>
                    </>
                  ) : (
                    <>
                      <Link to="/login" className="dropdown-item text-dark py-1 px-2">Login</Link>
                      <Link to="/register" className="dropdown-item text-dark py-1 px-2">Register</Link>
                    </>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      </header>

      <Modal
        service={selectedService}
        onClose={handleCloseModal}
        onBook={handleBook}
      />
    </>
  );
}

export default Header;
