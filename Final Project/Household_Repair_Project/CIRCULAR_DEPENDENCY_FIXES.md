# Circular Dependency Fixes

## Issues Fixed:

1. **Users ↔ Customer**: Added @JsonManagedReference to Users, @JsonBackReference to Customer
2. **Users ↔ Contractor**: Already had @JsonManagedReference to Users, @JsonBackReference to Contractor  
3. **Services ↔ SubServices**: Added @JsonManagedReference to Services, @JsonBackReference to SubServices
4. **Booking ↔ Related Entities**: Added @JsonBackReference to Customer, Services, SubServices in Booking

## Files Modified:
- Users.java - Added @JsonManagedReference to customer relationship
- Customer.java - Added @JsonBackReference to user relationship
- Services.java - Added @JsonManagedReference to collections
- SubServices.java - Added @JsonBackReference to service relationship
- Booking.java - Added @JsonBackReference to all related entities

## Expected Result:
- No more circular reference errors in JSON serialization
- Booking API endpoints should work properly
- Registration and login should work without JSON errors

## Test:
1. Restart Spring Boot application
2. Test registration endpoint
3. Test booking/pending endpoint
4. Check for no circular dependency warnings in logs 