# Booking API Debug Guide - 403 Forbidden Fix

## 🔍 **Step-by-Step Debugging Process**

### **Step 1: Test Authentication First**
```bash
# Test if auth endpoint works
curl -X GET http://localhost:6969/auth/test
# Should return: "Authentication test endpoint is working"
```

### **Step 2: Register a Customer**
```bash
curl -X POST http://localhost:6969/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Customer",
    "email": "customer@test.com",
    "password": "password123",
    "role": "CUSTOMER"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "Test Customer",
  "role": "CUSTOMER",
  "userId": 1
}
```

### **Step 3: Login to Get Token**
```bash
curl -X POST http://localhost:6969/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@test.com",
    "password": "password123"
  }'
```

**Save the token and userId from the response!**

### **Step 4: Test Token Validity**
```bash
# Replace YOUR_TOKEN_HERE with the actual token
curl -X GET http://localhost:6969/bookings/test \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

**Should return:** "Booking authentication test endpoint is working"

### **Step 5: Test Booking Creation**
```bash
# Replace YOUR_TOKEN_HERE and YOUR_CUSTOMER_ID with actual values
curl -X POST "http://localhost:6969/bookings?customerId=YOUR_CUSTOMER_ID&serviceId=1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "address": "123 Test Street, Test City, TC 12345",
    "scheduledDate": "2024-01-15",
    "notes": "Test booking for plumbing service"
  }'
```

## 🐛 **Common Issues and Solutions**

### **Issue 1: 403 Forbidden on /auth/test**
**Cause:** Server not running or wrong port
**Solution:** 
```bash
# Start the server
cd /path/to/your/project
mvn spring-boot:run
```

### **Issue 2: 403 Forbidden on /bookings/test**
**Cause:** Invalid or missing token
**Solution:**
1. Check if token is valid
2. Ensure token starts with "Bearer "
3. Check if token is not expired

### **Issue 3: 403 Forbidden on POST /bookings**
**Cause:** Wrong role or missing authorization
**Solution:**
1. Ensure you're logged in as CUSTOMER role
2. Check if token is included in Authorization header
3. Verify the customerId matches your user ID

## 🔧 **Complete Test Script**

```bash
#!/bin/bash

echo "=== Step 1: Test Server ==="
curl -X GET http://localhost:6969/auth/test

echo -e "\n=== Step 2: Register Customer ==="
REGISTER_RESPONSE=$(curl -s -X POST http://localhost:6969/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Customer",
    "email": "customer@test.com",
    "password": "password123",
    "role": "CUSTOMER"
  }')

echo $REGISTER_RESPONSE

echo -e "\n=== Step 3: Login ==="
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:6969/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@test.com",
    "password": "password123"
  }')

echo $LOGIN_RESPONSE

# Extract token and userId (you'll need to do this manually)
echo -e "\n=== Step 4: Test Booking Creation ==="
echo "Please extract token and userId from the login response above"
echo "Then run:"
echo 'curl -X POST "http://localhost:6969/bookings?customerId=YOUR_ID&serviceId=1" \'
echo '  -H "Content-Type: application/json" \'
echo '  -H "Authorization: Bearer YOUR_TOKEN" \'
echo '  -d '"'"'{"address": "123 Test St", "scheduledDate": "2024-01-15", "notes": "Test"}'"'"''
```

## 📋 **Expected Results**

### **Successful Flow:**
1. ✅ `/auth/test` - Returns "Authentication test endpoint is working"
2. ✅ `POST /auth/register` - Returns user data with token
3. ✅ `POST /auth/login` - Returns user data with token
4. ✅ `GET /bookings/test` - Returns "Booking authentication test endpoint is working"
5. ✅ `POST /bookings` - Returns 201 Created with booking data

### **Error Responses:**
- **401 Unauthorized** - Invalid/missing token
- **403 Forbidden** - Wrong role or insufficient permissions
- **400 Bad Request** - Invalid request data
- **404 Not Found** - Invalid endpoint or IDs

## 🎯 **Quick Fix Checklist**

- [ ] Server is running on port 6969
- [ ] You're logged in as CUSTOMER role
- [ ] Token is included in Authorization header
- [ ] Token starts with "Bearer "
- [ ] customerId matches your user ID
- [ ] serviceId exists in database (1-5)

## 🔍 **Debug Commands**

### **Check Server Logs:**
```bash
# Look for authentication errors
tail -f logs/application.log | grep -i "auth\|jwt\|token"
```

### **Check Database:**
```sql
-- Verify user exists
SELECT * FROM users WHERE email = 'customer@test.com';

-- Verify customer record exists
SELECT * FROM Customer_Master WHERE customer_id = (SELECT user_id FROM users WHERE email = 'customer@test.com');
```

Try this step-by-step process and let me know at which step you get the 403 error!







