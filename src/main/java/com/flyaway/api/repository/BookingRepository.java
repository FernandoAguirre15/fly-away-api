package com.flyaway.api.repository;

import com.flyaway.api.entity.Booking;
import com.flyaway.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer(User customer);
}