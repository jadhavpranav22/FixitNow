package com.household_repair.dto;

import com.household_repair.entity.BookingStatus;
import java.time.LocalDate;

public class BookingDto {
    private Long id;
    private String address;
    private String notes;
    private LocalDate scheduledDate;
    private BookingStatus status;
    
    // Contractor info (simplified)
    private Long contractorId;
    private String contractorName;
    private String contractorPhoneNo;
    
    // Customer info (simplified)
    private Long customerId;
    private String customerName;
    private String customerEmail;
    
    // Service info (simplified)
    private Long serviceId;
    private String serviceName;
    private Long subServiceId;
    private String subServiceName;

    // Constructors
    public BookingDto() {}

    public BookingDto(Long id, String address, String notes, LocalDate scheduledDate, 
                     BookingStatus status, Long contractorId, String contractorName, 
                     String contractorPhoneNo, Long customerId, String customerName, 
                     String customerEmail, Long serviceId, String serviceName, 
                     Long subServiceId, String subServiceName) {
        this.id = id;
        this.address = address;
        this.notes = notes;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.contractorId = contractorId;
        this.contractorName = contractorName;
        this.contractorPhoneNo = contractorPhoneNo;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.subServiceId = subServiceId;
        this.subServiceName = subServiceName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getContractorPhoneNo() {
        return contractorPhoneNo;
    }

    public void setContractorPhoneNo(String contractorPhoneNo) {
        this.contractorPhoneNo = contractorPhoneNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getSubServiceId() {
        return subServiceId;
    }

    public void setSubServiceId(Long subServiceId) {
        this.subServiceId = subServiceId;
    }

    public String getSubServiceName() {
        return subServiceName;
    }

    public void setSubServiceName(String subServiceName) {
        this.subServiceName = subServiceName;
    }
}

