package com.household_repair.service;

import com.household_repair.entity.Customer;
import com.household_repair.entity.Contractor;
import com.household_repair.entity.Users;
import com.household_repair.repo.CustomerRepo;
import com.household_repair.repo.ContractorRepo;
import com.household_repair.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataSyncService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ContractorRepo contractorRepo;

    /**
     * Sync all users to ensure they have corresponding records in customer_master or contractor_master
     */
    @Transactional
    public void syncAllUsers() {
        List<Users> allUsers = userRepository.findAll();
        
        for (Users user : allUsers) {
            String role = user.getRole();
            
            if (role != null) {
                if (role.equals("ROLE_CUSTOMER") || role.equals("CUSTOMER")) {
                    // Check if customer record exists
                    if (!customerRepo.existsById(user.getUserId())) {
                        Customer customer = new Customer();
                        customer.setUser(user);
                        customer.setCustomerName(user.getUsername());
                        customerRepo.save(customer);
                        System.out.println("Created customer record for user: " + user.getEmail());
                    }
                } else if (role.equals("ROLE_CONTRACTOR") || role.equals("CONTRACTOR")) {
                    // Check if contractor record exists
                    if (!contractorRepo.existsById(user.getUserId())) {
                        Contractor contractor = new Contractor();
                        contractor.setUser(user);
                        contractor.setContractorName(user.getUsername());
                        contractor.setStatus("ACTIVE");
                        contractorRepo.save(contractor);
                        System.out.println("Created contractor record for user: " + user.getEmail());
                    }
                }
            }
        }
    }

    /**
     * Sync a specific user
     */
    @Transactional
    public void syncUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        String role = user.getRole();
        
        if (role != null) {
            if (role.equals("ROLE_CUSTOMER") || role.equals("CUSTOMER")) {
                if (!customerRepo.existsById(userId)) {
                    Customer customer = new Customer();
                    customer.setUser(user);
                    customer.setCustomerName(user.getUsername());
                    customerRepo.save(customer);
                    System.out.println("Created customer record for user: " + user.getEmail());
                }
            } else if (role.equals("ROLE_CONTRACTOR") || role.equals("CONTRACTOR")) {
                if (!contractorRepo.existsById(userId)) {
                    Contractor contractor = new Contractor();
                    contractor.setUser(user);
                    contractor.setContractorName(user.getUsername());
                    contractor.setStatus("ACTIVE");
                    contractorRepo.save(contractor);
                    System.out.println("Created contractor record for user: " + user.getEmail());
                }
            }
        }
    }

    /**
     * Get sync status - shows which users need to be synced
     */
    public void getSyncStatus() {
        List<Users> allUsers = userRepository.findAll();
        
        System.out.println("=== SYNC STATUS REPORT ===");
        System.out.println("Total users: " + allUsers.size());
        
        for (Users user : allUsers) {
            String role = user.getRole();
            boolean hasCustomerRecord = customerRepo.existsById(user.getUserId());
            boolean hasContractorRecord = contractorRepo.existsById(user.getUserId());
            
            System.out.println("User ID: " + user.getUserId() + 
                             ", Email: " + user.getEmail() + 
                             ", Role: " + role + 
                             ", Customer Record: " + hasCustomerRecord + 
                             ", Contractor Record: " + hasContractorRecord);
        }
    }
}




