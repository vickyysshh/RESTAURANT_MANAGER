package com.example.restaurant;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("userId") != null) return "redirect:/role-selection";
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("userId") != null) return "redirect:/role-selection";
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(HttpSession session) {
        if (session.getAttribute("userId") != null) return "redirect:/role-selection";
        return "signup";
    }

    @GetMapping("/role-selection")
    public String roleSelection(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        return "role-selection";
    }

    @GetMapping("/customer-browse")
    public String customerBrowse(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        String role = (String) session.getAttribute("userRole");
        if (!"CUSTOMER".equals(role)) return "redirect:/role-selection";
        return "customer-browse";
    }

    @GetMapping("/restaurant-menu")
    public String restaurantMenu(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        String role = (String) session.getAttribute("userRole");
        if (!"CUSTOMER".equals(role)) return "redirect:/role-selection";
        return "restaurant-menu";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return "redirect:/role-selection";
        return "admin-dashboard";
    }

    @GetMapping("/manager-dashboard")
    public String managerDashboard(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        String role = (String) session.getAttribute("userRole");
        if (!"MANAGER".equals(role) && !"ADMIN".equals(role)) return "redirect:/role-selection";
        return "manager-dashboard";
    }

    @GetMapping("/meal-plan")
    public String mealPlan(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        String role = (String) session.getAttribute("userRole");
        if (!"CUSTOMER".equals(role)) return "redirect:/role-selection";
        return "meal-plan";
    }
}
