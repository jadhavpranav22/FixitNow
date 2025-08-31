package com.household_repair.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.household_repair.entity.Services;



@Repository
public interface ServiceRepo extends JpaRepository<Services, Long> {
}