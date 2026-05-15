package com.flyaway.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NewFlightRequestDTO {
    @NotBlank
    private String airlineName;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2,3}[0-9]{3}$", message = "Invalid flight number format")
    private String flightNumber;

    @NotNull
    private LocalDateTime estDepartureTime;

    @NotNull
    private LocalDateTime estArrivalTime;

    @NotNull
    @Min(1)
    private Integer availableSeats;
}