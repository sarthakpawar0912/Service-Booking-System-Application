package com.ServiceBookingSystem.DTO;


import lombok.Data;

import java.util.Date;
@Data
public class ReviewDTO {
    private Long id;
    private String review;
    private Date reviewDate;
    private Long rating;
    private Long userId;
    private Long adId;
    private String clientName;
    private String serviceName;
    private Long bookId;  // Make sure this is properly included
}
