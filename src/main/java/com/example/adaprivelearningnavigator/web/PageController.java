package com.example.adaprivelearningnavigator.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String landing() {
        return "forward:/index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "forward:/login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "forward:/register.html";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/dashboard.html";
    }

    @GetMapping("/roadmap")
    public String roadmap() {
        return "forward:/roadmap.html";
    }

    @GetMapping("/plan")
    public String plan() {
        return "forward:/plan.html";
    }

    @GetMapping("/profile")
    public String profile() {
        return "forward:/profile.html";
    }

    @GetMapping("/progress")
    public String progress(HttpServletRequest request) {
        String query = request.getQueryString();
        return query == null || query.isBlank()
                ? "redirect:/plan"
                : "redirect:/plan?" + query;
    }
}
