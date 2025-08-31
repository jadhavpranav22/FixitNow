package com.household_repair.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.household_repair.entity.Admin;



@Repository
public interface AdminRepo extends JpaRepository<Admin, Long> {
}