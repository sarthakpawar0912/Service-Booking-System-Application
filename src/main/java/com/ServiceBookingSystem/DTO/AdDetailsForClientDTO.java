package com.ServiceBookingSystem.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AdDetailsForClientDTO {


    private AdDTO adDTO;

    private List<ReviewDTO> reviewDTOList;

    //private List<ReviewDTO> reviewDTOList;
}
