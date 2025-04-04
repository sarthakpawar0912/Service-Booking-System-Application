package com.ServiceBookingSystem.SERVICES.CLIENT;

import com.ServiceBookingSystem.DTO.AdDTO;
import com.ServiceBookingSystem.DTO.AdDetailsForClientDTO;
import com.ServiceBookingSystem.DTO.ReservationDTO;
import com.ServiceBookingSystem.DTO.ReviewDTO;
import com.ServiceBookingSystem.ENTITY.Ad;
import com.ServiceBookingSystem.ENTITY.Reservation;
import com.ServiceBookingSystem.ENTITY.Review;
import com.ServiceBookingSystem.ENTITY.User;
import com.ServiceBookingSystem.ENUMS.ReservationStatus;
import com.ServiceBookingSystem.ENUMS.ReviewStatus;
import com.ServiceBookingSystem.REPOSITORY.AdRepository;
import com.ServiceBookingSystem.REPOSITORY.ReservationRepository;
import com.ServiceBookingSystem.REPOSITORY.ReviewRepository;
import com.ServiceBookingSystem.REPOSITORY.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements  ClientService{

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public List<AdDTO> getAllAds(){
        return adRepository.findAll().stream().map(Ad::getAdDto).collect(Collectors.toList());
    }

    public List<AdDTO> searchAdByName(String name) {
        return adRepository.findByServiceNameContainingIgnoreCase(name)
                .stream()
                .map(Ad::getAdDto)
                .collect(Collectors.toList());
    }

    public boolean bookService(ReservationDTO reservationDTO) {
        Optional<Ad> optionalAd = adRepository.findById(reservationDTO.getAdId());
        Optional<User> optionalUser = userRepository.findById(reservationDTO.getUserId());

        if (optionalAd.isPresent() && optionalUser.isPresent()) {
            Reservation reservation = new Reservation();
            reservation.setBookDate(reservationDTO.getBookDate());
            reservation.setReservationStatus(ReservationStatus.PENDING);
            reservation.setUser(optionalUser.get());
            reservation.setAd(optionalAd.get());
            reservation.setCompany(optionalAd.get().getUser());
            reservation.setReviewStatus(ReviewStatus.FALSE);

            reservationRepository.save(reservation);
            return true;
        }
        return false;

    }
    public AdDetailsForClientDTO getAdDetailsByAdId(Long adId) {
        Optional<Ad> optionalAd = adRepository.findById(adId);
        if (!optionalAd.isPresent()) {
            throw new RuntimeException("Ad not found with ID: " + adId); // Better error handling
        }

        AdDetailsForClientDTO adDetailsForClientDTO = new AdDetailsForClientDTO();
        adDetailsForClientDTO.setAdDTO(optionalAd.get().getAdDto());

        List<Review> reviewList = reviewRepository.findAllByAdId(adId);
        adDetailsForClientDTO.setReviewDTOList(reviewList.stream().map(Review::getDto).collect(Collectors.toList()));

        return adDetailsForClientDTO;
    }

    public List<ReservationDTO> getAllBookingsByUserId(Long userId){
        return reservationRepository.findAllByUserId(userId)
                .stream().map(Reservation::getReservationDTO)
                .collect(Collectors.toList());

    }

    public Boolean giveReview(ReviewDTO reviewDTO) {
        try {
            Optional<User> optionalUser = userRepository.findById(reviewDTO.getUserId());
            Optional<Reservation> optionalBooking = reservationRepository.findById(reviewDTO.getBookId());

            if (optionalUser.isPresent() && optionalBooking.isPresent()) {
                Reservation booking = optionalBooking.get();

                // Check if review already exists for this booking
                if (booking.getReviewStatus() == ReviewStatus.TRUE) {
                    throw new IllegalStateException("Review already submitted for this booking");
                }

                Review review = new Review();
                review.setReviewDate(new Date());
                review.setReview(reviewDTO.getReview());
                review.setRating(reviewDTO.getRating());
                review.setUser(optionalUser.get());
                review.setAd(booking.getAd());

                reviewRepository.save(review);

                booking.setReviewStatus(ReviewStatus.TRUE);
                reservationRepository.save(booking);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to submit review: " + e.getMessage());
        }
    }
}
