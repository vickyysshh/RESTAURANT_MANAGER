package com.example.restaurant.model;

import java.util.ArrayList;
import java.util.List;

public class DayMealPlan {
    private Integer dayNumber;
    private String dayName;
    private List<MealItem> breakfast;
    private List<MealItem> lunch;
    private List<MealItem> dinner;
    private List<MealItem> snacks;
    
    private Double totalDayCalories;
    private Double totalDayCost;
    private String reason; // Why these meals for this day
    
    public DayMealPlan() {
        this.breakfast = new ArrayList<>();
        this.lunch = new ArrayList<>();
        this.dinner = new ArrayList<>();
        this.snacks = new ArrayList<>();
        this.totalDayCalories = 0.0;
        this.totalDayCost = 0.0;
    }
    
    // Getters and Setters
    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
    
    public String getDayName() { return dayName; }
    public void setDayName(String dayName) { this.dayName = dayName; }
    
    public List<MealItem> getBreakfast() { return breakfast; }
    public void setBreakfast(List<MealItem> breakfast) { this.breakfast = breakfast; }
    
    public List<MealItem> getLunch() { return lunch; }
    public void setLunch(List<MealItem> lunch) { this.lunch = lunch; }
    
    public List<MealItem> getDinner() { return dinner; }
    public void setDinner(List<MealItem> dinner) { this.dinner = dinner; }
    
    public List<MealItem> getSnacks() { return snacks; }
    public void setSnacks(List<MealItem> snacks) { this.snacks = snacks; }
    
    public Double getTotalDayCalories() { return totalDayCalories; }
    public void setTotalDayCalories(Double totalDayCalories) { this.totalDayCalories = totalDayCalories; }
    
    public Double getTotalDayCost() { return totalDayCost; }
    public void setTotalDayCost(Double totalDayCost) { this.totalDayCost = totalDayCost; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
