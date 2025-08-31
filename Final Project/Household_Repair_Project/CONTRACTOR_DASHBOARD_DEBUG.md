# Contractor Dashboard Debug Guide

## 🔍 Problem
Contractor dashboard is fetching booking data but not displaying it properly.

## 🛠️ Debugging Steps

### 1. Check Browser Console
1. Open browser developer tools (F12)
2. Go to Console tab
3. Look for any JavaScript errors
4. Check for the debug logs I added:
   - `🔍 Fetching bookings for contractor: [ID]`
   - `📊 API Response: [data]`
   - `✅ Bookings set: [data]`

### 2. Check Network Tab
1. Go to Network tab in developer tools
2. Refresh the contractor dashboard
3. Look for the API call to `/bookings/contractor/{id}`
4. Check:
   - Request URL
   - Request headers (Authorization)
   - Response status
   - Response data

### 3. Run Backend Test
```bash
node test_contractor_endpoint.js
```

This will test:
- Contractor registration/login
- Contractor bookings endpoint
- Pending bookings endpoint
- All bookings endpoint

### 4. Check Database Directly
```sql
-- Check if bookings exist
SELECT * FROM booking;

-- Check pending bookings
SELECT * FROM booking WHERE status = 'PENDING';

-- Check if contractor exists
SELECT * FROM contractor WHERE email = 'contractor@test.com';

-- Check if customer exists
SELECT * FROM customer WHERE email = 'customer@test.com';
```

### 5. Manual API Testing
```bash
# Login as contractor
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "contractor@test.com",
    "password": "password123"
  }'

# Use the token from above to test contractor bookings
curl -X GET "http://localhost:8080/bookings/contractor/CONTRACTOR_ID" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 🔧 Common Issues and Solutions

### Issue 1: No Bookings in Database
**Symptoms:** API returns empty array
**Solution:** Create a test booking first

### Issue 2: Authentication Error
**Symptoms:** 401 Unauthorized
**Solution:** 
- Check if contractor is logged in
- Verify JWT token is valid
- Check if token is being sent in headers

### Issue 3: CORS Error
**Symptoms:** CORS error in browser console
**Solution:** Check backend CORS configuration

### Issue 4: Wrong Contractor ID
**Symptoms:** No bookings returned
**Solution:** Verify contractor ID is correct

### Issue 5: Backend Not Running
**Symptoms:** Network error
**Solution:** Start backend server

## 🧪 Step-by-Step Testing

### Step 1: Create Test Data
```bash
# Register customer
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Customer",
    "email": "customer@test.com",
    "phone": "1234567890",
    "password": "password123",
    "role": "CUSTOMER"
  }'

# Register contractor
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Contractor",
    "email": "contractor@test.com",
    "phone": "0987654321",
    "password": "password123",
    "role": "CONTRACTOR"
  }'
```

### Step 2: Create Test Booking
```bash
# Login as customer
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@test.com",
    "password": "password123"
  }'

# Create booking (replace CUSTOMER_ID and TOKEN)
curl -X POST "http://localhost:8080/bookings?customerId=CUSTOMER_ID&serviceId=1" \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "address": "123 Test Street",
    "scheduledDate": "2024-12-25",
    "notes": "Test booking"
  }'
```

### Step 3: Test Contractor Dashboard
```bash
# Login as contractor
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "contractor@test.com",
    "password": "password123"
  }'

# Check contractor bookings (replace CONTRACTOR_ID and TOKEN)
curl -X GET "http://localhost:8080/bookings/contractor/CONTRACTOR_ID" \
  -H "Authorization: Bearer TOKEN"
```

## 📊 Expected Results

### API Response Format
```json
[
  {
    "id": 1,
    "address": "123 Test Street",
    "scheduledDate": "2024-12-25",
    "notes": "Test booking",
    "status": "PENDING",
    "customer": {
      "customerId": 1,
      "name": "Test Customer",
      "email": "customer@test.com",
      "phone": "1234567890"
    },
    "service": {
      "id": 1,
      "serviceName": "Test Service",
      "description": "Test Description"
    },
    "contractor": null
  }
]
```

### Frontend Display
- Dashboard should show booking cards
- Each card should display:
  - Service name
  - Customer details
  - Address
  - Scheduled date
  - Notes
  - Accept/Reject buttons (for pending bookings)

## 🚨 Emergency Debugging

If nothing works, try this:

1. **Clear browser cache and cookies**
2. **Restart both frontend and backend**
3. **Check if backend is running on correct port**
4. **Verify database connection**
5. **Run the test script to verify API works**

## 📝 Debug Checklist

- [ ] Backend is running on port 8080
- [ ] Frontend is running on port 3000
- [ ] Contractor is registered and logged in
- [ ] Customer is registered and logged in
- [ ] At least one booking exists in database
- [ ] API call returns 200 status
- [ ] API response contains booking data
- [ ] No JavaScript errors in browser console
- [ ] No CORS errors
- [ ] JWT token is valid and being sent

## 🎯 Quick Fix

If you're still having issues, try this simplified approach:

1. **Use the updated ContractorDashboard.jsx** (with debug info)
2. **Run the test script** to verify backend works
3. **Check browser console** for debug logs
4. **Look at the debug info** displayed on the dashboard

The updated dashboard will show:
- Total bookings fetched
- Number of pending/accepted/completed bookings
- Raw API response data
- All bookings in a debug view

This will help identify exactly where the issue is occurring.







