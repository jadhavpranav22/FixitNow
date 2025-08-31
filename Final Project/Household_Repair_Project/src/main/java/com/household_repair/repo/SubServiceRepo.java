package com.household_repair.repo;



import com.household_repair.entity.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubServiceRepo extends JpaRepository<SubServices, Long> {

    // Find all subservices under a specific service
	List<SubServices> findByService_Servicesid(Long servicesid);


}

