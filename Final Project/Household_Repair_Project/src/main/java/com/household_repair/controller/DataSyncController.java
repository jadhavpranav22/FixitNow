package com.household_repair.controller;

import com.household_repair.service.DataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync")
@CrossOrigin(origins = "http://localhost:3000")
public class DataSyncController {

    @Autowired
    private DataSyncService dataSyncService;

    /**
     * Sync all users to ensure they have corresponding records in customer_master or contractor_master
     */
    @PostMapping("/all-users")
    public ResponseEntity<String> syncAllUsers() {
        try {
            dataSyncService.syncAllUsers();
            return ResponseEntity.ok("All users synced successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error syncing users: " + e.getMessage());
        }
    }

    /**
     * Sync a specific user by ID
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<String> syncUser(@PathVariable Long userId) {
        try {
            dataSyncService.syncUser(userId);
            return ResponseEntity.ok("User " + userId + " synced successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error syncing user: " + e.getMessage());
        }
    }

    /**
     * Get sync status
     */
    @GetMapping("/status")
    public ResponseEntity<String> getSyncStatus() {
        try {
            dataSyncService.getSyncStatus();
            return ResponseEntity.ok("Sync status checked. Check console for details.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error checking sync status: " + e.getMessage());
        }
    }
}




