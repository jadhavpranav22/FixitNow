package com.household_repair.controller;

import com.household_repair.dto.BookingDto;
import com.household_repair.dto.BookingMultipleRequestDto;
import com.household_repair.entity.Booking;
import com.household_repair.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/test")
    public ResponseEntity<?> testBookingAuth() {
        return ResponseEntity.ok("Booking authentication test endpoint is working");
    }

    @GetMapping("/auth-test")
    public ResponseEntity<?> testBookingAuthWithUser() {
        // Get current authenticated user info
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "username", auth.getName(),
                "authorities", auth.getAuthorities().stream()
                    .map(Object::toString)
                    .collect(Collectors.toList()),
                "principal", auth.getPrincipal().toString()
            ));
        } else {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
    }

    @GetMapping("/debug")
    public ResponseEntity<?> debugSecurity() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        return ResponseEntity.ok(Map.of(
            "authentication", auth != null ? Map.of(
                "authenticated", auth.isAuthenticated(),
                "name", auth.getName(),
                "authorities", auth.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()),
                "principal", auth.getPrincipal().toString()
            ) : "null",
            "request", Map.of(
                "method", request.getMethod(),
                "uri", request.getRequestURI(),
                "queryString", request.getQueryString(),
                "authorizationHeader", request.getHeader("Authorization")
            )
        ));
    }

    @GetMapping("/token-test")
    public ResponseEntity<?> testToken() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        String authHeader = request.getHeader("Authorization");
        boolean hasToken = authHeader != null && authHeader.startsWith("Bearer ");
        
        return ResponseEntity.ok(Map.of(
            "hasToken", hasToken,
            "tokenLength", hasToken ? authHeader.substring(7).length() : 0,
            "authentication", auth != null ? Map.of(
                "authenticated", auth.isAuthenticated(),
                "name", auth.getName(),
                "authorities", auth.getAuthorities().stream().map(Object::toString).collect(Collectors.toList())
            ) : "null"
        ));
    }

    @GetMapping("/open")
    public ResponseEntity<?> openEndpoint() {
        return ResponseEntity.ok(Map.of(
            "message", "This endpoint is completely open - no security restrictions",
            "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/comprehensive-test")
    public ResponseEntity<?> comprehensiveTest() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        // Get all headers
        var headers = new java.util.HashMap<String, String>();
        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Comprehensive test endpoint",
            "timestamp", System.currentTimeMillis(),
            "request", Map.of(
                "method", request.getMethod(),
                "uri", request.getRequestURI(),
                "queryString", request.getQueryString(),
                "headers", headers
            ),
            "authentication", auth != null ? Map.of(
                "authenticated", auth.isAuthenticated(),
                "name", auth.getName(),
                "authorities", auth.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()),
                "principal", auth.getPrincipal().toString(),
                "details", auth.getDetails() != null ? auth.getDetails().toString() : "null"
            ) : "null"
        ));
    }

    @PostMapping("/test-create")
    public ResponseEntity<?> testCreateBooking(
            @RequestParam Long customerId,
            @RequestParam Long serviceId,
            @RequestParam(required = false) Long subServiceId,
            @RequestBody Booking booking) {
        
        var auth = SecurityContextHolder.getContext().getAuthentication();
        
        return ResponseEntity.ok(Map.of(
            "message", "Test endpoint reached successfully",
            "authentication", auth != null ? Map.of(
                "authenticated", auth.isAuthenticated(),
                "name", auth.getName(),
                "authorities", auth.getAuthorities().stream().map(Object::toString).collect(Collectors.toList())
            ) : "null",
            "parameters", Map.of(
                "customerId", customerId,
                "serviceId", serviceId,
                "subServiceId", subServiceId
            )
        ));
    }

    @PostMapping
    // @PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_ADMIN')") // Temporarily commented out for testing
    public ResponseEntity<?> createBooking(
            @RequestParam Long customerId,
            @RequestParam Long serviceId,
            @RequestParam(required = false) Long subServiceId,
            @RequestBody Booking booking) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(bookingService.createBooking(booking, customerId, serviceId, subServiceId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create-multiple")
    public ResponseEntity<?> createMultipleBookings(@RequestBody BookingMultipleRequestDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(bookingService.createMultipleBookings(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignBooking(@PathVariable Long id, @RequestParam Long contractorId) {
        try {
            return ResponseEntity.ok(bookingService.assignBooking(id, contractorId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<?> contractorAccept(@PathVariable Long id, @RequestParam Long contractorId) {
        try {
            return ResponseEntity.ok(bookingService.contractorAccept(id, contractorId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> contractorReject(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.contractorReject(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirmBooking(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(bookingService.confirmBooking(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookingsDto());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ROLE_CONTRACTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BookingDto>> getPendingBookings() {
        return ResponseEntity.ok(bookingService.getPendingBookingsDto());
    }

    @GetMapping("/contractor/{contractorId}")
    @PreAuthorize("hasAuthority('ROLE_CONTRACTOR') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BookingDto>> getBookingsByContractor(@PathVariable Long contractorId) {
        return ResponseEntity.ok(bookingService.getBookingsByContractorDto(contractorId));
    }

    @GetMapping("/contractor/{contractorId}/accepted")
    public ResponseEntity<List<BookingDto>> getAcceptedBookingsByContractor(@PathVariable Long contractorId) {
        return ResponseEntity.ok(bookingService.getAcceptedBookingsByContractorDto(contractorId));
    }

    @GetMapping("/contractor/{contractorId}/completed")
    public ResponseEntity<List<BookingDto>> getCompletedBookingsByContractor(@PathVariable Long contractorId) {
        return ResponseEntity.ok(bookingService.getCompletedBookingsByContractorDto(contractorId));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BookingDto>> getBookingsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomerDto(customerId));
    }
}
