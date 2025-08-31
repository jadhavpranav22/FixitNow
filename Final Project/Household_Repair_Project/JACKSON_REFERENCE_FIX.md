# 🔧 **Jackson Reference Fix - Contractor-Bookings Relationship**

## 🚨 **Error Fixed:**
```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: 
Cannot handle managed/back reference 'contractor-bookings': 
back reference type (java.util.List<com.household_repair.entity.Booking>) 
not compatible with managed type (com.household_repair.entity.Booking)
```

## 🔍 **Root Cause:**
The Jackson reference mapping was incorrect for the Contractor-Booking relationship:

### **Before (Incorrect):**
- **Booking Entity**: `@JsonManagedReference("contractor-bookings")` ❌
- **Contractor Entity**: `@JsonBackReference("contractor-bookings")` ❌

### **Problem:**
- **Booking** (ManyToOne) was marked as `@JsonManagedReference` (parent side)
- **Contractor** (OneToMany) was marked as `@JsonBackReference` (child side)
- This creates a mismatch because the relationship is **Contractor (One) ←→ Booking (Many)**

## ✅ **Solution Applied:**
Fixed the Jackson reference mapping to correctly represent the One-to-Many relationship:

### **After (Correct):**
- **Contractor Entity**: `@JsonManagedReference("contractor-bookings")` ✅ (parent side)
- **Booking Entity**: `@JsonBackReference("contractor-bookings")` ✅ (child side)

## 📋 **Changes Made:**

### **1. Booking Entity:**
```java
// Before (incorrect)
@JsonManagedReference("contractor-bookings") // marks this as the "parent" side
private Contractor contractor;

// After (correct)
@JsonBackReference("contractor-bookings") // marks this as the "child" side
private Contractor contractor;
```

### **2. Contractor Entity:**
```java
// Before (incorrect)
@JsonBackReference("contractor-bookings")
private List<Booking> bookings;

// After (correct)
@JsonManagedReference("contractor-bookings")
private List<Booking> bookings;
```

## 🎯 **How Jackson References Work:**

### **@JsonManagedReference:**
- Used on the **parent/owning** side of relationships
- **Contractor** owns the `List<Booking>` relationship
- This is the side that gets serialized

### **@JsonBackReference:**
- Used on the **child/owned** side of relationships
- **Booking** is owned by `Contractor`
- This side is excluded from serialization to prevent circular references

## 🔧 **Relationship Mapping:**

```
Contractor (One) ←→ Booking (Many)
     ↑                    ↑
@JsonManagedReference  @JsonBackReference
"contractor-bookings"  "contractor-bookings"
```

## ✅ **Expected Results:**
- No more Jackson serialization errors
- Proper JSON serialization without circular references
- Contractor can serialize with its bookings list
- Booking can reference contractor without infinite loops

## 🚨 **Important Notes:**
- **Restart required** - Entity annotation changes require application restart
- **Reference names must match** - Both sides must use the same reference name
- **Parent side uses @JsonManagedReference** - This is what gets serialized
- **Child side uses @JsonBackReference** - This prevents circular references

## 🧪 **Test After Fix:**
1. **Restart your Spring Boot application**
2. **Test the booking endpoints** - should work without Jackson errors
3. **Test contractor endpoints** - should serialize properly with bookings
4. **No more 403 Forbidden errors** related to Jackson serialization

This fix should resolve the Jackson serialization error and allow your booking API to work properly! 