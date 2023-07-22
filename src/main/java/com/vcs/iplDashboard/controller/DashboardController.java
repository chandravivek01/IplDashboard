package com.vcs.iplDashboard.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @GetMapping("/greet")
    public String displayMessage() {
        return "Welcome to the IPL Dashboard";
    }
}
