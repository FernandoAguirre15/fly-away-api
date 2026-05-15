package com.flyaway.api.service;

import com.flyaway.api.dto.*;
import com.flyaway.api.entity.Flight;
import com.flyaway.api.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.time.Instant;
import java.time.ZoneOffset;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public NewIdDTO create(NewFlightRequestDTO dto) {
        if (dto.getEstDepartureTime().isAfter(dto.getEstArrivalTime()) ||
                dto.getEstDepartureTime().isEqual(dto.getEstArrivalTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure must be before arrival");
        }
        if (flightRepository.existsByFlightNumber(dto.getFlightNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight number already exists");
        }
        Flight flight = new Flight();
        flight.setAirlineName(dto.getAirlineName());
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setEstDepartureTime(dto.getEstDepartureTime());
        flight.setEstArrivalTime(dto.getEstArrivalTime());
        flight.setAvailableSeats(dto.getAvailableSeats());
        Flight saved = flightRepository.save(flight);
        return new NewIdDTO(saved.getId().toString());
    }

    @Async
    public void createAsync(NewFlightRequestDTO dto) {
        try {
            create(dto);
        } catch (Exception ignored) {}
    }

    public NewFlightManyResponseDTO createMany(NewFlightManyRequestDTO dto) {
        for (NewFlightRequestDTO f : dto.getInputs()) {
            createAsync(f);
        }
        return new NewFlightManyResponseDTO(dto.getInputs().size());
    }

    public FlightSearchResponseDTO search(String flightNumber, String airlineName,
                                          String from, String to) {
        String fn = flightNumber != null ? flightNumber : "";
        String an = airlineName != null ? airlineName : "";

        List<Flight> results;

        if (from != null && to != null) {
            LocalDateTime fromDt = Instant.parse(from).atZone(ZoneOffset.UTC).toLocalDateTime();
            LocalDateTime toDt = Instant.parse(to).atZone(ZoneOffset.UTC).toLocalDateTime();
            results = flightRepository
                    .findByFlightNumberContainingIgnoreCaseAndAirlineNameContainingIgnoreCaseAndEstDepartureTimeGreaterThanEqualAndEstDepartureTimeLessThanEqual(
                            fn, an, fromDt, toDt);
        } else if (from != null) {
            LocalDateTime fromDt = Instant.parse(from).atZone(ZoneOffset.UTC).toLocalDateTime();
            results = flightRepository
                    .findByFlightNumberContainingIgnoreCaseAndAirlineNameContainingIgnoreCaseAndEstDepartureTimeGreaterThanEqual(
                            fn, an, fromDt);
        } else if (to != null) {
            LocalDateTime toDt = Instant.parse(to).atZone(ZoneOffset.UTC).toLocalDateTime();
            results = flightRepository
                    .findByFlightNumberContainingIgnoreCaseAndAirlineNameContainingIgnoreCaseAndEstDepartureTimeLessThanEqual(
                            fn, an, toDt);
        } else {
            results = flightRepository
                    .findByFlightNumberContainingIgnoreCaseAndAirlineNameContainingIgnoreCase(fn, an);
        }

        List<FlightResponseDTO> dtos = results.stream().map(this::toDTO).toList();
        return new FlightSearchResponseDTO(dtos);
    }

    public FlightResponseDTO findById(Long id) {
        Flight f = flightRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));
        return toDTO(f);
    }

    public Flight findEntityById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight not found"));
    }

    public void deleteAll() {
        flightRepository.deleteAll();
    }

    private FlightResponseDTO toDTO(Flight f) {
        FlightResponseDTO dto = new FlightResponseDTO();
        dto.id = f.getId().toString();
        dto.airlineName = f.getAirlineName();
        dto.flightNumber = f.getFlightNumber();
        dto.estDepartureTime = f.getEstDepartureTime();
        dto.estArrivalTime = f.getEstArrivalTime();
        dto.availableSeats = f.getAvailableSeats();
        return dto;
    }
}