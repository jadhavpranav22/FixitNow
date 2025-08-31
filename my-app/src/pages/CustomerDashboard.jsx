import React, { useState, useEffect, useMemo } from 'react';
import { 
    Container, 
    Typography, 
    Card, 
    CardContent, 
    Grid, 
    Chip, 
    Box,
    Alert,
    CircularProgress,
    Button,
    Snackbar,
    IconButton   // ⬅️ Add this
} from '@mui/material';

import { 
    CheckCircle as AcceptedIcon,
    Cancel as RejectedIcon,
    Schedule as PendingIcon,
    Done as CompletedIcon,
    Assignment as AssignedIcon,
    Refresh as RefreshIcon   // ⬅️ Add this
} from '@mui/icons-material';

import { useAuth } from '../contexts/AuthContext';
import { useNotifications } from '../contexts/NotificationContext';
import api from '../api/axiosConfig';

const CustomerDashboard = () => {
    const { userId } = useAuth();
    const { addNotification } = useNotifications();
    const STATUS_BASELINE_KEY = useMemo(() => `bookingStatusBaseline_${userId || 'anon'}`, [userId]);
    const [bookings, setBookings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (userId) {
            fetchCustomerBookings();
            // Poll every 5 seconds for near real-time updates
            const interval = setInterval(fetchCustomerBookings, 5000);
            return () => clearInterval(interval);
        }
    }, [userId]);

    const fetchCustomerBookings = async () => {
        try {
            setLoading(true);
            const response = await api.get(`/bookings/customer/${userId}`);
            const newBookings = response.data || [];
            
            // Check for status changes and show notifications (in-session compare)
            if (bookings.length > 0) {
                newBookings.forEach(newBooking => {
                    const oldBooking = bookings.find(b => b.id === newBooking.id);
                    if (oldBooking && oldBooking.status !== newBooking.status) {
                        showStatusNotification(newBooking.status, newBooking.service?.serviceName);
                    }
                });
            }

            // Also compare against a persisted baseline so user gets notifications even after navigation/reload
            try {
                const baselineJson = localStorage.getItem(STATUS_BASELINE_KEY);
                const baseline = baselineJson ? JSON.parse(baselineJson) : {};
                const nextBaseline = {};

                newBookings.forEach(nb => {
                    nextBaseline[nb.id] = nb.status;
                    if (baseline && baseline[nb.id] && baseline[nb.id] !== nb.status) {
                        showStatusNotification(nb.status, nb.service?.serviceName);
                    }
                });
                localStorage.setItem(STATUS_BASELINE_KEY, JSON.stringify(nextBaseline));
            } catch (e) {
                // ignore baseline persistence errors
            }
            
            setBookings(newBookings);
        } catch (err) {
            console.error('Error fetching customer bookings:', err);
            setError('Failed to load your bookings');
        } finally {
            setLoading(false);
        }
    };

    const showStatusNotification = (status, serviceName) => {
        let message = '';
        let severity = 'info';
        
        switch (status) {
            case 'ACCEPTED_BY_CONTRACTOR':
                message = `🎉 Great news! Your booking for "${serviceName}" has been accepted by a contractor!`;
                severity = 'success';
                break;
            case 'REJECTED_BY_CONTRACTOR':
                message = `⚠️ Your booking for "${serviceName}" was rejected. Other contractors may still accept it.`;
                severity = 'warning';
                break;
            case 'COMPLETED':
                message = `✅ Your booking for "${serviceName}" has been completed!`;
                severity = 'success';
                break;
            default:
                return;
        }
        
        console.log('Adding notification:', { message, severity, status, serviceName });
        addNotification({ message, severity });
    };

    const getStatusColor = (status) => {
        switch (status) {
            case 'PENDING':
                return 'warning';
            case 'ACCEPTED_BY_CONTRACTOR':
                return 'success';
            case 'REJECTED_BY_CONTRACTOR':
                return 'error';
            case 'COMPLETED':
                return 'info';
            case 'ASSIGNED_TO_CONTRACTOR':
                return 'primary';
            default:
                return 'default';
        }
    };

    const getStatusIcon = (status) => {
        switch (status) {
            case 'PENDING':
                return <PendingIcon />;
            case 'ACCEPTED_BY_CONTRACTOR':
                return <AcceptedIcon />;
            case 'REJECTED_BY_CONTRACTOR':
                return <RejectedIcon />;
            case 'COMPLETED':
                return <CompletedIcon />;
            case 'ASSIGNED_TO_CONTRACTOR':
                return <AssignedIcon />;
            default:
                return <PendingIcon />;
        }
    };

    const getStatusText = (status) => {
        switch (status) {
            case 'PENDING':
                return 'Pending - Waiting for Contractor';
            case 'ACCEPTED_BY_CONTRACTOR':
                return 'Accepted by Contractor';
            case 'REJECTED_BY_CONTRACTOR':
                return 'Rejected by Contractor';
            case 'COMPLETED':
                return 'Completed';
            case 'ASSIGNED_TO_CONTRACTOR':
                return 'Assigned to Contractor';
            default:
                return status;
        }
    };

    const getStatusDescription = (status) => {
        switch (status) {
            case 'PENDING':
                return 'Your booking request is visible to all contractors. They can accept or reject it.';
            case 'ACCEPTED_BY_CONTRACTOR':
                return 'A contractor has accepted your booking! They will contact you soon to schedule the work.';
            case 'REJECTED_BY_CONTRACTOR':
                return 'This booking was rejected by a contractor. Other contractors may still accept it.';
            case 'COMPLETED':
                return 'This service has been completed successfully!';
            case 'ASSIGNED_TO_CONTRACTOR':
                return 'Your booking has been assigned to a contractor by an admin.';
            default:
                return '';
        }
    };

    if (loading) {
        return (
            <Container maxWidth="lg" sx={{ mt: 4, textAlign: 'center' }}>
                <CircularProgress />
            </Container>
        );
    }

    if (error) {
        return (
            <Container maxWidth="lg" sx={{ mt: 4 }}>
                <Alert severity="error">{error}</Alert>
            </Container>
        );
    }

    return (
        <Container maxWidth="lg" sx={{ mt: 4 }}>
            <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
                <Typography variant="h4" component="h1">
                    🏠 My Bookings Dashboard
                </Typography>
                <div>
                    <IconButton 
                        onClick={fetchCustomerBookings}
                        disabled={loading}
                        color="primary"
                        title="Refresh bookings"
                    >
                        <RefreshIcon />
                    </IconButton>
                    <IconButton 
                        onClick={() => addNotification({
                            message: "🧪 Test notification from customer dashboard!",
                            severity: 'info'
                        })}
                        color="secondary"
                        title="Test notification"
                    >
                        🧪
                    </IconButton>
                </div>
            </Box>
            
            {bookings.length === 0 ? (
                <Alert severity="info">
                    You haven't made any bookings yet. 
                    <br />
                    Go to our services page to book a service!
                </Alert>
            ) : (
                <Grid container spacing={3}>
                    {bookings.map((booking) => (
                        <Grid item xs={12} md={6} key={booking.id}>
                            <Card sx={{ 
                                border: booking.status === 'ACCEPTED_BY_CONTRACTOR' ? '2px solid #4caf50' : 
                                       booking.status === 'REJECTED_BY_CONTRACTOR' ? '2px solid #f44336' : 
                                       '1px solid #e0e0e0',
                                transition: 'all 0.3s ease'
                            }}>
                                <CardContent>
                                    <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={2}>
                                        <Typography variant="h6" component="h2">
                                            {booking.service?.serviceName || 'Service'}
                                            {booking.subservice && (
                                                <Typography variant="body2" color="text.secondary">
                                                    - {booking.subservice.subServiceName}
                                                </Typography>
                                            )}
                                        </Typography>
                                        <Chip 
                                            icon={getStatusIcon(booking.status)}
                                            label={getStatusText(booking.status)} 
                                            color={getStatusColor(booking.status)}
                                            size="small"
                                        />
                                    </Box>
                                    
                                    <Typography variant="body2" color="text.secondary" sx={{ mb: 2, fontStyle: 'italic' }}>
                                        {getStatusDescription(booking.status)}
                                    </Typography>
                                    
                                    <Typography variant="body2" color="text.secondary" gutterBottom>
                                        <strong>📍 Address:</strong> {booking.address}
                                    </Typography>
                                    
                                    <Typography variant="body2" color="text.secondary" gutterBottom>
                                        <strong>📅 Scheduled Date:</strong> {new Date(booking.scheduledDate).toLocaleDateString()}
                                    </Typography>
                                    
                                    {booking.notes && (
                                        <Typography variant="body2" color="text.secondary" gutterBottom>
                                            <strong>📝 Notes:</strong> {booking.notes}
                                        </Typography>
                                    )}
                                    
                                    {booking.contractor && (
                                        <Box mt={2} p={2} bgcolor="grey.100" borderRadius={1}>
                                            <Typography variant="subtitle2" gutterBottom>
                                                🛠️ Assigned Contractor:
                                            </Typography>
                                            <Typography variant="body2">
                                                <strong>Name:</strong> {booking.contractor.name}
                                            </Typography>
                                            <Typography variant="body2">
                                                <strong>Phone:</strong> {booking.contractor.phone}
                                            </Typography>
                                            <Typography variant="body2">
                                                <strong>Email:</strong> {booking.contractor.email}
                                            </Typography>
                                        </Box>
                                    )}
                                    
                                    {booking.status === 'ACCEPTED_BY_CONTRACTOR' && (
                                        <Alert severity="success" sx={{ mt: 2 }}>
                                            ✅ Your booking has been accepted by the contractor!
                                            <br />
                                            They will contact you soon to schedule the work.
                                        </Alert>
                                    )}
                                    
                                    {booking.status === 'REJECTED_BY_CONTRACTOR' && (
                                        <Alert severity="warning" sx={{ mt: 2 }}>
                                            ⚠️ This booking was rejected by the contractor.
                                            <br />
                                            Other contractors may still accept it.
                                        </Alert>
                                    )}

                                    {booking.status === 'COMPLETED' && (
                                        <Alert severity="info" sx={{ mt: 2 }}>
                                            🎉 This service has been completed successfully!
                                            <br />
                                            Thank you for choosing our services.
                                        </Alert>
                                    )}
                                </CardContent>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
            )}


        </Container>
    );
};

export default CustomerDashboard;

