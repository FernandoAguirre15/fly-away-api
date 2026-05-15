package com.flyaway.api.service;

import com.flyaway.api.dto.BookingResponseDTO;
import com.flyaway.api.dto.FlightBookRequestDTO;
import com.flyaway.api.dto.NewIdDTO;
import com.flyaway.api.entity.Booking;
import com.flyaway.api.entity.Flight;
import com.flyaway.api.entity.User;
import com.flyaway.api.event.BookingEvent;
import com.flyaway.api.repository.BookingRepository;
import com.flyaway.api.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public NewIdDTO book(FlightBookRequestDTO dto, User customer) {
        Flight flight = flightRepository.findById(dto.getFlightId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));

        if (flight.getAvailableSeats() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No seats available");
        }


        if (flight.getEstDepartureTime().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight is in the past or in transit");
        }

        boolean hasOverlap = bookingRepository.findByCustomer(customer).stream().anyMatch(b ->
                b.getFlight().getEstDepartureTime().isBefore(flight.getEstArrivalTime()) &&
                        b.getFlight().getEstArrivalTime().isAfter(flight.getEstDepartureTime())
        );
        if (hasOverlap) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Flight overlaps with another booking");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);


        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setCustomer(customer);
        booking.setBookingDate(LocalDateTime.now());
        Booking saved = bookingRepository.save(booking);

        eventPublisher.publishEvent(new BookingEvent(this, saved));

        return new NewIdDTO(saved.getId().toString());
    }

    public BookingResponseDTO findById(Long id) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        return toDTO(b);
    }

    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    private BookingResponseDTO toDTO(Booking b) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.id = b.getId().toString();
        dto.bookingDate = b.getBookingDate();
        dto.flightId = b.getFlight().getId().toString();
        dto.flightNumber = b.getFlight().getFlightNumber();
        dto.customerId = b.getCustomer().getId().toString();
        dto.customerFirstName = b.getCustomer().getFirstName();
        dto.customerLastName = b.getCustomer().getLastName();
        dto.estDepartureTime = b.getFlight().getEstDepartureTime().toString();
        dto.estArrivalTime = b.getFlight().getEstArrivalTime().toString();
        return dto;
    }
}