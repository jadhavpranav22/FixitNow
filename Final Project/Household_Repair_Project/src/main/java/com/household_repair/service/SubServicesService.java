package com.household_repair.service;



import com.household_repair.entity.SubServices;
import com.household_repair.repo.SubServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubServicesService {

    @Autowired
    private SubServiceRepo subServicesRepository;


    public List<SubServices> getAllSubServices() {
        return subServicesRepository.findAll();
    }

    // Get subservices by service ID
    public List<SubServices> getSubServicesByServiceId(Long serviceId) {
        return subServicesRepository.findByService_Servicesid(serviceId);
    }

    // Get subservice by ID
    public SubServices getSubServiceById(Long id) {
        return subServicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SubService not found with id: " + id));
    }

    // Create a new subservice
    public SubServices createSubService(SubServices subService) {
        return subServicesRepository.save(subService);
    }

    // Update an existing subservice
    public SubServices updateSubService(Long id, SubServices subServiceDetails) {
        SubServices subService = getSubServiceById(id);

        subService.setSubservicename(subServiceDetails.getSubservicename());
        subService.setSubservicedescription(subServiceDetails.getSubservicedescription());
        subService.setService(subServiceDetails.getService());

        return subServicesRepository.save(subService);
    }

    // Delete subservice
    public void deleteSubService(Long id) {
        subServicesRepository.deleteById(id);
    }
}
