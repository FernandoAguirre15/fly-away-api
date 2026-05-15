package com.flyaway.api.controller;

import com.flyaway.api.service.BookingService;
import com.flyaway.api.service.FlightService;
import com.flyaway.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cleanup")
public class CleanupController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private UserService userService;

    @DeleteMapping
    public ResponseEntity<?> cleanup() {
        bookingService.deleteAll();
        flightService.deleteAll();
        userService.deleteAll();
        return ResponseEntity.ok().build();
    }
}