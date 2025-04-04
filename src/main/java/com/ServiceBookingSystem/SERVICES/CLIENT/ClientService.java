package com.ServiceBookingSystem.SERVICES.CLIENT;

import com.ServiceBookingSystem.DTO.AdDTO;
import com.ServiceBookingSystem.DTO.AdDetailsForClientDTO;
import com.ServiceBookingSystem.DTO.ReservationDTO;
import com.ServiceBookingSystem.DTO.ReviewDTO;

import java.util.List;

public interface ClientService {

    List<AdDTO> getAllAds();

    List<AdDTO> searchAdByName(String name);
    boolean bookService(ReservationDTO reservationDTO);
    AdDetailsForClientDTO getAdDetailsByAdId(Long adId);

    List<ReservationDTO> getAllBookingsByUserId(Long userId);

    Boolean giveReview(ReviewDTO reviewDTO);
}
