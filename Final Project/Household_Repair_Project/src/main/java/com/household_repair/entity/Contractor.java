package com.household_repair.entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "Contractor_Master")
public class Contractor {

    @Id
    @Column(name = "contractor_id")
    private Long contractorId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "contractor_id")
    @JsonBackReference("user-contractor") // marks this as the "child" side
    private Users user;

    @OneToMany(mappedBy = "contractor")
    @JsonManagedReference("contractor-bookings")
    private List<Booking> bookings;

    @Column(name = "contractor_name", length = 30)
    private String contractorName;

    @Column(name = "contractor_phone_no")
    private String contractorPhoneNo;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "status")
    private String status;

    @Version
    @Column(name = "version")
    private Long version;

    // Getters and setters

    public Long getContractorId() {
        return contractorId;
    }
    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
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

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }
    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> list) {
        this.bookings = list;
    }
}
