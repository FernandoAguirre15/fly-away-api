package com.flyaway.api.repository;

import com.flyaway.api.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByFlightNumber(String flightNumber);
    List<Flight> findByFlightNumberContainingIgnoreCaseAndAirlineNameContainingIgnoreCase(
            String flightNumber, String airlineName);
    List<Flight> findByFlightNumberContainingIgnoreCaseAndAirlineNameContainingIgnoreCaseAndEstDepartureTimeGreaterThanEqual(
            String flightNumber, String airlineName, LocalDateTime from);
    List<Flight> findByFlightNumberContainingIgnoreCaseAndAirlineNameContainingIgnoreCaseAndEstDepartureTimeLessThanEqual(
            String flightNumber, String airlineName, LocalDateTime to);
    List<Flight> findByFlightNumberContainingIgnoreCaseAndAirlineNameContainingIgnoreCaseAndEstDepartureTimeGreaterThanEqualAndEstDepartureTimeLessThanEqual(
            String flightNumber, String airlineName, LocalDateTime from, LocalDateTime to);
}