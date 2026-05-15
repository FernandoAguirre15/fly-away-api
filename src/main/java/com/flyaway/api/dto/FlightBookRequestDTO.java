package com.flyaway.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightBookRequestDTO {
    @NotNull
    private Long flightId;
}