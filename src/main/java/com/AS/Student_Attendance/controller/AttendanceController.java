package com.AS.Student_Attendance.controller;

import com.AS.Student_Attendance.entity.Attendance;
import com.AS.Student_Attendance.repository.AttendanceRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/attendance")
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