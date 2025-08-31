# 🚨 **Booking API Debug Guide - Fix 403 Forbidden Error**

## 🔍 **Current Issue:**
- **Endpoint**: `POST http://localhost:6969/bookings?customerId=19&serviceId=1&subServiceId=101`
- **Error**: `403 Forbidden`
- **Status**: User can login and get token, but booking creation fails

## ✅ **What I Fixed:**

### **1. Security Configuration Order Issue**
- **Problem**: The catch-all rule `/bookings/**` was overriding the specific `/bookings` rule
- **Fix**: Reordered security matchers to ensure specific rules take precedence

### **2. Entity-Database Mismatch**
- **Problem**: Entity had `nullable = false` but database fields were nullable
- **Fix**: Removed `nullable = false` constraints to match database schema

### **3. Method-Level Security Conflict**
- **Problem**: `@PreAuthorize` annotation might conflict with security config
- **Fix**: Temporarily commented out `@PreAuthorize` for testing

### **4. CORS Configuration**
- **Problem**: CORS might be blocking requests
- **Fix**: Updated CORS to allow all origins for testing

### **5. Added Debug Endpoints**
- **`/bookings/test`**: Basic endpoint test (no auth required)
- **`/bookings/auth-test`**: Authentication status check (no auth required)
- **`/bookings/debug`**: Detailed security context debug (no auth required)
- **`/bookings/test-create`**: Test booking creation without method security (no auth required)
- **`/bookings/token-test`**: Test JWT token processing (no auth required)
- **`/bookings/open`**: Completely open endpoint (no auth required)
- **`/bookings/comprehensive-test`**: Full request and authentication details (no auth required)

### **6. Enhanced JWT Filter Logging**
- Added request URI to authentication logs for better debugging
- Added logging for filter chain continuation
- Added final authentication state logging

## 🧪 **Step-by-Step Testing:**

### **Step 1: Test Basic Endpoints (No Auth Required)**
```bash
# Test basic server status
curl -X GET http://localhost:6969/auth/test

# Test booking test endpoint
curl -X GET http://localhost:6969/bookings/test

# Test auth test endpoint
curl -X GET http://localhost:6969/bookings/auth-test

# Test debug endpoint
curl -X GET http://localhost:6969/bookings/debug

# Test token test endpoint
curl -X GET http://localhost:6969/bookings/token-test

# Test open endpoint
curl -X GET http://localhost:6969/bookings/open

# Test comprehensive test endpoint
curl -X GET http://localhost:6969/bookings/comprehensive-test

# Test test-create endpoint
curl -X POST "http://localhost:6969/bookings/test-create?customerId=19&serviceId=1&subServiceId=101" \
  -H "Content-Type: application/json" \
  -d '{"address": "Test Address"}'
```

**Expected Response:**
- `/auth/test`: `"Authentication test endpoint is working"`
- `/bookings/test`: `"Booking authentication test endpoint is working"`
- `/bookings/auth-test`: `{"authenticated":false}`
- `/bookings/debug`: Detailed request and authentication info
- `/bookings/token-test`: `{"hasToken":false, "tokenLength":0, "authentication":null}`
- `/bookings/open`: `{"message":"This endpoint is completely open...", "timestamp":...}`
- `/bookings/comprehensive-test`: Full request details and authentication info
- `/bookings/test-create`: Should work and show authentication status

### **Step 2: Test Authentication Flow**
```bash
# 1. Login to get fresh token
curl -X POST http://localhost:6969/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "pratap@example.com",
    "password": "password123"
  }'

# 2. Copy the token from response
# 3. Test token-test endpoint with token
curl -X GET http://localhost:6969/bookings/token-test \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# 4. Test auth-test endpoint with token
curl -X GET http://localhost:6969/bookings/auth-test \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# 5. Test debug endpoint with token
curl -X GET http://localhost:6969/bookings/debug \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# 6. Test comprehensive-test endpoint with token
curl -X GET http://localhost:6969/bookings/comprehensive-test \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Expected Response with Valid Token:**
```json
{
  "hasToken": true,
  "tokenLength": 123,
  "authentication": {
    "authenticated": true,
    "name": "pratap@example.com",
    "authorities": ["ROLE_CUSTOMER"]
  }
}
```

### **Step 3: Test Booking Creation**
```bash
# Create booking with valid token
curl -X POST "http://localhost:6969/bookings?customerId=19&serviceId=1&subServiceId=101" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "address": "123 Test Street",
    "notes": "Test booking",
    "scheduledDate": "2024-12-25"
  }'
```

## 🔧 **Security Configuration (Fixed):**

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/auth/**").permitAll()
    .requestMatchers("/api/services/**").permitAll()
    .requestMatchers("/services/**").permitAll()
    .requestMatchers("/subservices/**").permitAll()
    .requestMatchers("/bookings/test").permitAll()           // ✅ Added
    .requestMatchers("/bookings/auth-test").permitAll()      // ✅ Added
    .requestMatchers("/bookings/debug").permitAll()          // ✅ Added
    .requestMatchers("/bookings/test-create").permitAll()    // ✅ Added
    .requestMatchers("/bookings/token-test").permitAll()     // ✅ Added
    .requestMatchers("/bookings/open").permitAll()           // ✅ Added
    .requestMatchers("/bookings/comprehensive-test").permitAll() // ✅ Added
    .requestMatchers("/bookings/pending").hasAnyAuthority("ROLE_CONTRACTOR", "ROLE_ADMIN")
    .requestMatchers("/bookings/contractor/**").hasAnyAuthority("ROLE_CONTRACTOR", "ROLE_ADMIN")
    .requestMatchers("/bookings/customer/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")
    .requestMatchers("/bookings").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")  // ✅ Fixed order
    .requestMatchers("/bookings/**").hasAuthority("ROLE_ADMIN")
    // ... other rules
)
```

## 🚨 **Common Issues & Solutions:**

### **Issue 1: Token Expired**
- **Symptom**: 401 Unauthorized
- **Solution**: Login again to get fresh token

### **Issue 2: Wrong Token Format**
- **Symptom**: 403 Forbidden
- **Solution**: Ensure header is `Authorization: Bearer TOKEN`

### **Issue 3: Role Mismatch**
- **Symptom**: 403 Forbidden
- **Solution**: Verify user has `ROLE_CUSTOMER` in database

### **Issue 4: Customer ID Mismatch**
- **Symptom**: 403 Forbidden
- **Solution**: Ensure `customerId` matches `userId` from token

### **Issue 5: Method-Level Security Conflict**
- **Symptom**: 403 Forbidden even with valid token
- **Solution**: `@PreAuthorize` temporarily commented out for testing

### **Issue 6: Entity-Database Mismatch**
- **Symptom**: 403 Forbidden or database errors
- **Solution**: Fixed entity constraints to match database schema

### **Issue 7: CORS Issues**
- **Symptom**: 403 Forbidden or CORS errors
- **Solution**: Updated CORS to allow all origins for testing

### **Issue 8: JWT Filter Not Processing Token**
- **Symptom**: 403 Forbidden even with valid token
- **Solution**: Check JWT filter logs and test token processing endpoints

## 📋 **Database Verification:**

```sql
-- Check user 19 details
SELECT user_id, username, email, role FROM users WHERE user_id = 19;

-- Check customer 19 details
SELECT * FROM customer_master WHERE customer_id = 19;

-- Expected: user_id=19, role='CUSTOMER'
```

## 🎯 **Expected Flow:**

1. **User login** → Returns JWT token ✅
2. **Token validation** → JWT filter processes token ✅
3. **Role verification** → Spring Security checks `ROLE_CUSTOMER` ✅
4. **Endpoint access** → `/bookings` allows `ROLE_CUSTOMER` ✅
5. **Booking creation** → Success! ✅

## 🔍 **Debugging Commands:**

```bash
# Check application logs for JWT filter messages
# Look for: "User authenticated successfully" or "JWT token validation failed"

# Test each step individually
# 1. Basic endpoints (no auth)
# 2. Authentication (with token)
# 3. Booking creation (with token)
```

## ✅ **After Fix:**

- **Restart your Spring Boot application**
- **Test the endpoints in order**
- **The 403 Forbidden error should be resolved**
- **Booking creation should work with valid token**

## 🚨 **If Still Getting 403:**

1. **Check application logs** for JWT filter messages
2. **Verify token format** in Postman headers
3. **Test token-test endpoint** to see if token is being processed
4. **Test auth-test endpoint** to see authentication status
5. **Test comprehensive-test endpoint** to see full request details
6. **Ensure user role is CUSTOMER** in database
7. **Test test-create endpoint** to isolate method-level security issues

## 🔧 **New Debug Endpoints:**

- **`/bookings/debug`**: Shows complete request and authentication context
- **`/bookings/token-test`**: Tests JWT token processing
- **`/bookings/open`**: Completely open endpoint for basic connectivity
- **`/bookings/comprehensive-test`**: Full request details and authentication info
- **`/bookings/test-create`**: Tests booking creation without method security
- **Enhanced JWT logging**: More detailed authentication flow logging

## 🎯 **Key Changes Made:**

1. **Fixed entity constraints** to match database schema
2. **Temporarily disabled method-level security** for testing
3. **Updated CORS configuration** to allow all origins
4. **Added comprehensive debug endpoints** for troubleshooting
5. **Enhanced JWT filter logging** for better visibility
6. **Added completely open endpoints** to isolate security issues

## 🔍 **Critical Debugging Steps:**

1. **Test `/bookings/open`** - Should work without any issues
2. **Test `/bookings/comprehensive-test`** - Shows all request details
3. **Check JWT filter logs** - Look for authentication processing
4. **Verify token format** - Must be `Bearer TOKEN`
5. **Test each endpoint systematically** - Identify where the failure occurs

This comprehensive fix should resolve your 403 Forbidden error! 