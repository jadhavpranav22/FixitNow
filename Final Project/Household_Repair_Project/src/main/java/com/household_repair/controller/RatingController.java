package com.household_repair.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.household_repair.entity.Rating;
import com.household_repair.service.RatingService;



@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    // ✅ Add a new rating
    @PostMapping("/add")
    public ResponseEntity<?> addRating(
            @RequestBody Rating rating,
            @RequestParam("customerId") Long customerId,
            @RequestParam("contractorId") Long contractorId,
            @RequestParam("bookingId") Long bookingId) {
        try {
            // Validate rating data
            if (rating.getStars() < 1 || rating.getStars() > 5) {
                return ResponseEntity.badRequest().body("Stars must be between 1 and 5");
            }
            
            Rating savedRating = ratingService.addRating(rating, customerId, contractorId, bookingId);
            return ResponseEntity.ok(savedRating);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Get all ratings
    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingService.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    // ✅ Get a single rating by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRatingById(@PathVariable Long id) {
        try {
            Rating rating = ratingService.getRatingById(id);
            return ResponseEntity.ok(rating);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Update an existing rating
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRating(@PathVariable Long id, @RequestBody Rating updatedRating) {
        try {
            Rating rating = ratingService.updateRating(id, updatedRating);
            return ResponseEntity.ok(rating);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Delete a rating
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {
        try {
            ratingService.deleteRating(id);
            return ResponseEntity.ok("Rating with ID " + id + " deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
