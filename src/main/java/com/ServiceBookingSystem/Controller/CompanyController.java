package com.ServiceBookingSystem.Controller;

import com.ServiceBookingSystem.DTO.AdDTO;
import com.ServiceBookingSystem.DTO.ReservationDTO;
import com.ServiceBookingSystem.ENTITY.User;
import com.ServiceBookingSystem.REPOSITORY.UserRepository;
import com.ServiceBookingSystem.SERVICES.Company.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/company")
@CrossOrigin("*")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(value = "/ad/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<?> postAd(@PathVariable Long userId, @ModelAttribute AdDTO adDTO) throws IOException {
        boolean success = companyService.postAd(userId, adDTO);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/ads/{userId}")
    public ResponseEntity<?> getAllAdsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(companyService.getAllAds(userId));
    }

    @GetMapping("/ad/{adId}")
    public ResponseEntity<?> getAdById(@PathVariable Long adId){
        AdDTO adDTO =companyService.getAdById(adId);
        if(adDTO != null) {
            return ResponseEntity.ok(adDTO);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PutMapping("/ad/{adId}")
    public ResponseEntity<?>updateAd(@PathVariable Long adId, @ModelAttribute AdDTO adDTO) throws IOException{
        boolean SUCCESS=companyService.updateAd(adId, adDTO);
        if(SUCCESS) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return null;
    }

    @DeleteMapping("/ad/{adId}")
    public ResponseEntity<?> deleteAd(@PathVariable Long adId){
        boolean SUCCESS=companyService.deleteAd(adId);
        if(SUCCESS) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/bookings/{companyId}")
    public ResponseEntity<List<ReservationDTO>> getAllAdBookings(@PathVariable Long companyId) {
        try {
            List<ReservationDTO> bookings = companyService.getAllAdBookings(companyId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bookings/{bookingId}/{status}")
    public ResponseEntity<?> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status) {
        boolean success = companyService.changeBookingStatus(bookingId, status);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
