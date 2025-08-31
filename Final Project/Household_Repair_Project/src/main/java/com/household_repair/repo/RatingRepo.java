package com.household_repair.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.household_repair.entity.Rating;



@Repository
public interface RatingRepo extends JpaRepository<Rating, Long> {
}