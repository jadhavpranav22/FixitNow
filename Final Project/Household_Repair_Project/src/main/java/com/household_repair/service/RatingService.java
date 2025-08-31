package com.household_repair.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.household_repair.entity.Booking;
import com.household_repair.entity.Contractor;
import com.household_repair.entity.Customer;
import com.household_repair.entity.Rating;
import com.household_repair.repo.BookingRepo;
import com.household_repair.repo.ContractorRepo;
import com.household_repair.repo.CustomerRepo;
import com.household_repair.repo.RatingRepo;





@Service
public class RatingService {

    @Autowired
    private RatingRepo ratingRepoRef;

    @Autowired
    private CustomerRepo customerRepoRef;

    @Autowired
    private ContractorRepo contractorRepoRef;

    @Autowired
    private BookingRepo bookingRepoRef;

    // ✅ Add a new rating
    public Rating addRating(Rating rating, Long customerId, Long contractorId, Long bookingId) {
        // Validate customer exists
        Optional<Customer> customerOpt = customerRepoRef.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }

        // Validate contractor exists
        Optional<Contractor> contractorOpt = contractorRepoRef.findById(contractorId);
        if (contractorOpt.isEmpty()) {
            throw new RuntimeException("Contractor not found with ID: " + contractorId);
        }

        // Validate booking exists
        Optional<Booking> bookingOpt = bookingRepoRef.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found with ID: " + bookingId);
        }

        // Set the relationships
        rating.setCustomer(customerOpt.get());
        rating.setContractor(contractorOpt.get());

        // Save and return the rating
        return ratingRepoRef.save(rating);
    }

    // ✅ Get all ratings
    public List<Rating> getAllRatings() {
        return ratingRepoRef.findAll();
    }

    // ✅ Get a single rating by ID
    public Rating getRatingById(Long id) {
        return ratingRepoRef.findById(id)
                .orElseThrow(() -> new RuntimeException("Rating not found with ID: " + id));
    }

    // ✅ Update an existing rating
    public Rating updateRating(Long id, Rating updatedRating) {
        Rating existingRating = getRatingById(id);

        // Update stars if provided and valid
        if (updatedRating.getStars() > 0 && updatedRating.getStars() <= 5) {
            existingRating.setStars(updatedRating.getStars());
        }

        // Update comment if provided
        if (updatedRating.getComment() != null && !updatedRating.getComment().trim().isEmpty()) {
            existingRating.setComment(updatedRating.getComment());
        }

        return ratingRepoRef.save(existingRating);
    }

    // ✅ Delete a rating
    public void deleteRating(Long id) {
        if (!ratingRepoRef.existsById(id)) {
            throw new RuntimeException("Rating not found with ID: " + id);
        }
        ratingRepoRef.deleteById(id);
    }
}
