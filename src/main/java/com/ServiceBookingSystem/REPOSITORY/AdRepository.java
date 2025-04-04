package com.ServiceBookingSystem.REPOSITORY;

import com.ServiceBookingSystem.ENTITY.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    List<Ad> findAllByUserId(Long userId);

    List<Ad> findByServiceNameContainingIgnoreCase(String serviceName);

}
