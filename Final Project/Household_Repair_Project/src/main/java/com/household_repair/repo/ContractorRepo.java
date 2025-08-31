package com.household_repair.repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.household_repair.entity.Contractor;



@Repository
public interface ContractorRepo extends JpaRepository<Contractor, Long> {
}




