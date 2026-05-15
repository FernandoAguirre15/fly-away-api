package com.flyaway.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FlightResponseDTO {
    public String id;
    public String airlineName;
    public String flightNumber;
    public LocalDateTime estDepartureTime;
    public LocalDateTime estArrivalTime;
    public Integer availableSeats;
}