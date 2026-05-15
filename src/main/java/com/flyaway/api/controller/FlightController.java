package com.flyaway.api.controller;

import com.flyaway.api.dto.*;
import com.flyaway.api.entity.User;
import com.flyaway.api.repository.UserRepository;
import com.flyaway.api.service.BookingService;
import com.flyaway.api.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<NewIdDTO> create(@Valid @RequestBody NewFlightRequestDTO dto) {
        return ResponseEntity.status(201).body(flightService.create(dto));
    }

    @PostMapping("/create-many")
    public ResponseEntity<NewFlightManyResponseDTO> createMany(@RequestBody NewFlightManyRequestDTO dto) {
        return ResponseEntity.status(201).body(flightService.createMany(dto));
    }

    @GetMapping("/search")
    public ResponseEntity<FlightSearchResponseDTO> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false) String estDepartureTimeFrom,
            @RequestParam(required = false) String estDepartureTimeTo) {
        return ResponseEntity.ok(flightService.search(flightNumber, airlineName,
                estDepartureTimeFrom, estDepartureTimeTo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.findById(id));
    }

    @PostMapping("/book")
    public ResponseEntity<NewIdDTO> book(@Valid @RequestBody FlightBookRequestDTO dto,
                                         Authentication authentication) {
        String email = authentication.getName();
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(bookingService.book(dto, customer));
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }
}