# Database Sync Guide

## Problem
Your `users` table has records, but some users don't have corresponding records in `customer_master` and `contractor_master` tables.

## Current State
```
users table:
- user_id: 1, email: mayurmali161616@gmail.com, role: ADMIN
- user_id: 2, email: alice@example.com, role: ROLE_CUSTOMER
- user_id: 3, email: cp@example.com, role: ROLE_CONTRACTOR
- user_id: 4, email: vishal10@gmail.com, role: ROLE_CUSTOMER
- user_id: 5, email: anandmali1616@gmail.com, role: ROLE_CUSTOMER
- user_id: 6, email: harshal123@gmail.com, role: ROLE_CONTRACTOR
- user_id: 7, email: virat123@gmail.com, role: ROLE_CUSTOMER

customer_master table:
- customer_id: 2, customer_name: Alice Johnson
- customer_id: 4, customer_name: Vishal patil

contractor_master table:
- (empty or missing records)
```

## Solution

### 1. Start Your Spring Boot Application
```bash
cd Household_Repair_Project
.\mvnw.cmd spring-boot:run
```

### 2. Run the Sync Script
```bash
node sync_database.js
```

### 3. Manual Sync via API (Alternative)

#### Check Sync Status:
```bash
curl -X GET http://localhost:6969/api/sync/status
```

#### Sync All Users:
```bash
curl -X POST http://localhost:6969/api/sync/all-users
```

#### Sync Specific User:
```bash
curl -X POST http://localhost:6969/api/sync/user/3
```

### 4. Expected Results After Sync

#### customer_master table should have:
```
+-------------+------------------+---------------+-------------------+---------+
| customer_id | customer_address | customer_name | customer_phone_no | version |
+-------------+------------------+---------------+-------------------+---------+
|           2 | NULL             | Alice Johnson | NULL              |       0 |
|           4 | NULL             | Vishal patil  | NULL              |       0 |
|           5 | NULL             | Anand Bhai    | NULL              |       0 |
|           7 | NULL             | Virat         | NULL              |       0 |
+-------------+------------------+---------------+-------------------+---------+
```

#### contractor_master table should have:
```
+---------------+------------------+-------------------+----------+--------+---------+
| contractor_id | contractor_name  | contractor_phone_no | location | status | version |
+---------------+------------------+-------------------+----------+--------+---------+
|             3 | CP Johnson       | NULL               | NULL     | ACTIVE |       0 |
|             6 | Harshal          | NULL               | NULL     | ACTIVE |       0 |
+---------------+------------------+-------------------+----------+--------+---------+
```

## Verification

### 1. Check Database Tables
```sql
-- Check all customers
SELECT * FROM customer_master;

-- Check all contractors
SELECT * FROM contractor_master;

-- Verify user-customer relationship
SELECT u.user_id, u.email, u.role, c.customer_name 
FROM users u 
LEFT JOIN customer_master c ON u.user_id = c.customer_id 
WHERE u.role LIKE '%CUSTOMER%';

-- Verify user-contractor relationship
SELECT u.user_id, u.email, u.role, c.contractor_name 
FROM users u 
LEFT JOIN contractor_master c ON u.user_id = c.contractor_id 
WHERE u.role LIKE '%CONTRACTOR%';
```

### 2. Test Login Flow

#### Customer Login:
1. Login with: `alice@example.com` / `password123`
2. Should redirect to customer dashboard
3. Should show customer bookings

#### Contractor Login:
1. Login with: `cp@example.com` / `password123`
2. Should redirect to contractor dashboard
3. Should show all booking requests

## What the Sync Does

1. **Scans all users** in the `users` table
2. **Checks role** of each user
3. **Creates missing records**:
   - `ROLE_CUSTOMER` → Creates record in `customer_master`
   - `ROLE_CONTRACTOR` → Creates record in `contractor_master`
   - `ADMIN` → No additional record needed
4. **Links records** using `user_id` as foreign key

## Future Registration

New user registrations will automatically:
- Create user record in `users` table
- Create corresponding record in `customer_master` or `contractor_master`
- Link them properly with `user_id`

## Troubleshooting

### If sync fails:
1. Check if Spring Boot is running
2. Check database connection
3. Verify table structure matches entities
4. Check console logs for errors

### If records still missing:
1. Run sync again
2. Check database permissions
3. Verify foreign key constraints
4. Check entity mappings

## Success Criteria

✅ **All CUSTOMER users** have records in `customer_master`  
✅ **All CONTRACTOR users** have records in `contractor_master`  
✅ **user_id matches** between tables  
✅ **Login works** for all user types  
✅ **Role-based access** works correctly  
✅ **Booking system** functions properly  

After sync, your booking system will work correctly with proper user role reflection in the database tables.




