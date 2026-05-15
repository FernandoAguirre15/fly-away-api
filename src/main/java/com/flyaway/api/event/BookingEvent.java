package com.flyaway.api.event;

import com.flyaway.api.entity.Booking;
import org.springframework.context.ApplicationEvent;

public class BookingEvent extends ApplicationEvent {
    private final Booking booking;

    public BookingEvent(Object source, Booking booking) {
        super(source);
        this.booking = booking;
    }

    public Booking getBooking() {
        return booking;
    }
}