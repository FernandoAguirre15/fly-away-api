package com.flyaway.api.event;

import com.flyaway.api.entity.Booking;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

@Component
public class BookingEventListener {

    @EventListener
    public void handleBookingEvent(BookingEvent event) throws IOException {
        Booking b = event.getBooking();

        String bookingTime = b.getBookingDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String depTime = b.getFlight().getEstDepartureTime().format(formatter);
        String arrTime = b.getFlight().getEstArrivalTime().format(formatter);

        String content = String.format(
                "Hello %s %s,%n%nYour booking was successful! %n%n" +
                        "The booking is for flight %s with departure date of %s and arrival date of %s.%n%n" +
                        "The booking was registered at %s.%n%nBon Voyage!%nFly Away Travel%n",
                b.getCustomer().getFirstName(),
                b.getCustomer().getLastName(),
                b.getFlight().getFlightNumber(),
                depTime,
                arrTime,
                bookingTime
        );

        Files.writeString(Path.of(
                "/Users/rosagoicochea/IdeaProjects/flyaway-api/-cs2031-2026-1-week07-tester/flight_booking_email_"
                        + b.getId() + ".txt"
        ), content);
    }
}
