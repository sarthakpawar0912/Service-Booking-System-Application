package com.ServiceBookingSystem.Controller;

import com.ServiceBookingSystem.DTO.ReservationDTO;
import com.ServiceBookingSystem.DTO.ReviewDTO;
import com.ServiceBookingSystem.SERVICES.CLIENT.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/client")
@CrossOrigin("*")
public class ClientController {

    @Autowired(required = true)
    private ClientService clientService;

    @GetMapping("/ads")
    public ResponseEntity<?> getAllAds()
    {
        return ResponseEntity.ok(clientService.getAllAds());
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> searchAdByService(@PathVariable String name){
        return ResponseEntity.ok(clientService.searchAdByName(name));
    }

    @PostMapping("/book-service")
    public ResponseEntity<?> bookService(@RequestBody ReservationDTO reservationDTO) {
        boolean success = clientService.bookService(reservationDTO);
        if (success) {
            return ResponseEntity.ok(Map.of("message", "Booking successful")); // Returns JSON
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Ad or User not found"));
        }
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<?> getAdDetailsByAdId(@PathVariable("adId") Long adId) {
        try {
            return ResponseEntity.ok(clientService.getAdDetailsByAdId(adId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/my-bookings/{userId}")
    public ResponseEntity<?> getAllBookingsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(clientService.getAllBookingsByUserId(userId));
    }

    @PostMapping("/review")
    public ResponseEntity<?> giveReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            Boolean success = clientService.giveReview(reviewDTO);
            if (success) {
                return ResponseEntity.ok().body(Map.of(
                        "success", true,
                        "message", "Review submitted successfully"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "success", false,
                        "message", "User or booking not found"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

}
