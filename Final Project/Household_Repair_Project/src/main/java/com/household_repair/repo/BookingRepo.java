package com.household_repair.repo;

import com.household_repair.entity.Booking;
import com.household_repair.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Long> {

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByCustomerCustomerId(Long customerId);

    List<Booking> findByContractorContractorId(Long contractorId);

    List<Booking> findByContractorContractorIdAndStatus(Long contractorId, BookingStatus status);
}
