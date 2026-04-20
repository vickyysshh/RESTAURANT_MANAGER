package com.example.restaurant.model;

public class MealItem {
    private String menuItemId;
    private String itemName;
    private Integer calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double price;
    private String aiReason;  // "High protein to support muscle gain"
    
    public MealItem() {}
    
    public MealItem(String menuItemId, String itemName, Integer calories, Double protein, Double carbs, Double fat, Double price) {
        this.menuItemId = menuItemId;
        this.itemName = itemName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.price = price;
    }
    
    // Getters and Setters
    public String getMenuItemId() { return menuItemId; }
    public void setMenuItemId(String menuItemId) { this.menuItemId = menuItemId; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }
    
    public Double getProtein() { return protein; }
    public void setProtein(Double protein) { this.protein = protein; }
    
    public Double getCarbs() { return carbs; }
    public void setCarbs(Double carbs) { this.carbs = carbs; }
    
    public Double getFat() { return fat; }
    public void setFat(Double fat) { this.fat = fat; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public String getAiReason() { return aiReason; }
    public void setAiReason(String aiReason) { this.aiReason = aiReason; }
}
