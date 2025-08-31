package com.household_repair.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.household_repair.entity.Contractor;
import com.household_repair.entity.Customer;
import com.household_repair.entity.Users;



public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmailIgnoreCase(String email);

    List<Users> findByResetToken(String resetToken);
}

