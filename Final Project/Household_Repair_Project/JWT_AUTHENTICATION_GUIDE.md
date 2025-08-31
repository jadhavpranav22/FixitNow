# JWT Authentication Implementation Guide

## Overview
This guide covers the complete JWT authentication implementation for the Household Repair Project, including both backend (Spring Boot) and frontend (React) components.

## Backend Implementation

### 1. Security Configuration (`SecurityConfig.java`)
- **Method Security**: Enabled with `@EnableMethodSecurity(prePostEnabled = true)`
- **JWT Filter**: Added before `UsernamePasswordAuthenticationFilter`
- **CORS**: Configured for `http://localhost:3000`
- **Endpoint Security**:
  - `/auth/**` - Public access
  - `/api/services/**` - Public access
  - `/bookings/**` - Requires authentication
  - `/admin/**` - Requires `ROLE_ADMIN`
  - `/user/**` - Requires `ROLE_CUSTOMER`
  - `/provider/**` - Requires `ROLE_CONTRACTOR`

### 2. JWT Filter (`JwtAuthFilter.java`)
- Extracts Bearer token from Authorization header
- Validates token using `JwtUtil`
- Loads user details via `CustomUserDetailsService`
- Sets authentication in SecurityContext

### 3. JWT Utility (`JwtUtil.java`)
- Generates tokens with user email and role
- Validates tokens against user details
- Extracts username and roles from tokens
- Uses HMAC-SHA256 signing algorithm

### 4. User Details Service (`CustomUserDetailsService.java`)
- Loads users by email from database
- Ensures roles have `ROLE_` prefix
- Returns Spring Security UserDetails

## Frontend Implementation

### 1. Authentication Context (`AuthContext.js`)
- Manages authentication state (token, role, email, userId)
- Provides login/logout functions
- Validates token format and expiration
- Syncs with localStorage

### 2. Axios Configuration (`axiosConfig.js`)
- Automatic JWT token injection via request interceptor
- 401 error handling with automatic logout
- Base URL configuration

### 3. Protected Routes (`ProtectedRoute.jsx`)
- Role-based access control
- Loading states
- Automatic redirects for unauthorized access

### 4. Booking Form (`BookingForm.jsx`)
- Uses configured axios instance
- Validates user role (CUSTOMER only)
- Proper error handling and loading states

## Key Features

### Token Management
- **Generation**: Backend generates tokens with user email and role
- **Storage**: Frontend stores in localStorage
- **Validation**: Both frontend and backend validate tokens
- **Expiration**: 10-hour expiration with automatic cleanup

### Role-Based Access Control
- **Backend**: URL-based and method-based security
- **Frontend**: Route protection and component-level checks
- **Roles**: CUSTOMER, CONTRACTOR, ADMIN

### Error Handling
- **Backend**: Comprehensive logging and error responses
- **Frontend**: User-friendly error messages and automatic logout

## Troubleshooting Guide

### "Missing authentication info" Error

#### Common Causes:
1. **Token not sent**: Check if Authorization header is present
2. **Invalid token format**: Verify Bearer prefix and token structure
3. **Expired token**: Check token expiration time
4. **User not found**: Verify user exists in database
5. **Role mismatch**: Ensure user has correct role assigned

#### Debugging Steps:

1. **Check Browser Network Tab**:
   ```
   Request Headers:
   Authorization: Bearer <your-token>
   Content-Type: application/json
   ```

2. **Verify Token Format**:
   ```javascript
   // Token should have 3 parts separated by dots
   const parts = token.split('.');
   console.log('Token parts:', parts.length); // Should be 3
   ```

3. **Decode Token Payload**:
   ```javascript
   const payload = JSON.parse(atob(token.split('.')[1]));
   console.log('Token payload:', payload);
   ```

4. **Check Backend Logs**:
   ```
   DEBUG level logging enabled for:
   - com.household_repair.Config.JwtAuthFilter
   - com.household_repair.Config.JwtUtil
   - org.springframework.security
   ```

5. **Use Debug Component**:
   - The `AuthDebug` component shows current auth state
   - Test authentication with "Test Auth" button
   - Validate token with "Validate Token" button

### Testing Authentication Flow

1. **Login Process**:
   ```javascript
   // 1. User submits login form
   const response = await api.post('/auth/login', { email, password });
   
   // 2. Backend validates credentials and returns token
   const { token, role, userId } = response.data;
   
   // 3. Frontend stores token and redirects
   login({ token, role, email, userId });
   ```

2. **API Request Process**:
   ```javascript
   // 1. Axios interceptor adds token
   config.headers.Authorization = `Bearer ${token}`;
   
   // 2. Backend JWT filter validates token
   // 3. UserDetails loaded and authentication set
   // 4. Request proceeds to controller
   ```

### Common Issues and Solutions

#### Issue: Token not being sent
**Solution**: Check axios interceptor configuration
```javascript
// Ensure this is in axiosConfig.js
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

#### Issue: Token validation fails
**Solution**: Check token generation and validation
```java
// Backend token generation
String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

// Frontend token validation
const isValid = isValidToken(token);
```

#### Issue: User not found
**Solution**: Verify user exists and role is correct
```java
// Check database for user
Users user = userRepo.findByEmailIgnoreCase(email.toLowerCase())
    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
```

#### Issue: Role-based access denied
**Solution**: Ensure correct role format
```java
// Backend role format
if (!role.startsWith("ROLE_")) {
    role = "ROLE_" + role.toUpperCase();
}
```

## Best Practices

### Security
- Use HTTPS in production
- Implement token refresh mechanism
- Add rate limiting for auth endpoints
- Log security events

### Performance
- Cache user details when possible
- Use efficient token validation
- Minimize database queries

### User Experience
- Clear error messages
- Loading states during auth operations
- Automatic logout on token expiration
- Remember user preferences

## Development Setup

1. **Backend**:
   ```bash
   cd Household_Repair_Project
   mvn spring-boot:run
   ```

2. **Frontend**:
   ```bash
   cd my-app/my-app
   npm start
   ```

3. **Database**: Ensure MySQL is running with correct credentials

4. **Testing**: Use the AuthDebug component to test authentication flow

## Production Considerations

1. **Environment Variables**: Move sensitive data to environment variables
2. **Token Secret**: Use a strong, unique secret key
3. **CORS**: Configure for production domain
4. **Logging**: Reduce debug logging in production
5. **Error Handling**: Implement proper error pages and messages 