package com.example.restaurant.repository;

import com.example.restaurant.model.UserDietProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserDietProfileRepository extends MongoRepository<UserDietProfile, String> {
    List<UserDietProfile> findByUserIdOrderByCreatedAtDesc(String userId);
    
    @Deprecated
    Optional<UserDietProfile> findByUserId(String userId);
}
