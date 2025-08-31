package com.household_repair.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")  // Matches the actual DB column name
    private Long id;

    private String address;
    private String notes;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne
    @JoinColumn(name = "contractor_id")
    @JsonBackReference("contractor-bookings") // marks this as the "child" side
    private Contractor contractor;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference("user-customer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonBackReference("service-bookings")
    private Services service;

    @ManyToOne
    @JoinColumn(name = "subservice_id")
    @JsonBackReference("service-subservices")
    private SubServices subservice;

    // Getters & Setters

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

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }

    public SubServices getSubservice() {
        return subservice;
    }

    public void setSubservice(SubServices subservice) {
        this.subservice = subservice;
    }
}
