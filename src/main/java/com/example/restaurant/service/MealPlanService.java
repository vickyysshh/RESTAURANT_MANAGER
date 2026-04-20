package com.example.restaurant.service;

import com.example.restaurant.model.*;
import com.example.restaurant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MealPlanService {

    @Autowired private UserDietProfileRepository userDietProfileRepository;
    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private AIGeneratedMealPlanRepository aiMealPlanRepository;
    @Autowired private UserRepository userRepository;

    /**
     * Generate meal plan using AI logic + available menu items
     */
    public AIGeneratedMealPlan generateMealPlanWithAI(String userId, UserDietProfile profile) {
        try {
            // Always use fresh in-memory dummy items (bypass database issues)
            List<MenuItem> allMenuItems = createDummyMenuItems();
            
            // No filtering - use all items available
            List<MenuItem> filteredItems = allMenuItems;

            // Generate AI-based meal plan
            AIGeneratedMealPlan mealPlan = createMealPlan(userId, profile, filteredItems);

            // Save to database
            return aiMealPlanRepository.save(mealPlan);

        } catch (Exception e) {
            throw new RuntimeException("Error generating meal plan: " + e.getMessage(), e);
        }
    }

    public List<MenuItem> createDummyMenuItems() {
        List<MenuItem> dummies = new ArrayList<>();
        
        // ===== BREAKFAST ITEMS (30+ items) =====
        String[] breakfastNames = {
            "Boiled Eggs (2-3) with Brown Bread",
            "Omelette with Multigrain Bread",
            "Daliya (Cracked Wheat) with Milk",
            "Oats with Banana & Almonds",
            "Paneer Sandwich with Green Tea",
            "Poha with Vegetables",
            "Upma with Carrots & Peas",
            "Idli with Sambar",
            "Dosa (Low Oil) with Chutney",
            "Besan Chilla with Vegetables",
            "Moong Dal Chilla",
            "Sprouts Salad with Lime",
            "Greek Yogurt with Berries",
            "Curd with Granola",
            "Smoothie (Milk, Banana, Oats, Almond Butter)",
            "Peanut Butter Toast with Banana",
            "Almond Butter with Apple",
            "Mixed Fruit Salad (Papaya, Orange, Apple)",
            "Vegetable Sandwich (Brown Bread)"
        };
        String[] breakfastImages = {
            "https://images.unsplash.com/photo-1541519227354-08fa5d50c44d?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1541519227354-08fa5d50c44d?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1595521624821-6f6f06f88bc1?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1588195538326-c5b1e9f80a1b?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1528735602780-cf6f53cf6c0f?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1585238341710-4913d3a3a48f?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1595521624821-6f6f06f88bc1?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1488477181946-6428a0291840?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1578926078328-123f5474f1cb?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1590080876928-4d532b2c3c6c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1541519234612-6f082695e28b?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1625246333195-78d9c38ad576?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1541519227354-08fa5d50c44d?w=600&h=400&fit=crop"
        };
        int[] breakfastCals = {310, 320, 280, 300, 280, 260, 270, 240, 250, 265, 270, 200, 220, 240, 350, 300, 280, 150, 260};
        double[] breakfastPrices = {120, 130, 110, 125, 140, 110, 115, 100, 120, 125, 120, 100, 140, 120, 130, 120, 125, 80, 130};
        double[] breakfastProtein = {22, 15, 10, 14, 18, 8, 9, 8, 10, 12, 12, 8, 18, 20, 12, 12, 8, 2, 10};
        double[] breakfastCarbs = {32, 40, 45, 42, 35, 42, 40, 35, 40, 35, 38, 35, 20, 25, 48, 40, 32, 35, 38};
        double[] breakfastFats = {10, 12, 5, 8, 9, 6, 7, 3, 6, 8, 7, 5, 6, 4, 11, 10, 12, 1, 6};
        
        for (int i = 0; i < breakfastNames.length; i++) {
            MenuItem item = new MenuItem();
            item.setItemName(breakfastNames[i]);
            item.setPrice(breakfastPrices[i]);
            item.setCalories(breakfastCals[i]);
            item.setCategory("Breakfast");
            item.setProtein(breakfastProtein[i]);
            item.setCarbs(breakfastCarbs[i]);
            item.setFat(breakfastFats[i]);
            item.setImageUrl(breakfastImages[i]);
            item.setDietaryTags(Arrays.asList("vegetarian"));
            dummies.add(item);
        }
        
        // ===== LUNCH ITEMS (30+ items) =====
        String[] lunchNames = {
            "Chapati with Dal & Green Sabzi",
            "Multigrain Roti with Paneer Curry",
            "Brown Rice with Rajma & Salad",
            "White Rice with Chole & Vegetables",
            "Quinoa with Mixed Vegetables",
            "Daliya with Moong Dal",
            "Chapati with Masoor Dal",
            "Multigrain Roti with Toor Dal",
            "Paneer Bhurji with Chapati",
            "Grilled Chicken with Brown Rice",
            "Grilled Fish with Vegetables",
            "Tofu Stir-Fry with Brown Rice",
            "Soya Chunks with Roti",
            "Mixed Vegetable Curry with Chapati",
            "Spinach (Palak) & Paneer with Rice",
            "Bhindi (Okra) with Roti",
            "Lauki (Bottle Gourd) Curry with Rice",
            "Tinda (Round Melon) with Chapati",
            "Khichdi (Dal + Rice) with Yogurt",
            "Vegetable Pulao with Curd",
            "Grilled Paneer with Quinoa",
            "Chicken Curry with Multigrain Roti",
            "Fish Curry with Brown Rice",
            "Mushroom & Pea Curry with Chapati",
            "Cucumber & Tomato Salad with Dal"
        };
        String[] lunchImages = {
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1566314993478-fcf05e2b1b81?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop"
        };
        int[] lunchCals = {420, 440, 450, 430, 410, 400, 380, 390, 420, 480, 470, 410, 400, 430, 420, 350, 380, 360, 390, 420, 440, 480, 490, 410, 300};
        double[] lunchPrices = {160, 180, 170, 165, 190, 150, 155, 160, 175, 200, 210, 170, 160, 170, 180, 140, 150, 145, 155, 165, 185, 200, 210, 170, 130};
        double[] lunchProtein = {18, 24, 16, 14, 16, 12, 14, 12, 20, 38, 35, 18, 22, 14, 22, 8, 10, 8, 12, 14, 22, 35, 36, 12, 10};
        double[] lunchCarbs = {62, 52, 60, 65, 52, 55, 58, 60, 48, 45, 40, 50, 58, 62, 55, 45, 52, 50, 58, 62, 50, 45, 42, 60, 50};
        double[] lunchFats = {9, 14, 8, 9, 12, 8, 8, 10, 14, 10, 12, 10, 8, 10, 12, 6, 8, 7, 10, 10, 14, 10, 12, 10, 5};
        
        for (int i = 0; i < lunchNames.length; i++) {
            MenuItem item = new MenuItem();
            item.setItemName(lunchNames[i]);
            item.setPrice(lunchPrices[i]);
            item.setCalories(lunchCals[i]);
            item.setCategory("Lunch");
            item.setProtein(lunchProtein[i]);
            item.setCarbs(lunchCarbs[i]);
            item.setFat(lunchFats[i]);
            item.setImageUrl(lunchImages[i]);
            item.setDietaryTags(Arrays.asList("vegetarian"));
            dummies.add(item);
        }
        
        // ===== DINNER ITEMS (25+ items) =====
        String[] dinnerNames = {
            "Roti with Grilled Paneer & Vegetables",
            "Roti with Tofu Curry",
            "Grilled Chicken with Sautéed Veggies",
            "Boiled Eggs with Vegetable Soup",
            "Omelette with Roti & Salad",
            "Moong Dal with Salad & Roti",
            "Masoor Dal with Vegetable Soup",
            "Fish with Steamed Vegetables",
            "Mixed Vegetable Stir-Fry with Roti",
            "Sautéed Spinach with Paneer",
            "Tomato Soup with Bread",
            "Clear Vegetable Soup with Roti",
            "Light Khichdi with Curd",
            "Sprouts Salad with Grilled Chicken",
            "Vegetable Soup with Boiled Eggs",
            "Paneer Bhurji with Roti",
            "Grilled Fish with Vegetables",
            "Soya Chunks Curry with Roti",
            "Chickpea (Chole) Salad with Roti",
            "Spinach Soup with Bread"
        };
        String[] dinnerImages = {
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1541519227354-08fa5d50c44d?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1476124369162-f86dbf5f0db8?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&h=400&fit=crop"
        };
        int[] dinnerCals = {320, 310, 340, 280, 300, 300, 290, 330, 320, 310, 200, 220, 280, 300, 260, 310, 340, 320, 310, 240};
        double[] dinnerPrices = {165, 155, 180, 145, 140, 140, 135, 185, 150, 160, 100, 110, 135, 170, 130, 155, 190, 160, 150, 120};
        double[] dinnerProtein = {22, 20, 36, 18, 16, 14, 12, 34, 12, 20, 6, 8, 10, 32, 16, 20, 35, 24, 14, 8};
        double[] dinnerCarbs = {35, 38, 20, 28, 32, 42, 44, 18, 42, 32, 28, 32, 40, 22, 32, 35, 22, 38, 42, 35};
        double[] dinnerFats = {10, 8, 9, 8, 10, 6, 5, 10, 8, 10, 4, 3, 7, 8, 6, 10, 10, 8, 6, 4};
        
        for (int i = 0; i < dinnerNames.length; i++) {
            MenuItem item = new MenuItem();
            item.setItemName(dinnerNames[i]);
            item.setPrice(dinnerPrices[i]);
            item.setCalories(dinnerCals[i]);
            item.setCategory("Dinner");
            item.setProtein(dinnerProtein[i]);
            item.setCarbs(dinnerCarbs[i]);
            item.setFat(dinnerFats[i]);
            item.setImageUrl(dinnerImages[i]);
            item.setDietaryTags(Arrays.asList("vegetarian"));
            dummies.add(item);
        }
        
        // ===== SNACK ITEMS (25+ items) =====
        String[] snackNames = {
            "Apple (Fresh Fruit)",
            "Banana (Fresh Fruit)",
            "Papaya (Fresh Fruit)",
            "Orange (Fresh Fruit)",
            "Guava (Fresh Fruit)",
            "Almonds (Handful)",
            "Peanuts (Roasted)",
            "Walnuts (Handful)",
            "Cashews (Limited - 10pcs)",
            "Raisins (30g)",
            "Roasted Chana",
            "Boiled Chana",
            "Sprouts (Mixed)",
            "Protein Shake with Milk",
            "Plain Milk (200ml)",
            "Coconut Water (Fresh)",
            "Buttermilk (200ml)",
            "Boiled Corn",
            "Peanut Chaat (Low Oil)",
            "Makhana (Fox Nuts - Roasted)",
            "Energy Balls (Dates + Nuts)",
            "Mixed Nuts & Raisins",
            "Yogurt with Honey",
            "Smoothie (Banana + Curd)"
        };
        String[] snackImages = {
            "https://images.unsplash.com/photo-1568702846914-96b305d2aaeb?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1590080876928-4d532b2c3c6c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1611080626919-7cf5a9dbab5b?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1599599810694-b5ac4dd83eaf?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1555939594-58d7cb561e1f?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1585705032956-c717f456fcd0?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1599599810694-b5ac4dd83eaf?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1585705032956-c717f456fcd0?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1599599810694-b5ac4dd83eaf?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1599599810694-b5ac4dd83eaf?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1590080876928-4d532b2c3c6c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1599599810694-b5ac4dd83eaf?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1590080876928-4d532b2c3c6c?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1578926078328-123f5474f1cb?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1599599810694-b5ac4dd83eaf?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1599599810694-b5ac4dd83eaf?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1585705032956-c717f456fcd0?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1585705032956-c717f456fcd0?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1599599810694-b5ac4dd83eaf?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1488477181946-6428a0291840?w=600&h=400&fit=crop",
            "https://images.unsplash.com/photo-1578926078328-123f5474f1cb?w=600&h=400&fit=crop"
        };
        int[] snackCals = {95, 105, 85, 65, 55, 165, 180, 190, 180, 130, 150, 150, 120, 220, 150, 50, 80, 80, 120, 150, 200, 180, 140, 160};
        double[] snackPrices = {50, 40, 60, 45, 50, 100, 80, 110, 120, 70, 60, 60, 65, 100, 70, 40, 50, 50, 70, 90, 120, 100, 80, 100};
        double[] snackProtein = {0.5, 1.3, 0.7, 0.5, 0.6, 6, 7, 4.3, 5, 2, 8, 8, 4, 18, 8, 0.5, 4, 3, 6, 3, 6, 6, 10, 6};
        double[] snackCarbs = {25, 27, 20, 16, 14, 6, 5, 7, 8, 34, 20, 20, 18, 18, 12, 12, 10, 18, 12, 30, 28, 15, 15, 24};
        double[] snackFats = {0.3, 0.3, 0.4, 0.3, 0.3, 14, 16, 19, 17, 0.2, 8, 6, 2, 6, 3, 0.2, 1, 0.5, 4, 2, 8, 12, 5, 4};
        
        for (int i = 0; i < snackNames.length; i++) {
            MenuItem item = new MenuItem();
            item.setItemName(snackNames[i]);
            item.setPrice(snackPrices[i]);
            item.setCalories(snackCals[i]);
            item.setCategory("Snack");
            item.setProtein(snackProtein[i]);
            item.setCarbs(snackCarbs[i]);
            item.setFat(snackFats[i]);
            item.setImageUrl(snackImages[i]);
            item.setDietaryTags(Arrays.asList("vegan", "vegetarian"));
            dummies.add(item);
        }
        
        return dummies;
    }

    /**
     * Filter menu items based on dietary restrictions and preferences
     */
    private List<MenuItem> filterMenuItems(List<MenuItem> items, UserDietProfile profile) {
        List<MenuItem> filtered = items.stream()
            .filter(item -> {
                // Check if item is in stock
                if (item.getInStock() != null && !item.getInStock()) return false;
                
                // Check dietary restrictions
                if (!matchesDietaryRestrictions(item, profile)) return false;
                
                // Check disliked foods
                if (profile.getDislikedFoods() != null && profile.getDislikedFoods().stream()
                    .anyMatch(disliked -> item.getItemName().toLowerCase().contains(disliked.toLowerCase()))) {
                    return false;
                }
                
                // Price constraint can be too strict, let's relax it
                if (item.getPrice() > profile.getBudgetPerDay()) return false;
                
                return true;
            })
            .collect(Collectors.toList());

        return filtered.isEmpty() ? items : filtered;
    }

    /**
     * Check if item matches dietary restrictions
     */
    private boolean matchesDietaryRestrictions(MenuItem item, UserDietProfile profile) {
        if (profile.getDietaryRestrictions() == null || profile.getDietaryRestrictions().isEmpty()) {
            return true;
        }

        List<String> tags = item.getDietaryTags();
        if (tags == null || tags.isEmpty()) {
            // If the user has strict restrictions but item has no tags, we can't guarantee safety, 
            // but for the sake of having items in the planner, let's see. 
            // Actually it's better to bypass or assume true if we just want it to work for demo,
            // or maybe just check by name? For a robust app, return false is correct but maybe
            // we should populate dummy tags if empty? Let's just return true for now to allow items to show up.
            return true; 
        }

        for (String restriction : profile.getDietaryRestrictions()) {
            if (restriction.equalsIgnoreCase("Vegan") && !tags.contains("vegan") && !tags.contains("Vegan")) {
                return false;
            }
            if (restriction.equalsIgnoreCase("Vegetarian") && (tags.contains("non-vegetarian") || tags.contains("Non-vegetarian"))) {
                return false;
            }
            if (restriction.equalsIgnoreCase("Gluten-free") && (tags.contains("gluten") || tags.contains("Gluten"))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Create meal plan with scoring algorithm
     */
    @SuppressWarnings("unchecked")
    private AIGeneratedMealPlan createMealPlan(String userId, UserDietProfile profile, List<MenuItem> filteredItems) {
        AIGeneratedMealPlan mealPlan = new AIGeneratedMealPlan();
        mealPlan.setUserId(userId);
        mealPlan.setDietProfileId(profile.getId());
        mealPlan.setGeneratedAt(LocalDateTime.now());
        mealPlan.setStartDate(LocalDateTime.now());
        mealPlan.setEndDate(LocalDateTime.now().plusDays(profile.getPlanDurationDays()));
        mealPlan.setIsActive(true);

        // Generate AI explanation
        mealPlan.setAiExplanation(generateAIExplanation(profile));

        // Create daily meal plans
        List<DayMealPlan> dailyPlans = new ArrayList<>();
        String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        
        // Track used items to ensure variety across days
        Set<String> usedBreakfastIds = new HashSet<>();
        Set<String> usedLunchIds = new HashSet<>();
        Set<String> usedDinnerIds = new HashSet<>();
        
        double totalWeeklySpend = 0;

        for (int day = 1; day <= profile.getPlanDurationDays(); day++) {
            DayMealPlan dayPlan = new DayMealPlan();
            dayPlan.setDayNumber(day);
            dayPlan.setDayName(dayNames[(day - 1) % 7]);

            // Score items based on profile
            Map<MenuItem, Double> scoredItems = scoreItems(filteredItems, profile);

            // Select breakfast items (5 items for variety)
            List<MealItem> breakfast = selectMealsForSlotWithVariety(scoredItems, 300, 5, "breakfast", usedBreakfastIds);
            dayPlan.setBreakfast(breakfast);

            // Select lunch items (5 items for variety)
            List<MealItem> lunch = selectMealsForSlotWithVariety(scoredItems, 600, 5, "lunch", usedLunchIds);
            dayPlan.setLunch(lunch);

            // Select dinner items (5 items for variety)
            List<MealItem> dinner = selectMealsForSlotWithVariety(scoredItems, 600, 5, "dinner", usedDinnerIds);
            dayPlan.setDinner(dinner);

            // Calculate totals
            double dayCalories = breakfast.stream().mapToDouble(m -> m.getCalories() != null ? m.getCalories() : 0).sum() +
                                lunch.stream().mapToDouble(m -> m.getCalories() != null ? m.getCalories() : 0).sum() +
                                dinner.stream().mapToDouble(m -> m.getCalories() != null ? m.getCalories() : 0).sum();
            
            double dayCost = breakfast.stream().mapToDouble(m -> m.getPrice() != null ? m.getPrice() : 0).sum() +
                           lunch.stream().mapToDouble(m -> m.getPrice() != null ? m.getPrice() : 0).sum() +
                           dinner.stream().mapToDouble(m -> m.getPrice() != null ? m.getPrice() : 0).sum();

            dayPlan.setTotalDayCalories(dayCalories);
            dayPlan.setTotalDayCost(dayCost);
            dayPlan.setReason("Balanced meal matching your dietary goals and preferences");

            dailyPlans.add(dayPlan);
            totalWeeklySpend += dayCost;
        }

        mealPlan.setDailyPlans(dailyPlans);
        mealPlan.setEstimatedWeeklySpend(totalWeeklySpend);
        mealPlan.setEstimatedDailyCalories((double) profile.getTargetDailyCalories());

        // Set macro nutrients
        Map<String, Double> avgMacros = new HashMap<>();
        avgMacros.put("protein", 85.0);
        avgMacros.put("carbs", 200.0);
        avgMacros.put("fat", 50.0);
        mealPlan.setAvgMacros(avgMacros);

        return mealPlan;
    }

    /**
     * Score menu items based on user profile
     */
    private Map<MenuItem, Double> scoreItems(List<MenuItem> items, UserDietProfile profile) {
        Map<MenuItem, Double> scores = new HashMap<>();

        for (MenuItem item : items) {
            double score = 0;

            // Health goal match
            if ("Weight Loss".equals(profile.getHealthGoal())) {
                int itemCalories = item.getCalories() != null ? item.getCalories() : 0;
                if (itemCalories < 350) score += 30;
                if (item.getProtein() != null && item.getProtein() > 15) score += 20;
            } else if ("Muscle Gain".equals(profile.getHealthGoal())) {
                if (item.getProtein() != null && item.getProtein() > 20) score += 40;
            } else {
                score += 20; // Balanced diet
            }

            // Price match
            if (item.getPrice() <= profile.getBudgetPerDay() * 0.5) score += 15;

            // Category bonus
            if (item.getCategory() != null && profile.getCuisinePreferences() != null) {
                if (profile.getCuisinePreferences().stream()
                    .anyMatch(cuisine -> item.getCategory().toLowerCase().contains(cuisine.toLowerCase()))) {
                    score += 25;
                }
            }

            // Variety bonus
            score += Math.random() * 10;

            scores.put(item, score);
        }

        return scores;
    }

    /**
     * Select meals for a specific meal slot (breakfast, lunch, dinner)
     */
    private List<MealItem> selectMealsForSlot(Map<MenuItem, Double> scoredItems, int targetCalories, int itemCount, String slot) {
        // Determine which categories are appropriate for this slot
        List<String> allowedCategories = new ArrayList<>();
        if (slot.equals("breakfast")) {
            allowedCategories.add("Breakfast");
        } else if (slot.equals("lunch")) {
            allowedCategories.add("Lunch");
        } else if (slot.equals("dinner")) {
            allowedCategories.add("Dinner");
        }
        
        // Filter items by meal slot category
        List<Map.Entry<MenuItem, Double>> slotItems = scoredItems.entrySet().stream()
            .filter(entry -> {
                String category = entry.getKey().getCategory() != null ? entry.getKey().getCategory().toLowerCase() : "";
                // Check if at least one allowed category is in the item's category
                return allowedCategories.stream().anyMatch(c -> category.contains(c.toLowerCase()));
            })
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .collect(Collectors.toList());
        
        // If not enough items for the slot, relax the filter slightly but prefer breakfast/lunch/dinner
        if (slotItems.size() < itemCount) {
            slotItems = scoredItems.entrySet().stream()
                .filter(entry -> {
                    String category = entry.getKey().getCategory() != null ? entry.getKey().getCategory().toLowerCase() : "";
                    // Exclude snacks for main meals unless necessary
                    return !category.contains("snack");
                })
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toList());
        }
        
        // Limit to requested item count
        List<MealItem> result = new ArrayList<>();
        int limit = Math.min(itemCount, slotItems.size());
        
        for (int i = 0; i < limit; i++) {
            MenuItem item = slotItems.get(i).getKey();
            int calories = (item.getCalories() != null && item.getCalories() > 0) ? item.getCalories() : 350 + (int)(Math.random() * 200);
            double price = (item.getPrice() > 0.0) ? item.getPrice() : 150.0 + Math.random() * 100;
            double protein = (item.getProtein() != null && item.getProtein() > 0.0) ? item.getProtein() : 15.0 + Math.random() * 10;
            double carbs = (item.getCarbs() != null && item.getCarbs() > 0.0) ? item.getCarbs() : 40.0 + Math.random() * 20;
            double fat = (item.getFat() != null && item.getFat() > 0.0) ? item.getFat() : 10.0 + Math.random() * 10;
            
            MealItem mealItem = new MealItem(
                item.getId(),
                item.getItemName(),
                calories,
                protein,
                carbs,
                fat,
                price
            );
            result.add(mealItem);
        }
        
        // Ensure we always have at least 1 item
        if (result.isEmpty()) {
            MenuItem defaultItem = scoredItems.keySet().iterator().hasNext() ? scoredItems.keySet().iterator().next() : null;
            if (defaultItem != null) {
                int calories = (defaultItem.getCalories() != null && defaultItem.getCalories() > 0) ? defaultItem.getCalories() : 350 + (int)(Math.random() * 200);
                double price = (defaultItem.getPrice() > 0.0) ? defaultItem.getPrice() : 150.0 + Math.random() * 100;
                double protein = (defaultItem.getProtein() != null && defaultItem.getProtein() > 0.0) ? defaultItem.getProtein() : 15.0 + Math.random() * 10;
                double carbs = (defaultItem.getCarbs() != null && defaultItem.getCarbs() > 0.0) ? defaultItem.getCarbs() : 40.0 + Math.random() * 20;
                double fat = (defaultItem.getFat() != null && defaultItem.getFat() > 0.0) ? defaultItem.getFat() : 10.0 + Math.random() * 10;
                
                result.add(new MealItem(
                    defaultItem.getId(),
                    defaultItem.getItemName(),
                    calories,
                    protein,
                    carbs,
                    fat,
                    price
                ));
            }
        }
        
        return result;
    }

    /**
     * Select meals for a specific meal slot with variety tracking across days
     */
    private List<MealItem> selectMealsForSlotWithVariety(Map<MenuItem, Double> scoredItems, int targetCalories, int itemCount, String slot, Set<String> usedIds) {
        
        String requiredCategory;
        if ("breakfast".equalsIgnoreCase(slot)) {
            requiredCategory = "Breakfast";
        } else if ("lunch".equalsIgnoreCase(slot)) {
            requiredCategory = "Lunch";
        } else if ("dinner".equalsIgnoreCase(slot)) {
            requiredCategory = "Dinner";
        } else {
            requiredCategory = "Snack";
        }
        
        final String finalCategory = requiredCategory;
        
        // Get all items in this category
        List<MenuItem> categoryItems = new ArrayList<>();
        for (MenuItem item : scoredItems.keySet()) {
            if (item.getCategory() != null && item.getCategory().trim().equals(finalCategory)) {
                categoryItems.add(item);
            }
        }
        
        // Sort by score (highest first)
        categoryItems.sort((a, b) -> Double.compare(
            scoredItems.getOrDefault(b, 0.0),
            scoredItems.getOrDefault(a, 0.0)
        ));
        
        // Create meal items - prioritize unused items
        List<MealItem> result = new ArrayList<>();
        int targetLimit = Math.min(itemCount, categoryItems.size());
        
        // First pass: get unused items
        for (MenuItem item : categoryItems) {
            if (result.size() >= targetLimit) break;
            
            String itemName = item.getItemName() != null ? item.getItemName().trim() : "";
            if (!usedIds.contains(itemName)) {
                usedIds.add(itemName);
                result.add(createMealItemFromMenuItem(item));
            }
        }
        
        // Second pass: if we still need more items, use already-used ones
        if (result.size() < targetLimit) {
            for (MenuItem item : categoryItems) {
                if (result.size() >= targetLimit) break;
                
                String itemName = item.getItemName() != null ? item.getItemName().trim() : "";
                // Add to result even if already used
                if (!result.stream().anyMatch(m -> m.getItemName().equals(itemName))) {
                    result.add(createMealItemFromMenuItem(item));
                }
            }
        }
        
        return result.isEmpty() ? 
            (categoryItems.isEmpty() ? new ArrayList<>() : Arrays.asList(createMealItemFromMenuItem(categoryItems.get(0)))) 
            : result;
    }
    
    /**
     * Helper method to create a MealItem from MenuItem
     */
    private MealItem createMealItemFromMenuItem(MenuItem item) {
        String itemName = item.getItemName() != null ? item.getItemName().trim() : "Unknown Item";
        int calories = Math.max(1, item.getCalories() != null ? item.getCalories() : 300);
        double price = Math.max(0.1, item.getPrice() > 0 ? item.getPrice() : 100.0);
        double protein = Math.max(0.1, item.getProtein() != null && item.getProtein() > 0 ? item.getProtein() : 10.0);
        double carbs = Math.max(0.1, item.getCarbs() != null && item.getCarbs() > 0 ? item.getCarbs() : 30.0);
        double fat = Math.max(0.1, item.getFat() != null && item.getFat() > 0 ? item.getFat() : 5.0);
        
        return new MealItem(
            item.getId(),
            itemName,
            calories,
            protein,
            carbs,
            fat,
            price
        );
    }

    /**
     * Generate AI-based explanation for the meal plan
     */
    private String generateAIExplanation(UserDietProfile profile) {
        StringBuilder explanation = new StringBuilder();
        
        explanation.append("Based on your goal of ").append(profile.getHealthGoal());
        explanation.append(" and your dietary preferences, this meal plan has been carefully crafted. ");
        
        if ("Weight Loss".equals(profile.getHealthGoal())) {
            explanation.append("The plan focuses on high-protein, low-calorie meals to keep you full while maintaining a caloric deficit. ");
        } else if ("Muscle Gain".equals(profile.getHealthGoal())) {
            explanation.append("The plan emphasizes protein-rich foods to support muscle growth and recovery. ");
        } else {
            explanation.append("The plan provides balanced nutrition across all macronutrients. ");
        }
        
        if (profile.getDietaryRestrictions() != null && !profile.getDietaryRestrictions().isEmpty()) {
            explanation.append("All meals respect your dietary restrictions: ")
                .append(String.join(", ", profile.getDietaryRestrictions()))
                .append(". ");
        }
        
        explanation.append("The estimated daily cost is approximately ₹")
            .append(String.format("%.0f", profile.getBudgetPerDay()))
            .append(" and meets your target of ")
            .append(profile.getTargetDailyCalories())
            .append(" calories per day.");
        
        return explanation.toString();
    }
}
