package com.example.restaurant.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "aiMealPlans")  //It just maps your Java class → MongoDB collection
public class AIGeneratedMealPlan {
    
    @Id
    private String id;
    private String userId;
    private String dietProfileId;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    private List<DayMealPlan> dailyPlans; // Day 1 to Day 7/30
    private String aiExplanation;         // "This plan focuses on..."
    private Double estimatedWeeklySpend;
    private Double estimatedDailyCalories;
    
    // Nutritional summary  
    private Map<String, Double> avgMacros; // {"protein": 85, "carbs": 200, "fat": 50}
    
    private LocalDateTime generatedAt;
    private Boolean isActive;
    private List<String> userFeedback;    // Reviews/ratings
    
    public AIGeneratedMealPlan() {
        this.isActive = true;
        this.userFeedback = new ArrayList<>();
        this.avgMacros = new HashMap<>();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getDietProfileId() { return dietProfileId; }
    public void setDietProfileId(String dietProfileId) { this.dietProfileId = dietProfileId; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    
    public List<DayMealPlan> getDailyPlans() { return dailyPlans; }
    public void setDailyPlans(List<DayMealPlan> dailyPlans) { this.dailyPlans = dailyPlans; }
    
    public String getAiExplanation() { return aiExplanation; }
    public void setAiExplanation(String aiExplanation) { this.aiExplanation = aiExplanation; }
    
    public Double getEstimatedWeeklySpend() { return estimatedWeeklySpend; }
    public void setEstimatedWeeklySpend(Double estimatedWeeklySpend) { this.estimatedWeeklySpend = estimatedWeeklySpend; }
    
    public Double getEstimatedDailyCalories() { return estimatedDailyCalories; }
    public void setEstimatedDailyCalories(Double estimatedDailyCalories) { this.estimatedDailyCalories = estimatedDailyCalories; }
    
    public Map<String, Double> getAvgMacros() { return avgMacros; }
    public void setAvgMacros(Map<String, Double> avgMacros) { this.avgMacros = avgMacros; }
    
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public List<String> getUserFeedback() { return userFeedback; }
    public void setUserFeedback(List<String> userFeedback) { this.userFeedback = userFeedback; }
}
