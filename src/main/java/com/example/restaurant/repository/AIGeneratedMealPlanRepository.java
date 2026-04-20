package com.example.restaurant.repository;

import com.example.restaurant.model.AIGeneratedMealPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AIGeneratedMealPlanRepository extends MongoRepository<AIGeneratedMealPlan, String> {
    List<AIGeneratedMealPlan> findByUserIdOrderByGeneratedAtDesc(String userId);
    List<AIGeneratedMealPlan> findByUserIdAndIsActiveTrueOrderByGeneratedAtDesc(String userId);
}
