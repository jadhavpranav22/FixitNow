package com.household_repair.service;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.household_repair.entity.Admin;
import com.household_repair.repo.AdminRepo;



@Service
public class AdminService {

	@Autowired
	 private AdminRepo adminRepoRef;
	 
	// Get all admin
	    public List<Admin> getAllAdmin() {
	        return adminRepoRef.findAll();
	    }

	    // Get one admin by ID
	    public Admin getOneAdmin(Long adminId) {
	        return adminRepoRef.findById(adminId).orElse(null);
	    }

//	    // Add new admin
//	    public Admin addAdmin(Admin newAdmin) {
//	        return adminRepoRef.save(newAdmin);
//	    }

	    // Get admin(s) by partial or full name (case-insensitive)
//	    public List<Admin> getAdminByName(String adminName) {
//	        return adminRepoRef.findAll().stream()
//	            .filter(admin -> admin.getAdminUsername() != null &&
//	                              admin.getAdminUsername().toLowerCase().contains(adminName.toLowerCase()))
//	            .collect(Collectors.toList());
//	    }
	    
	    
	    // Get update
	    public void updateAdmin(Admin modifiedAdmin) {
	    	adminRepoRef.save(modifiedAdmin);
		}
	    
	    // Get delete		
		public void deleteAdmin(Long adminId) {
			
			adminRepoRef.deleteById(adminId);
		} 
	 
	 
}

