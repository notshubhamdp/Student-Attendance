package com.AS.Student_Attendance.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeacherDashboardController {

    @GetMapping("/teacher/dashboard")
    public Object teacherDashboard(HttpSession session) {
        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("userId");
        if (role == null || userId == null) {
            return new org.springframework.web.servlet.view.RedirectView("/login");
        }
        if (!role.equals("TEACHER")) {
            return new org.springframework.web.servlet.view.RedirectView("/student/profile");
        }
        // Return teacher dashboard data here
        return "Teacher dashboard data";
    }
}