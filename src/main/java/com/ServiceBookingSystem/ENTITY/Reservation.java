package com.ServiceBookingSystem.ENTITY;


import com.ServiceBookingSystem.DTO.ReservationDTO;
import com.ServiceBookingSystem.ENUMS.ReservationStatus;
import com.ServiceBookingSystem.ENUMS.ReviewStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    private Date bookDate;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="user_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name = "company_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User company;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="ad_id",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ad ad;

    public ReservationDTO getReservationDTO() {
        ReservationDTO dto= new ReservationDTO();

        dto.setId(id);
        dto.setServiceName(ad.getServiceName());
        dto.setBookDate(bookDate);
        dto.setReservationStatus(reservationStatus);
        dto.setReviewStatus(reviewStatus);
        dto.setAdId(ad.getId());
        dto.setCompanyId(company.getId());
        dto.setUserName(user.getName());

        return dto;
    }

}
