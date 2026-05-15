package com.flyaway.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingResponseDTO {
    public String id;
    public LocalDateTime bookingDate;
    public String flightId;
    public String flightNumber;
    public String customerId;
    public String customerFirstName;
    public String customerLastName;
    public String estDepartureTime;
    public String estArrivalTime;
}