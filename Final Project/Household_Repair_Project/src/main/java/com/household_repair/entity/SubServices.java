package com.household_repair.entity;



import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "subservices_master")
public class SubServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subserviceid;

    @Column(nullable = false)
    private String subservicename;

    @Column(length = 1000)
    private String subservicedescription;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    @JsonBackReference("service-subservices")
    private Services service;

    public Long getSubserviceid() {
        return subserviceid;
    }

    public void setSubserviceid(Long subserviceid) {
        this.subserviceid = subserviceid;
    }

    public String getSubservicename() {
        return subservicename;
    }

    public void setSubservicename(String subservicename) {
        this.subservicename = subservicename;
    }

    public String getSubservicedescription() {
        return subservicedescription;
    }

    public void setSubservicedescription(String subservicedescription) {
        this.subservicedescription = subservicedescription;
    }

    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }
}

