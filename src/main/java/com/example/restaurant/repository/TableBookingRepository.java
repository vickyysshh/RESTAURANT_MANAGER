package com.example.restaurant.repository;

import com.example.restaurant.model.TableBooking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TableBookingRepository extends MongoRepository<TableBooking, String> {
    List<TableBooking> findByUserIdOrderByCreatedAtDesc(String userId);
    List<TableBooking> findAllByOrderByCreatedAtDesc();
}
