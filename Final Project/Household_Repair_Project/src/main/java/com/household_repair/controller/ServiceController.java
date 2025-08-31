package com.household_repair.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.household_repair.entity.Services;
import com.household_repair.service.Service_Services;



@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private Service_Services serviceService;

    // ✅ Get all services
    @GetMapping
    public List<Services> getAllServices() {
        return serviceService.getAllServices();
    }

    // ✅ Get service by ID
    @GetMapping("/{id}")
    public Services getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id);
    }

    // ✅ Add new service
    @PostMapping
    public Services createService(@RequestBody Services service) {
        return serviceService.addService(service);
    }

    // ✅ Update service
    @PutMapping("/{id}")
    public Services updateService(@PathVariable Long id, @RequestBody Services updatedService) {
        return serviceService.updateService(id, updatedService);
    }

    // ✅ Delete service
    @DeleteMapping("/{id}")
    public String deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return "Service with ID " + id + " deleted successfully.";
    }
}

