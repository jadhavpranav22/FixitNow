package com.household_repair.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
@Table(name = "services_master")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long servicesid;

    @Column(nullable = false, unique = true)
    private String servicesname;

    @Column(length = 1000)
    private String servicesdescription;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("service-subservices")
    private List<SubServices> subServices;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @JsonManagedReference("service-bookings")
    private List<Booking> bookings;

    public Long getServicesid() {
        return servicesid;
    }

    public void setServicesid(Long servicesid) {
        this.servicesid = servicesid;
    }

    public String getServicesname() {
        return servicesname;
    }

    public void setServicesname(String servicesname) {
        this.servicesname = servicesname;
    }

    public String getServicesdescription() {
        return servicesdescription;
    }

    public void setServicesdescription(String servicesdescription) {
        this.servicesdescription = servicesdescription;
    }

    public List<SubServices> getSubServices() {
        return subServices;
    }

    public void setSubServices(List<SubServices> subServices) {
        this.subServices = subServices;
    }
}
