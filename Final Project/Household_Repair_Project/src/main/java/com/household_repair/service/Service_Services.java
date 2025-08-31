package com.household_repair.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.household_repair.entity.Services;
import com.household_repair.repo.ServiceRepo;


@Service
public class Service_Services {

    @Autowired
    private ServiceRepo serviceRepoRef;

    // Get all services
    public List<Services> getAllServices() {
        return serviceRepoRef.findAll();
    }

    // Get service by ID
    public Services getServiceById(Long id) {
        Optional<Services> optional = serviceRepoRef.findById(id);
        return optional.orElse(null);
    }

    // Add new service
    public Services addService(Services newService) {
        return serviceRepoRef.save(newService);
    }

    // Update existing service
    public Services updateService(Long id, Services updatedService) {
        updatedService.setServicesid(id);
        return serviceRepoRef.save(updatedService);
    }

    // Delete service by ID
    public void deleteService(Long id) {
        serviceRepoRef.deleteById(id);
    }
}

