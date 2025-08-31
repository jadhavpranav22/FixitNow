package com.household_repair.service;

import com.household_repair.dto.BookingDto;
import com.household_repair.dto.BookingMultipleRequestDto;
import com.household_repair.entity.*;
import com.household_repair.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepository;
    @Autowired
    private CustomerRepo customerRepository;
    @Autowired
    private ServiceRepo serviceRepository;
    @Autowired
    private SubServiceRepo subServiceRepository;
    @Autowired
    private ContractorRepo contractorRepository;

    // Create single booking
    public Booking createBooking(Booking booking, Long customerId, Long serviceId, Long subServiceId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        booking.setCustomer(customer);
        booking.setService(service);
        
        // Set subservice if provided
        if (subServiceId != null) {
            SubServices subService = subServiceRepository.findById(subServiceId)
                    .orElseThrow(() -> new RuntimeException("SubService not found"));
            booking.setSubservice(subService);
        }
        
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    // Create multiple bookings
    public List<Booking> createMultipleBookings(BookingMultipleRequestDto dto) {
        List<Booking> savedBookings = new ArrayList<>();
        for (BookingMultipleRequestDto.CartItem item : dto.getCartItems()) {
            Booking booking = new Booking();
            booking.setAddress(dto.getAddress());
            booking.setScheduledDate(dto.getScheduledDate());
            booking.setNotes(dto.getNotes());
            savedBookings.add(createBooking(booking, dto.getCustomerId(),
                    item.getServiceId(), item.getSubServiceId()));
        }
        return savedBookings;
    }

    // Assign booking to contractor
    public Booking assignBooking(Long bookingId, Long contractorId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        Contractor contractor = contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found"));

        booking.setContractor(contractor);
        booking.setStatus(BookingStatus.ASSIGNED_TO_CONTRACTOR);
        return bookingRepository.save(booking);
    }

    public Booking contractorAccept(Long bookingId, Long contractorId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setContractor(contractorRepository.findById(contractorId)
                .orElseThrow(() -> new RuntimeException("Contractor not found")));
        booking.setStatus(BookingStatus.ACCEPTED_BY_CONTRACTOR);
        return bookingRepository.save(booking);
    }

    public Booking contractorReject(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.REJECTED_BY_CONTRACTOR);
        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getPendingBookings() {
        return bookingRepository.findByStatus(BookingStatus.PENDING);
    }

    // Contractor-specific bookings: Pending + assigned to them
    public List<Booking> getBookingsByContractor(Long contractorId) {
        List<Booking> visibleBookings = new ArrayList<>();

        // Include all PENDING bookings
        visibleBookings.addAll(bookingRepository.findByStatus(BookingStatus.PENDING));

        // Include bookings assigned to this contractor
        List<Booking> myBookings = bookingRepository.findByContractorContractorId(contractorId);
        for (Booking b : myBookings) {
            if (!visibleBookings.contains(b)) visibleBookings.add(b);
        }

        return visibleBookings;
    }

    public List<Booking> getAcceptedBookingsByContractor(Long contractorId) {
        return bookingRepository.findByContractorContractorIdAndStatus(contractorId, BookingStatus.ACCEPTED_BY_CONTRACTOR);
    }

    public List<Booking> getCompletedBookingsByContractor(Long contractorId) {
        return bookingRepository.findByContractorContractorIdAndStatus(contractorId, BookingStatus.COMPLETED);
    }

    public List<Booking> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerCustomerId(customerId);
    }

    // DTO conversion methods to avoid circular dependency
    public List<BookingDto> getAllBookingsDto() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getPendingBookingsDto() {
        return bookingRepository.findByStatus(BookingStatus.PENDING).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getBookingsByContractorDto(Long contractorId) {
        List<Booking> bookings = getBookingsByContractor(contractorId);
        return bookings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getAcceptedBookingsByContractorDto(Long contractorId) {
        return bookingRepository.findByContractorContractorIdAndStatus(contractorId, BookingStatus.ACCEPTED_BY_CONTRACTOR).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getCompletedBookingsByContractorDto(Long contractorId) {
        return bookingRepository.findByContractorContractorIdAndStatus(contractorId, BookingStatus.COMPLETED).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookingDto> getBookingsByCustomerDto(Long customerId) {
        return bookingRepository.findByCustomerCustomerId(customerId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BookingDto convertToDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setAddress(booking.getAddress());
        dto.setNotes(booking.getNotes());
        dto.setScheduledDate(booking.getScheduledDate());
        dto.setStatus(booking.getStatus());
        
        // Contractor info
        if (booking.getContractor() != null) {
            dto.setContractorId(booking.getContractor().getContractorId());
            dto.setContractorName(booking.getContractor().getContractorName());
            dto.setContractorPhoneNo(booking.getContractor().getContractorPhoneNo());
        }
        
        // Customer info
        if (booking.getCustomer() != null) {
            dto.setCustomerId(booking.getCustomer().getCustomerId());
            dto.setCustomerName(booking.getCustomer().getCustomerName());
            if (booking.getCustomer().getUser() != null) {
                dto.setCustomerEmail(booking.getCustomer().getUser().getEmail());
            }
        }
        
        // Service info
        if (booking.getService() != null) {
            dto.setServiceId(booking.getService().getServicesid());
            dto.setServiceName(booking.getService().getServicesname());
        }
        
        // SubService info
        if (booking.getSubservice() != null) {
            dto.setSubServiceId(booking.getSubservice().getSubserviceid());
            dto.setSubServiceName(booking.getSubservice().getSubservicename());
        }
        
        return dto;
    }
}
