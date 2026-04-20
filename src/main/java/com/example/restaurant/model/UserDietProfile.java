package com.example.restaurant.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "userDietProfiles")
public class UserDietProfile {
    
    @Id
    private String id;
    private String userId;
    
    // Questionnaire answers
    private String healthGoal;              // "Weight Loss", "Muscle Gain", "Balanced Diet"
    private List<String> dietaryRestrictions; // ["Vegan", "Gluten-free"]
    private List<String> cuisinePreferences;  // ["Indian", "Chinese"]
    private Double budgetPerDay;
    private Integer targetDailyCalories;
    private Integer planDurationDays;       // 3, 7, 14, 30
    private List<String> dislikedFoods;
    private Double weight;                  // kg
    private Double height;                  // cm
    private Double bmi;
    
    // AI-generated insights
    private String aiAssessment;            // "You should focus on high-protein, low-carb..."
    private String personalizedMessage;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public UserDietProfile() {}
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getHealthGoal() { return healthGoal; }
    public void setHealthGoal(String healthGoal) { this.healthGoal = healthGoal; }
    
    public List<String> getDietaryRestrictions() { return dietaryRestrictions; }
    public void setDietaryRestrictions(List<String> dietaryRestrictions) { this.dietaryRestrictions = dietaryRestrictions; }
    
    public List<String> getCuisinePreferences() { return cuisinePreferences; }
    public void setCuisinePreferences(List<String> cuisinePreferences) { this.cuisinePreferences = cuisinePreferences; }
    
    public Double getBudgetPerDay() { return budgetPerDay; }
    public void setBudgetPerDay(Double budgetPerDay) { this.budgetPerDay = budgetPerDay; }
    
    public Integer getTargetDailyCalories() { return targetDailyCalories; }
    public void setTargetDailyCalories(Integer targetDailyCalories) { this.targetDailyCalories = targetDailyCalories; }
    
    public Integer getPlanDurationDays() { return planDurationDays; }
    public void setPlanDurationDays(Integer planDurationDays) { this.planDurationDays = planDurationDays; }
    
    public List<String> getDislikedFoods() { return dislikedFoods; }
    public void setDislikedFoods(List<String> dislikedFoods) { this.dislikedFoods = dislikedFoods; }
    
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    
    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }
    
    public String getAiAssessment() { return aiAssessment; }
    public void setAiAssessment(String aiAssessment) { this.aiAssessment = aiAssessment; }
    
    public String getPersonalizedMessage() { return personalizedMessage; }
    public void setPersonalizedMessage(String personalizedMessage) { this.personalizedMessage = personalizedMessage; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
