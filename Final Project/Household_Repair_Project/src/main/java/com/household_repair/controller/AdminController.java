package com.household_repair.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.household_repair.entity.Admin;
import com.household_repair.repo.AdminRepo;



@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = "*")  // You can restrict this to a domain if needed
public class AdminController {

    @Autowired
    private AdminRepo adminRepo;

    // ✅ Get all admins
    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminRepo.findAll();
    }

    // ✅ Get admin by ID
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        return adminRepo.findById(id)
                .map(admin -> ResponseEntity.ok(admin))
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Create new admin
    @PostMapping
    public Admin createAdmin(@RequestBody Admin admin) {
        return adminRepo.save(admin);
    }

    // ✅ Update existing admin
    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin updatedAdmin) {
        Optional<Admin> optionalAdmin = adminRepo.findById(id);

        if (optionalAdmin.isPresent()) {
            Admin existingAdmin = optionalAdmin.get();

            // Only update if non-null (you can also remove these checks if frontend sends full object)
            if (updatedAdmin.getAdminUsername() != null) {
                existingAdmin.setAdminUsername(updatedAdmin.getAdminUsername());
            }

            if (updatedAdmin.getAdminPassword() != null) {
                existingAdmin.setAdminPassword(updatedAdmin.getAdminPassword());
            }

            Admin savedAdmin = adminRepo.save(existingAdmin);
            return ResponseEntity.ok(savedAdmin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // ✅ Delete admin by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        if (!adminRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        adminRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
