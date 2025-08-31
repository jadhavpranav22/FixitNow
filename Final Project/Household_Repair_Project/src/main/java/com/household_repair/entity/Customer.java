package com.household_repair.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "Customer_Master")
public class Customer {

    @Id
    @Column(name = "customer_id")
    private Long customerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id")
    @JsonBackReference("user-customer")
    private Users user;

    @Column(name = "customer_name", length = 30)
    private String customerName;

    @Column(name = "customer_phone_no")
    private String customerPhoneNo;

    @Column(name = "customer_address", length = 100)
    private String customerAddress;

    @Version
    @Column(name = "version")
    private Long version;

    // Getters and setters

    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }
    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Long getVersion() {
        return version;
    }
    public void setVersion(Long version) {
        this.version = version;
    }
}
