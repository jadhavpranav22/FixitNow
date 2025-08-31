package com.household_repair.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.household_repair.entity.Customer;
import com.household_repair.entity.Users;
import com.household_repair.repo.CustomerRepo;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepoRef;

    // Register customer linked with user - minimal info (only name)
    @Transactional
    public Customer registerCustomerWithUser(Users user, String customerName) {
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setCustomerName(customerName);
        return customerRepoRef.save(customer);
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepoRef.findAll();
    }

    // Get one customer by ID
    public Customer getCustomerById(Long customerId) {
        return customerRepoRef.findById(customerId).orElse(null);
    }

    // Add new customer
    public Customer addCustomer(Customer newCustomer) {
        return customerRepoRef.save(newCustomer);
    }

    // Get customer(s) by partial or full name (case-insensitive)
    public List<Customer> getCustomersByName(String customerName) {
        return customerRepoRef.findAll().stream()
            .filter(customer -> customer.getCustomerName() != null &&
                     customer.getCustomerName().toLowerCase().contains(customerName.toLowerCase()))
            .collect(Collectors.toList());
    }

    // Update customer info
    public Customer updateCustomer(Customer updatedCustomer) {
        return customerRepoRef.save(updatedCustomer);
    }

    // Delete customer by ID
    public void deleteCustomer(Long customerId) {
        customerRepoRef.deleteById(customerId);
    }
}
