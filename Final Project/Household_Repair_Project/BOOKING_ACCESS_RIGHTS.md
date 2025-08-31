# Booking API Access Rights

## ✅ **Correct Access Rights:**

### **🔧 CONTRACTORS can access:**
- `GET /bookings/pending` - View all pending bookings
- `GET /bookings/contractor/{contractorId}` - View their assigned bookings
- `GET /bookings/contractor/{contractorId}/accepted` - View their accepted bookings
- `GET /bookings/contractor/{contractorId}/completed` - View their completed bookings
- `PUT /bookings/{id}/accept` - Accept a booking
- `PUT /bookings/{id}/reject` - Reject a booking

### **👤 CUSTOMERS can access:**
- `POST /bookings` - Create new bookings
- `GET /bookings/customer/{customerId}` - View their own bookings
- `PUT /bookings/{id}/confirm` - Confirm completed bookings

### **👨‍💼 ADMINS can access:**
- All booking endpoints
- `PUT /bookings/{id}/assign` - Assign bookings to contractors

## 🚫 **What was wrong before:**
- Customers could access `/bookings/pending` (should be contractors only)
- No proper role-based restrictions
- All authenticated users could access all booking endpoints

## ✅ **What's fixed now:**
- Contractors can see pending bookings
- Customers can only see their own bookings
- Proper role-based access control
- Method-level security annotations added

## 🧪 **Testing the Fix:**

### **Test as Contractor:**
```bash
# 1. Register a contractor
curl -X POST http://localhost:6969/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Contractor",
    "email": "contractor@test.com",
    "password": "password123",
    "role": "CONTRACTOR"
  }'

# 2. Login as contractor
curl -X POST http://localhost:6969/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "contractor@test.com",
    "password": "password123"
  }'

# 3. Test contractor access (should work)
curl -X GET http://localhost:6969/bookings/pending \
  -H "Authorization: Bearer YOUR_CONTRACTOR_TOKEN"
```

### **Test as Customer:**
```bash
# 1. Register a customer
curl -X POST http://localhost:6969/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Customer",
    "email": "customer@test.com",
    "password": "password123",
    "role": "CUSTOMER"
  }'

# 2. Login as customer
curl -X POST http://localhost:6969/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "customer@test.com",
    "password": "password123"
  }'

# 3. Test customer access to pending bookings (should fail with 403)
curl -X GET http://localhost:6969/bookings/pending \
  -H "Authorization: Bearer YOUR_CUSTOMER_TOKEN"

# 4. Test customer access to their own bookings (should work)
curl -X GET http://localhost:6969/bookings/customer/YOUR_CUSTOMER_ID \
  -H "Authorization: Bearer YOUR_CUSTOMER_TOKEN"
```

## 📋 **Expected Results:**

### **Contractor Access:**
- ✅ Can access `/bookings/pending`
- ✅ Can access `/bookings/contractor/{id}`
- ❌ Cannot access `/bookings/customer/{id}` (403 Forbidden)

### **Customer Access:**
- ❌ Cannot access `/bookings/pending` (403 Forbidden)
- ✅ Can access `/bookings/customer/{id}`
- ✅ Can create bookings with `POST /bookings`

### **Admin Access:**
- ✅ Can access all booking endpoints

## 🔧 **Files Modified:**
- `SecurityConfig.java` - Updated security rules
- `BookingController.java` - Added method-level security annotations

## 🎯 **Key Changes:**
1. **Security Configuration**: Added role-based access control for booking endpoints
2. **Method Security**: Added `@PreAuthorize` annotations to controller methods
3. **Proper Restrictions**: Contractors can see pending bookings, customers cannot

This fix ensures that contractors have the right to see bookings while customers are properly restricted to their own data. 