# Jackson Circular Reference Fix

## 🚨 **Error Fixed:**
```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: 
Multiple back-reference properties with name 'defaultReference'
```

## 🔍 **Root Cause:**
Multiple entities were using `@JsonManagedReference` and `@JsonBackReference` without unique names, causing Jackson to create multiple back-references with the same default name.

## ✅ **Solution Applied:**
Added unique names to all JSON reference annotations to prevent conflicts.

## 📋 **Changes Made:**

### **1. Users Entity:**
```java
// Before (conflicting)
@JsonManagedReference
private Customer customer;

@JsonManagedReference
private Contractor contractor;

// After (unique names)
@JsonManagedReference("user-customer")
private Customer customer;

@JsonManagedReference("user-contractor")
private Contractor contractor;
```

### **2. Customer Entity:**
```java
// Before
@JsonBackReference
private Users user;

// After
@JsonBackReference("user-customer")
private Users user;
```

### **3. Contractor Entity:**
```java
// Before
@JsonBackReference
private Users user;

// After
@JsonBackReference("user-contractor")
private Users user;
```

### **4. Services Entity:**
```java
// Before (conflicting)
@JsonManagedReference
private List<SubServices> subServices;

@JsonManagedReference
private List<Booking> bookings;

// After (unique names)
@JsonManagedReference("service-subservices")
private List<SubServices> subServices;

@JsonManagedReference("service-bookings")
private List<Booking> bookings;
```

### **5. SubServices Entity:**
```java
// Before
@JsonBackReference
private Services service;

// After
@JsonBackReference("service-subservices")
private Services service;
```

### **6. Booking Entity:**
```java
// Before (conflicting)
@JsonManagedReference // missing name for contractor
private Contractor contractor;

@JsonBackReference
private Customer customer;

@JsonBackReference
private Services service;

@JsonBackReference
private SubServices subservice;

// After (unique names)
@JsonManagedReference("contractor-bookings")
private Contractor contractor;

@JsonBackReference("user-customer")
private Customer customer;

@JsonBackReference("service-bookings")
private Services service;

@JsonBackReference("service-subservices")
private SubServices subservice;
```

### **7. Contractor Entity (Updated):**
```java
// Added missing relationship
@OneToMany(mappedBy = "contractor")
@JsonBackReference("contractor-bookings")
private List<Booking> bookings;
```

## 🎯 **How It Works:**

1. **@JsonManagedReference("name")** - Marks the "parent" side of relationships
2. **@JsonBackReference("name")** - Marks the "child" side with matching name
3. **Unique names prevent conflicts** - Each relationship pair has its own unique identifier

## 🔧 **Files Modified:**
- `Users.java` - Added unique names to customer and contractor references
- `Customer.java` - Added unique name to user reference
- `Contractor.java` - Added unique name to user reference + added bookings relationship
- `Services.java` - Added unique names to subservices and bookings references
- `SubServices.java` - Added unique name to service reference
- `Booking.java` - Added unique names to all entity references including contractor

## ✅ **Expected Results:**
- No more Jackson circular reference errors
- Proper JSON serialization
- Authentication and booking APIs should work correctly
- No more "Multiple back-reference properties" errors

## 🧪 **Test the Fix:**
1. **Restart your Spring Boot application**
2. **Test registration endpoint** - should work without JSON errors
3. **Test login endpoint** - should return proper token
4. **Test booking creation** - should work with valid token

## 🚨 **Important Notes:**
- **Restart required** - Changes to entity annotations require application restart
- **All references must have unique names** - No more default names
- **Matching names required** - Managed and Back references must use the same name

This fix should resolve the Jackson serialization error and allow your booking API to work properly! 