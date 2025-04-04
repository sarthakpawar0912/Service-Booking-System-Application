package com.ServiceBookingSystem.REPOSITORY;


import com.ServiceBookingSystem.ENTITY.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByCompanyId(Long CompanyId);
    List<Reservation> findAllByUserId(Long userId);
}
