package com.household_repair.dto;

import java.time.LocalDate;
import java.util.List;

public class BookingMultipleRequestDto {
    private String address;
    private LocalDate scheduledDate;
    private String notes;
    private Long customerId;
    private List<CartItem> cartItems;

    public BookingMultipleRequestDto() {}

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }

    public static class CartItem {
        private Long serviceId;
        private Long subServiceId; // optional if you have subservices
        private Long contractorId; // optional

        public CartItem() {}

        public Long getServiceId() { return serviceId; }
        public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

        public Long getSubServiceId() { return subServiceId; }
        public void setSubServiceId(Long subServiceId) { this.subServiceId = subServiceId; }

        public Long getContractorId() { return contractorId; }
        public void setContractorId(Long contractorId) { this.contractorId = contractorId; }
    }
}
