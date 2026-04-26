package com.example.restaurant;

import com.example.restaurant.model.MenuItem;
import com.example.restaurant.model.Order;
import com.example.restaurant.model.OrderItem;
import com.example.restaurant.model.TableBooking;
import com.example.restaurant.model.User;
import com.example.restaurant.model.UserDietProfile;
import com.example.restaurant.model.AIGeneratedMealPlan;
import com.example.restaurant.model.DayMealPlan;
import com.example.restaurant.model.MealItem;
import com.example.restaurant.repository.MenuItemRepository;
import com.example.restaurant.repository.OrderRepository;
import com.example.restaurant.repository.TableBookingRepository;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.repository.UserDietProfileRepository;
import com.example.restaurant.repository.AIGeneratedMealPlanRepository;
import com.example.restaurant.service.MealPlanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    //auto cret ob witho crte
    @Autowired private UserRepository userRepository;
    @Autowired private MenuItemRepository menuItemRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private TableBookingRepository tableBookingRepository;
    @Autowired private UserDietProfileRepository userDietProfileRepository;
    @Autowired private AIGeneratedMealPlanRepository aiMealPlanRepository;
    @Autowired private MealPlanService mealPlanService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ─── AUTH ──────

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body, HttpSession session) {
        String email = body.get("email");
        String password = body.get("password");
        String fullName = body.get("fullName");
        String role = body.getOrDefault("role", "CUSTOMER");

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }

        User user = new User(fullName, email, passwordEncoder.encode(password), role);
        userRepository.save(user);

        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("userName", user.getName());

        return ResponseEntity.ok(Map.of("message", "Signup successful", "role", user.getRole()));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        User user = userOpt.get();
        session.setAttribute("userId", user.getId());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("userName", user.getName());

        return ResponseEntity.ok(Map.of("message", "Login successful", "role", user.getRole()));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> getMe(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        return ResponseEntity.ok(Map.of(
            "id", userId,
            "name", session.getAttribute("userName"),
            "role", session.getAttribute("userRole")
        ));
    }

    // ─── MENU ─────────────────────────────────────────────────────────────────

    @GetMapping("/menu")
    public ResponseEntity<?> getMenu() {
        return ResponseEntity.ok(menuItemRepository.findAll());
    }

    @PostMapping("/menu")
    public ResponseEntity<?> createMenuItem(@RequestBody MenuItem item, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        return ResponseEntity.ok(menuItemRepository.save(item));
    }

    @PutMapping("/menu/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable String id, @RequestBody MenuItem updated, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        return menuItemRepository.findById(id).map(item -> {
            item.setItemName(updated.getItemName());
            item.setPrice(updated.getPrice());
            item.setDescription(updated.getDescription());
            item.setCategory(updated.getCategory());
            item.setImageUrl(updated.getImageUrl());
            item.setInStock(updated.getInStock());
            return ResponseEntity.ok(menuItemRepository.save(item));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable String id, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        menuItemRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
    }

    // ─── ORDERS ───────────────────────────────────────────────────────────────

    @PostMapping("/orders")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> body, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rawItems = (List<Map<String, Object>>) body.get("items");
        if (rawItems == null || rawItems.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (Map<String, Object> raw : rawItems) {
            @SuppressWarnings("unchecked")
            Map<String, Object> rawMenuItem = (Map<String, Object>) raw.get("menuItem");

            OrderItem.MenuItemRef ref = new OrderItem.MenuItemRef();
            ref.setId(String.valueOf(rawMenuItem.getOrDefault("id", rawMenuItem.getOrDefault("_id", ""))));
            ref.setItemName(String.valueOf(rawMenuItem.getOrDefault("itemName", "Unknown")));
            ref.setPrice(Double.parseDouble(String.valueOf(rawMenuItem.getOrDefault("price", 0))));

            int qty = Integer.parseInt(String.valueOf(raw.getOrDefault("quantity", 1)));

            OrderItem item = new OrderItem();
            item.setMenuItem(ref);
            item.setQuantity(qty);
            item.setSubtotal(ref.getPrice() * qty);
            orderItems.add(item);
        }

        double total = Double.parseDouble(String.valueOf(body.getOrDefault("totalPrice", 0)));

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(orderItems);
        order.setTotalPrice(total);
        order.setStatus("PAID");
        order.setCreatedAt(LocalDateTime.now());

        return ResponseEntity.ok(orderRepository.save(order));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        List<Order> orders = orderRepository.findAll();
        orders.sort((a, b) -> {
            if (a.getCreatedAt() == null || b.getCreatedAt() == null) return 0;
            return b.getCreatedAt().compareTo(a.getCreatedAt());
        });
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/my-orders")
    public ResponseEntity<?> getMyOrders(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        return ResponseEntity.ok(orderRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Map<String, String> body, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        String status = body.get("status");
        List<String> valid = List.of("CART", "PAID", "PREPARING", "READY", "DELIVERED");
        if (!valid.contains(status)) return ResponseEntity.badRequest().body(Map.of("error", "Invalid status"));

        return orderRepository.findById(id).map(order -> {
            order.setStatus(status);
            return ResponseEntity.ok(orderRepository.save(order));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ─── TABLE BOOKINGS ─────────────────────────────────────────────────────

    @PostMapping("/table-bookings")
    public ResponseEntity<?> createTableBooking(@RequestBody Map<String, Object> body, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        if (!"CUSTOMER".equals(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Only customers can create table bookings"));
        }

        String dateTimeRaw = String.valueOf(body.getOrDefault("bookingDateTime", ""));
        if (dateTimeRaw.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Booking date and time is required"));
        }

        LocalDateTime bookingDateTime;
        try {
            bookingDateTime = LocalDateTime.parse(dateTimeRaw);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid booking date format"));
        }

        int guestCount;
        try {
            guestCount = Integer.parseInt(String.valueOf(body.getOrDefault("guestCount", "0")));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid guest count"));
        }
        if (guestCount < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Guest count must be at least 1"));
        }

        TableBooking booking = new TableBooking();
        booking.setUserId(userId);
        booking.setUserName(String.valueOf(session.getAttribute("userName")));
        booking.setContactPhone(String.valueOf(body.getOrDefault("contactPhone", "")));
        booking.setBookingDateTime(bookingDateTime);
        booking.setGuestCount(guestCount);
        booking.setSpecialRequest(String.valueOf(body.getOrDefault("specialRequest", "")));
        booking.setStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.now());

        return ResponseEntity.ok(tableBookingRepository.save(booking));
    }

    @GetMapping("/table-bookings/my")
    public ResponseEntity<?> getMyTableBookings(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        return ResponseEntity.ok(tableBookingRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @GetMapping("/table-bookings")
    public ResponseEntity<?> getAllTableBookings(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }
        return ResponseEntity.ok(tableBookingRepository.findAllByOrderByCreatedAtDesc());
    }

    @PutMapping("/table-bookings/{id}/status")
    public ResponseEntity<?> updateTableBookingStatus(@PathVariable String id, @RequestBody Map<String, String> body, HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }

        String status = String.valueOf(body.getOrDefault("status", ""));
        List<String> valid = List.of("PENDING", "APPROVED", "REJECTED");
        if (!valid.contains(status)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid booking status"));
        }

        return tableBookingRepository.findById(id).map(booking -> {
            booking.setStatus(status);
            booking.setManagerNote(String.valueOf(body.getOrDefault("managerNote", "")));
            return ResponseEntity.ok(tableBookingRepository.save(booking));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ─── BILLING ────────────────────────────────────────────────────────────

    @GetMapping("/billing/my")
    public ResponseEntity<?> getMyBilling(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));

        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        double totalPaid = orders.stream().mapToDouble(Order::getTotalPrice).sum();

        return ResponseEntity.ok(Map.of(
            "orders", orders,
            "summary", Map.of(
                "totalPaid", totalPaid,
                "orderCount", orders.size()
            )
        ));
    }

    @GetMapping("/billing/overview")
    public ResponseEntity<?> getBillingOverview(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role)) {
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden"));
        }

        List<Order> orders = orderRepository.findAll();
        Set<String> billableStatuses = Set.of("PAID", "PREPARING", "READY", "DELIVERED");

        List<Map<String, Object>> bills = new ArrayList<>();
        double totalRevenue = 0;
        for (Order order : orders) {
            if (!billableStatuses.contains(order.getStatus())) continue;
            totalRevenue += order.getTotalPrice();
            String customerName = userRepository.findById(order.getUserId())
                .map(User::getName)
                .orElse("Unknown");

            bills.add(Map.of(
                "orderId", order.getId(),
                "customerName", customerName,
                "status", order.getStatus(),
                "totalPrice", order.getTotalPrice(),
                "createdAt", order.getCreatedAt()
            ));
        }

        bills.sort((a, b) -> {
            LocalDateTime aTime = (LocalDateTime) a.get("createdAt");
            LocalDateTime bTime = (LocalDateTime) b.get("createdAt");
            if (aTime == null || bTime == null) return 0;
            return bTime.compareTo(aTime);
        });

        return ResponseEntity.ok(Map.of(
            "summary", Map.of(
                "totalRevenue", totalRevenue,
                "totalBills", bills.size()
            ),
            "bills", bills
        ));
    }

    // ─── AI MEAL PLAN ────────────────────────────────────────────────────────

    @PostMapping("/diet-profile")
    public ResponseEntity<?> saveDietProfile(@RequestBody UserDietProfile profile, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        
        profile.setUserId(userId);
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        
        // Calculate BMI
        if (profile.getWeight() != null && profile.getHeight() != null) {
            double bmi = profile.getWeight() / (profile.getHeight() * profile.getHeight() / 10000);
            profile.setBmi(bmi);
        }
        
        UserDietProfile saved = userDietProfileRepository.save(profile);
        
        return ResponseEntity.ok(Map.of(
            "message", "Diet profile saved successfully",
            "profileId", saved.getId()
        ));
    }

    @GetMapping("/diet-profile")
    public ResponseEntity<?> getDietProfile(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        
        List<UserDietProfile> profiles = userDietProfileRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (profiles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profiles.get(0));
    }

    @PostMapping("/generate-meal-plan")
    public ResponseEntity<?> generateMealPlan(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        
        List<UserDietProfile> profiles = userDietProfileRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (profiles.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please fill diet profile first"));
        }
        
        try {
            AIGeneratedMealPlan mealPlan = mealPlanService.generateMealPlanWithAI(userId, profiles.get(0));
            return ResponseEntity.ok(Map.of(
                "message", "Meal plan generated successfully",
                "mealPlan", mealPlan
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to generate meal plan: " + e.getMessage()));
        }
    }

    @GetMapping("/meal-plans")
    public ResponseEntity<?> getMealPlans(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        
        List<AIGeneratedMealPlan> plans = aiMealPlanRepository.findByUserIdOrderByGeneratedAtDesc(userId);
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/meal-plans/{planId}")
    public ResponseEntity<?> getMealPlanDetails(@PathVariable String planId, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        
        return aiMealPlanRepository.findById(planId)
            .filter(plan -> plan.getUserId().equals(userId))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/meal-plans/{planId}")
    public ResponseEntity<?> deleteMealPlan(@PathVariable String planId, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        
        return aiMealPlanRepository.findById(planId)
            .filter(plan -> plan.getUserId().equals(userId))
            .map(plan -> {
                aiMealPlanRepository.deleteById(planId);
                return ResponseEntity.ok(Map.of("message", "Meal plan deleted successfully"));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/meal-plans/{planId}/order-day/{dayNumber}")
    public ResponseEntity<?> orderMealFromPlan(
        @PathVariable String planId,
        @PathVariable int dayNumber,
        HttpSession session
    ) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        
        java.util.Optional<AIGeneratedMealPlan> planOpt = aiMealPlanRepository.findById(planId);
        if (planOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        AIGeneratedMealPlan plan = planOpt.get();
        DayMealPlan dayPlan = plan.getDailyPlans().stream()
            .filter(d -> d.getDayNumber() == dayNumber)
            .findFirst()
            .orElse(null);
        
        if (dayPlan == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Day not found in plan"));
        }
        
        // Create order from day's meals
        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;
        
        // Add breakfast items
        for (MealItem meal : dayPlan.getBreakfast()) {
            OrderItem.MenuItemRef ref = new OrderItem.MenuItemRef();
            ref.setId(meal.getMenuItemId());
            ref.setItemName(meal.getItemName());
            ref.setPrice(meal.getPrice() != null ? meal.getPrice() : 0);
            
            OrderItem item = new OrderItem();
            item.setMenuItem(ref);
            item.setQuantity(1);
            item.setSubtotal(ref.getPrice());
            orderItems.add(item);
            total += ref.getPrice();
        }
        
        // Add lunch items
        for (MealItem meal : dayPlan.getLunch()) {
            OrderItem.MenuItemRef ref = new OrderItem.MenuItemRef();
            ref.setId(meal.getMenuItemId());
            ref.setItemName(meal.getItemName());
            ref.setPrice(meal.getPrice() != null ? meal.getPrice() : 0);
            
            OrderItem item = new OrderItem();
            item.setMenuItem(ref);
            item.setQuantity(1);
            item.setSubtotal(ref.getPrice());
            orderItems.add(item);
            total += ref.getPrice();
        }
        
        // Add dinner items
        for (MealItem meal : dayPlan.getDinner()) {
            OrderItem.MenuItemRef ref = new OrderItem.MenuItemRef();
            ref.setId(meal.getMenuItemId());
            ref.setItemName(meal.getItemName());
            ref.setPrice(meal.getPrice() != null ? meal.getPrice() : 0);
            
            OrderItem item = new OrderItem();
            item.setMenuItem(ref);
            item.setQuantity(1);
            item.setSubtotal(ref.getPrice());
            orderItems.add(item);
            total += ref.getPrice();
        }
        
        Order order = new Order();
        order.setUserId(userId);
        order.setItems(orderItems);
        order.setTotalPrice(total);
        order.setStatus("PAID");
        order.setCreatedAt(LocalDateTime.now());
        
        return ResponseEntity.ok(orderRepository.save(order));
    }
}
