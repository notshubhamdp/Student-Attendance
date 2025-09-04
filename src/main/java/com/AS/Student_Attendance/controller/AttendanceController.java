package com.AS.Student_Attendance.controller;

import com.AS.Student_Attendance.entity.Attendance;
import com.AS.Student_Attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;

@RestController
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @PostMapping("/attendance/mark")
    public Object markAttendance(@RequestBody Attendance attendance, HttpSession session) {
        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("userId");
        if (role == null || userId == null) {
            return new org.springframework.web.servlet.view.RedirectView("/login");
        }
        if (!role.equals("TEACHER")) {
            return new org.springframework.web.servlet.view.RedirectView("/student/profile");
        }
        // Save attendance record
        attendanceRepository.save(attendance);
        return "Attendance marked";
    }

    @GetMapping("/attendance/student")
    public Object viewStudentAttendance(HttpSession session) {
        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("userId");
        if (role == null || userId == null) {
            return new org.springframework.web.servlet.view.RedirectView("/login");
        }
        if (!role.equals("STUDENT")) {
            return new org.springframework.web.servlet.view.RedirectView("/teacher/dashboard");
        }
        // Only return attendance for this student
        java.util.List<Attendance> records = attendanceRepository.findByUser_UserId(userId);
        return records;
    }
}

@Controller
class AttendancePageController {
    @GetMapping("/attendance")
    public String attendancePage() {
        return "attendance"; // Thymeleaf template name
    }
    // Add other MVC methods as needed
}