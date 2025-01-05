package com.bkmarriott.hotel.presentation.rest.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record HotelSearchRequest(
        @NotBlank
        String name,
        String city,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate
){
}
