# Complete Booking Flow Implementation Guide

## Overview
This document explains the complete booking flow implementation where customers can book services, contractors can view and accept/reject bookings, and customers receive real-time confirmations.

## 🏗️ System Architecture

### Backend Components
- **Booking Entity**: Stores booking information with status tracking
- **BookingService**: Handles business logic for booking operations
- **BookingController**: REST API endpoints for booking management
- **BookingRepo**: Database operations for bookings

### Frontend Components
- **BookingForm**: Customer interface for creating bookings
- **ContractorDashboard**: Contractor interface for managing bookings
- **CustomerDashboard**: Customer interface for viewing booking status

## 📋 Booking Flow Steps

### 1. Customer Creates Booking
```
Customer → BookingForm → POST /bookings → BookingService.createBooking()
```

**Process:**
- Customer fills out booking form with:
  - Service selection
  - Address
  - Scheduled date
  - Additional notes
- System creates booking with `PENDING` status
- Booking becomes visible to all contractors

**API Endpoint:**
```http
POST /bookings?customerId={id}&serviceId={id}&subServiceId={id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "address": "123 Main St, City, State",
  "scheduledDate": "2024-02-15",
  "notes": "Urgent repair needed"
}
```

### 2. Contractors View Available Bookings
```
Contractor → ContractorDashboard → GET /bookings/contractor/{id} → BookingService.getBookingsByContractor()
```

**Process:**
- Contractor dashboard shows all `PENDING` bookings
- Each booking displays:
  - Service details
  - Customer information
  - Address and scheduled date
  - Notes
  - Accept/Reject buttons

**API Endpoint:**
```http
GET /bookings/contractor/{contractorId}
Authorization: Bearer {token}
```

### 3. Contractor Accepts/Rejects Booking
```
Contractor → Accept/Reject Button → PUT /bookings/{id}/accept|reject → BookingService.contractorAccept()|contractorReject()
```

**Process:**
- Contractor clicks Accept/Reject button
- System updates booking status:
  - Accept: `PENDING` → `ACCEPTED_BY_CONTRACTOR`
  - Reject: `PENDING` → `REJECTED_BY_CONTRACTOR`
- Contractor is assigned to accepted bookings

**API Endpoints:**
```http
PUT /bookings/{bookingId}/accept?contractorId={contractorId}
PUT /bookings/{bookingId}/reject?contractorId={contractorId}
Authorization: Bearer {token}
```

### 4. Customer Receives Confirmation
```
Customer → CustomerDashboard → GET /bookings/customer/{id} → Real-time updates
```

**Process:**
- Customer dashboard polls for updates every 30 seconds
- Status changes trigger notifications
- Accepted bookings show contractor contact information
- Rejected bookings remain available for other contractors

**API Endpoint:**
```http
GET /bookings/customer/{customerId}
Authorization: Bearer {token}
```

### 5. Contractor Completes Work
```
Contractor → Mark Complete → PUT /bookings/{id}/confirm → BookingService.confirmBooking()
```

**Process:**
- Contractor marks accepted booking as completed
- Status changes: `ACCEPTED_BY_CONTRACTOR` → `COMPLETED`
- Customer receives completion notification

**API Endpoint:**
```http
PUT /bookings/{bookingId}/confirm
Authorization: Bearer {token}
```

## 🗄️ Database Schema

### Booking Entity
```java
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String address;
    private String notes;
    private LocalDate scheduledDate;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    @ManyToOne
    @JoinColumn(name = "contractor_id")
    private Contractor contractor;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services service;
    
    @ManyToOne
    @JoinColumn(name = "subservice_id")
    private SubServices subservice;
}
```

### Booking Status Enum
```java
public enum BookingStatus {
    PENDING,                    // Visible to all contractors
    PENDING_ADMIN_APPROVAL,     // For admin review
    ASSIGNED_TO_CONTRACTOR,     // Admin assigned
    ACCEPTED_BY_CONTRACTOR,     // Contractor accepted
    REJECTED_BY_CONTRACTOR,     // Contractor rejected
    COMPLETED                   // Work finished
}
```

## 🎨 User Interface Features

### Contractor Dashboard
- **Tabbed Interface:**
  - Available Bookings (Pending)
  - My Accepted Bookings
  - Completed Bookings
- **Booking Cards:**
  - Service and subservice details
  - Customer contact information
  - Address and scheduled date
  - Notes
  - Status indicators with icons
  - Action buttons (Accept/Reject/Complete)

### Customer Dashboard
- **Real-time Updates:**
  - Automatic polling every 30 seconds
  - Status change notifications
  - Visual status indicators
- **Booking Cards:**
  - Service details
  - Current status with descriptions
  - Contractor information (when assigned)
  - Color-coded borders for status
  - Status-specific alerts

## 🔄 Status Flow Diagram

```
PENDING
    ↓ (Contractor Accepts)
ACCEPTED_BY_CONTRACTOR
    ↓ (Contractor Completes)
COMPLETED

PENDING
    ↓ (Contractor Rejects)
REJECTED_BY_CONTRACTOR
    ↓ (Still visible to other contractors)
PENDING (for other contractors)
```

## 🚀 Key Features

### 1. Real-time Notifications
- Customer receives immediate notifications when booking status changes
- Snackbar notifications with appropriate icons and colors
- Automatic status updates without page refresh

### 2. Comprehensive Booking Information
- Full customer details visible to contractors
- Complete service information
- Address, date, and notes
- Contractor contact information for customers

### 3. Flexible Contractor Assignment
- Multiple contractors can see pending bookings
- First-come-first-served acceptance
- Rejected bookings remain available to others

### 4. Status Tracking
- Clear visual indicators for each status
- Descriptive status messages
- Progress tracking through the booking lifecycle

### 5. Error Handling
- Proper validation of booking states
- User-friendly error messages
- Graceful handling of edge cases

## 🧪 Testing

### Test Script
Run the complete booking flow test:
```bash
node test_booking_flow.js
```

This script tests:
1. Customer and contractor registration
2. Service creation
3. Booking creation
4. Contractor visibility
5. Booking acceptance
6. Customer confirmation
7. Booking completion

### Manual Testing Steps
1. **Create Customer Account**
2. **Create Contractor Account**
3. **Customer Books Service**
4. **Contractor Views Available Bookings**
5. **Contractor Accepts Booking**
6. **Customer Sees Confirmation**
7. **Contractor Marks as Completed**

## 🔧 Configuration

### Backend Configuration
- Database connection in `application.properties`
- JWT token configuration
- CORS settings for frontend communication

### Frontend Configuration
- API base URL in `axiosConfig.js`
- Authentication token management
- Real-time polling intervals

## 📱 Mobile Responsiveness
- All dashboards are mobile-responsive
- Cards stack properly on smaller screens
- Touch-friendly buttons and interactions
- Optimized for both desktop and mobile use

## 🔒 Security Considerations
- JWT token authentication for all API calls
- Role-based access control
- Input validation and sanitization
- Secure password handling

## 🚀 Deployment
1. Build the Spring Boot application
2. Build the React frontend
3. Configure environment variables
4. Deploy to your preferred hosting platform

This implementation provides a complete, production-ready booking system with real-time updates, comprehensive status tracking, and user-friendly interfaces for both customers and contractors.

