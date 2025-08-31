# ID Generation Fix for Contractor Entity

## Issue Identified:
**Error**: "Identifier of entity 'com.household_repair.entity.Contractor' must be manually assigned before calling 'persist()'"

## Root Cause:
The `Contractor` entity was missing proper ID mapping configuration for the `@MapsId` relationship with the `Users` entity.

## Fixes Applied:

### 1. **Contractor Entity Fix**
```java
// Before (incorrect)
@OneToOne
@JoinColumn(name = "user_id")
private Users user;

// After (correct)
@OneToOne
@MapsId
@JoinColumn(name = "contractor_id")
private Users user;
```

### 2. **ContractorService Fix**
```java
// Before (missing ID assignment)
public Contractor registerContractorWithUser(Users user, String contractorName) {
    Contractor contractor = new Contractor();
    contractor.setUser(user);
    contractor.setContractorName(contractorName);
    return contractorRepoRef.save(contractor);
}

// After (with proper ID assignment)
@Transactional
public Contractor registerContractorWithUser(Users user, String contractorName) {
    Contractor contractor = new Contractor();
    contractor.setContractorId(user.getUserId()); // Set the ID to match the user ID
    contractor.setUser(user);
    contractor.setContractorName(contractorName);
    return contractorRepoRef.save(contractor);
}
```

## How @MapsId Works:

1. **Customer Entity** (working correctly):
   - Uses `@MapsId` to map `customer_id` to `user_id`
   - The customer ID is the same as the user ID

2. **Contractor Entity** (now fixed):
   - Uses `@MapsId` to map `contractor_id` to `user_id`
   - The contractor ID should be the same as the user ID

## Database Schema Requirements:

The database tables should have:
- `users` table with `user_id` as primary key
- `Customer_Master` table with `customer_id` as primary key (same as user_id)
- `Contractor_Master` table with `contractor_id` as primary key (same as user_id)

## Expected Behavior:

1. **User Registration**: Creates user record with auto-generated ID
2. **Customer Registration**: Creates customer record with same ID as user
3. **Contractor Registration**: Creates contractor record with same ID as user

## Testing:

### Test Registration for Customer:
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

### Test Registration for Contractor:
```bash
curl -X POST http://localhost:6969/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Contractor",
    "email": "contractor@test.com",
    "password": "password123",
    "role": "CONTRACTOR"
  }'
```

## Verification:

Check database after registration:
```sql
-- Check users table
SELECT * FROM users WHERE email = 'contractor@test.com';

-- Check contractor table (should have same ID as user)
SELECT * FROM Contractor_Master WHERE contractor_id = (SELECT user_id FROM users WHERE email = 'contractor@test.com');
```

## Files Modified:
- `src/main/java/com/household_repair/entity/Contractor.java`
- `src/main/java/com/household_repair/service/ContractorService.java`

This fix should resolve the ID generation issue and allow contractor registration to work properly. 