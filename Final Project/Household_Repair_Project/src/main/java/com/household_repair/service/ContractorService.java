package com.household_repair.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.household_repair.entity.Contractor;
import com.household_repair.entity.Customer;
import com.household_repair.entity.Users;
import com.household_repair.repo.ContractorRepo;
import com.household_repair.repo.CustomerRepo;
import com.household_repair.repo.UserRepository;

@Service
public class ContractorService {

    @Autowired
    private ContractorRepo contractorRepoRef;

    @Autowired
    private UserRepository userRepository;

    // Registration method to save user + contractor together
    @Transactional
    public Contractor registerContractorWithUser(Users user, String contractorName) {
        Contractor contractor = new Contractor();
        contractor.setContractorId(user.getUserId()); // Set the ID to match the user ID
        contractor.setUser(user);
        contractor.setContractorName(contractorName);
        return contractorRepoRef.save(contractor);
    }
    // Get all contractors
    public List<Contractor> getAllContractor() {
        return contractorRepoRef.findAll();
    }

    // Get a single contractor by ID
    public Contractor getOneContractor(Long contractorId) {
        return contractorRepoRef.findById(contractorId).orElse(null);
    }

    // Add a new contractor
    public Contractor addContractor(Contractor newContractor) {
        return contractorRepoRef.save(newContractor);
    }

    // Search contractor(s) by name (case-insensitive)
    public List<Contractor> getContractorByName(String contractorName) {
        return contractorRepoRef.findAll().stream()
            .filter(contractor -> contractor.getContractorName() != null &&
                                  contractor.getContractorName().toLowerCase().contains(contractorName.toLowerCase()))
            .collect(Collectors.toList());
    }

    // Update contractor
    public Contractor updateContractor(Contractor modifiedContractor) {
        return contractorRepoRef.save(modifiedContractor);
    }

    // Delete contractor by ID
    public void deleteContractor(Long contractorId) {
        contractorRepoRef.deleteById(contractorId);
    }
}
