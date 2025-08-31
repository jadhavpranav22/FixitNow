# Booking Service Testing Guide

## Overview
This guide provides step-by-step instructions for testing the complete booking functionality in the Household Repair Project.

## Prerequisites

1. **Backend Running**: Ensure Spring Boot application is running on port 6969
2. **Frontend Running**: Ensure React application is running on port 3000
3. **Database**: MySQL should be running with the correct database
4. **Test User**: Create a customer user in the database

## Test User Setup

### Option 1: Use Existing User
If you have an existing customer user, use those credentials.

### Option 2: Create Test User via API
```bash
curl -X POST http://localhost:6969/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testcustomer@example.com",
    "password": "password123",
    "username": "TestCustomer",
    "role": "CUSTOMER"
  }'
```

## Testing Steps

### 1. Frontend Testing (Manual)

#### Step 1: Login
1. Open browser and navigate to `http://localhost:3000`
2. Click "Login" and enter customer credentials
3. Verify successful login and redirect to customer dashboard

#### Step 2: Browse Services
1. Navigate to "Services" page
2. Search for a service (e.g., "Plumbing")
3. Click on the service to open modal
4. Verify sub-services are displayed with prices

#### Step 3: Add to Cart
1. Click "Add To Cart" for a sub-service
2. Verify success message appears
3. Navigate to Cart page
4. Verify item appears in cart with correct details

#### Step 4: Create Booking
1. Fill in booking form:
   - Address: "123 Test Street, Test City"
   - Scheduled Date: Select a future date
   - Notes: "Test booking"
2. Click "Submit Booking"
3. Verify success message
4. Verify cart is cleared
5. Verify form is reset

### 2. Backend Testing (Automated)

#### Run Authentication Test
```bash
node test_auth.js
```

#### Run Booking Flow Test
```bash
node test_booking_flow.js
```

### 3. API Testing (Manual)

#### Test Login
```bash
curl -X POST http://localhost:6969/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testcustomer@example.com",
    "password": "password123"
  }'
```

#### Test Booking Creation
```bash
# First, get token from login response
TOKEN="your_jwt_token_here"
USER_ID="your_user_id_here"

curl -X POST "http://localhost:6969/bookings?customerId=$USER_ID&serviceId=1&subServiceId=1" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "address": "123 Test Street, Test City",
    "scheduledDate": "2024-12-25",
    "notes": "Test booking"
  }'
```

#### Test Fetch Customer Bookings
```bash
curl -X GET "http://localhost:6969/bookings/customer/$USER_ID" \
  -H "Authorization: Bearer $TOKEN"
```

## Expected Results

### Frontend
- ✅ Login successful with customer role
- ✅ Services page displays correctly
- ✅ Modal opens with sub-services
- ✅ Add to cart works with success message
- ✅ Cart page shows items correctly
- ✅ Booking form submits successfully
- ✅ Success message appears
- ✅ Cart clears after successful booking

### Backend
- ✅ Authentication returns valid JWT token
- ✅ Booking creation returns 201 status
- ✅ Booking data is saved to database
- ✅ Customer bookings endpoint returns user's bookings
- ✅ All bookings endpoint returns all bookings

## Troubleshooting

### Common Issues

#### 1. "Missing authentication info" Error
**Cause**: JWT token not being sent or invalid
**Solution**: 
- Check browser network tab for Authorization header
- Verify token is stored in localStorage
- Check token expiration

#### 2. "Customer not found" Error
**Cause**: User ID doesn't exist in database
**Solution**:
- Verify user exists in database
- Check if user has CUSTOMER role
- Ensure correct user ID is being sent

#### 3. "Service not found" Error
**Cause**: Service ID doesn't exist in database
**Solution**:
- Verify service exists in database
- Check service ID mapping in frontend
- Ensure correct service ID is being sent

#### 4. Date Format Issues
**Cause**: Invalid date format
**Solution**:
- Use YYYY-MM-DD format
- Ensure date is in the future
- Check date parsing in backend

#### 5. CORS Issues
**Cause**: Cross-origin request blocked
**Solution**:
- Verify CORS configuration in backend
- Check if frontend and backend ports match
- Ensure proper headers are set

### Debug Steps

1. **Check Browser Console**:
   - Look for JavaScript errors
   - Check network requests
   - Verify localStorage contents

2. **Check Backend Logs**:
   - Look for authentication errors
   - Check database connection
   - Verify request processing

3. **Check Database**:
   - Verify user exists
   - Check service data
   - Confirm booking records

4. **Use Debug Component**:
   - The AuthDebug component shows current auth state
   - Test authentication with "Test Auth" button
   - Validate token with "Validate Token" button

## Database Verification

### Check User Table
```sql
SELECT * FROM users WHERE email = 'testcustomer@example.com';
```

### Check Services Table
```sql
SELECT * FROM services;
```

### Check Bookings Table
```sql
SELECT * FROM booking ORDER BY id DESC LIMIT 5;
```

## Performance Testing

### Load Testing
```bash
# Test multiple concurrent bookings
for i in {1..10}; do
  node test_booking_flow.js &
done
wait
```

### Stress Testing
```bash
# Test with invalid data
curl -X POST "http://localhost:6969/bookings?customerId=999&serviceId=999" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"address": "", "scheduledDate": "invalid-date"}'
```

## Security Testing

### Test Unauthorized Access
```bash
# Try to access without token
curl -X GET "http://localhost:6969/bookings"
```

### Test Invalid Token
```bash
# Try to access with invalid token
curl -X GET "http://localhost:6969/bookings" \
  -H "Authorization: Bearer invalid.token.here"
```

### Test Wrong Role Access
```bash
# Try to access admin endpoints with customer token
curl -X GET "http://localhost:6969/admin/bookings" \
  -H "Authorization: Bearer $CUSTOMER_TOKEN"
```

## Success Criteria

✅ **Authentication**: JWT tokens work correctly
✅ **Authorization**: Role-based access control works
✅ **Booking Creation**: Customers can create bookings
✅ **Data Persistence**: Bookings are saved to database
✅ **Error Handling**: Proper error messages displayed
✅ **User Experience**: Smooth flow from login to booking
✅ **Security**: Unauthorized access is blocked
✅ **Performance**: Response times are acceptable

## Next Steps

After successful testing:

1. **Production Deployment**: Configure for production environment
2. **Monitoring**: Set up logging and monitoring
3. **Backup**: Implement database backup strategy
4. **Scaling**: Plan for horizontal scaling if needed
5. **Documentation**: Update user documentation 