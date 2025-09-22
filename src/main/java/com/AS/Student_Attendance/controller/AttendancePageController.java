package com.AS.Student_Attendance.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
class AttendancePageController {
    @GetMapping("/attendance/student")
    public String attendancePage() {
        return "attendance"; // Thymeleaf template name
    }
    // Add other MVC methods as needed
}