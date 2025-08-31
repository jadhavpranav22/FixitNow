# Authentication and Booking API Debug Guide

## Issues Identified and Fixed

### 1. JWT Filter Logger Issue ✅ FIXED
- **Problem**: Missing logger import in JwtAuthFilter
- **Fix**: Added proper logger import and initialization
- **Status**: ✅ RESOLVED

### 2. Security Configuration ✅ IMPROVED
- **Problem**: Missing role-based endpoints in security config
- **Fix**: Added `/contractor/**` and `/customer/**` endpoints
- **Status**: ✅ IMPROVED

### 3. JWT Filter Error Handling ✅ IMPROVED
- **Problem**: Poor error handling and debugging in JWT filter
- **Fix**: Added comprehensive logging and error handling
- **Status**: ✅ IMPROVED

### 4. Frontend Debugging ✅ IMPROVED
- **Problem**: Limited debugging information for API calls
- **Fix**: Added detailed logging in axios interceptors
- **Status**: ✅ IMPROVED

## Testing Steps

### Step 1: Test Registration
```bash
# Test registration endpoint
curl -X POST http://localhost:6969/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "role": "CUSTOMER"
  }'
```

### Step 2: Test Login
```bash
# Test login endpoint
curl -X POST http://localhost:6969/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Step 3: Test Authentication
```bash
# Test auth endpoint (should work without token)
curl -X GET http://localhost:6969/auth/test

# Test booking endpoint (should require token)
curl -X GET http://localhost:6969/bookings/test
```

### Step 4: Test Booking with Token
```bash
# Get token from login response, then test booking
curl -X GET http://localhost:6969/bookings/test \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Frontend Debugging

### 1. Check Browser Console
- Open browser developer tools
- Check Console tab for authentication logs
- Look for token-related messages

### 2. Check Network Tab
- Monitor API requests in Network tab
- Verify Authorization headers are being sent
- Check response status codes

### 3. Check localStorage
```javascript
// In browser console
console.log('Token:', localStorage.getItem('token'));
console.log('Role:', localStorage.getItem('role'));
console.log('Email:', localStorage.getItem('email'));
console.log('UserId:', localStorage.getItem('userId'));
```

## Common Issues and Solutions

### Issue 1: 403 Forbidden Error
**Causes:**
- Missing or invalid JWT token
- Token expired
- User doesn't have required permissions
- CORS issues

**Solutions:**
1. Check if token exists in localStorage
2. Verify token is not expired
3. Ensure user has correct role
4. Check CORS configuration

### Issue 2: Registration Fails
**Causes:**
- Database connection issues
- Validation errors
- Email already exists

**Solutions:**
1. Check database connection
2. Verify email uniqueness
3. Check server logs for errors

### Issue 3: Login Fails
**Causes:**
- Invalid credentials
- User not found
- Password encoding issues

**Solutions:**
1. Verify user exists in database
2. Check password encoding
3. Verify email format

## Server Logs to Monitor

### Application Logs
```bash
# Check for authentication-related logs
tail -f logs/application.log | grep -i "auth\|jwt\|token"
```

### Security Logs
```bash
# Check Spring Security logs
tail -f logs/application.log | grep -i "security"
```

## Database Verification

### Check Users Table
```sql
SELECT * FROM users WHERE email = 'test@example.com';
```

### Check Customer/Contractor Tables
```sql
-- For customers
SELECT * FROM customer WHERE user_id = (SELECT user_id FROM users WHERE email = 'test@example.com');

-- For contractors
SELECT * FROM contractor WHERE user_id = (SELECT user_id FROM users WHERE email = 'test@example.com');
```

## Quick Fixes

### 1. Clear Browser Data
```javascript
// Clear all authentication data
localStorage.clear();
sessionStorage.clear();
```

### 2. Restart Application
```bash
# Stop and restart the Spring Boot application
./mvnw spring-boot:run
```

### 3. Check Database
```bash
# Verify database is running and accessible
mysql -u root -p household
```

## Expected Behavior

### Successful Registration
- Returns 200 OK with user details and token
- Creates user record in database
- Creates corresponding customer/contractor record

### Successful Login
- Returns 200 OK with token and user details
- Token should be valid for 10 hours

### Successful Booking
- Returns 201 Created for new bookings
- Requires valid JWT token
- User must have CUSTOMER role

## Next Steps

1. Test registration with the provided curl commands
2. Test login and verify token generation
3. Test booking creation with valid token
4. Monitor server logs for any errors
5. Check browser console for frontend issues

If issues persist, check the specific error messages in:
- Server logs
- Browser console
- Network tab responses 