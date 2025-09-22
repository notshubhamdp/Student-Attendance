package com.AS.Student_Attendance.controller;

import com.AS.Student_Attendance.entity.User;
import com.AS.Student_Attendance.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/student/profile")
    public Object getProfile(HttpSession session) {
        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("userId");
        if (role == null || userId == null) {
            return new org.springframework.web.servlet.view.RedirectView("/login");
        }
        if (!role.equals("STUDENT")) {
            return new org.springframework.web.servlet.view.RedirectView("/teacher/dashboard");
        }
        User student = userRepository.findById(userId).orElse(null);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        if (student != null) {
            result.put("name", student.getFirstName() + " " + student.getLastName());
            result.put("rollNo", student.getRollNo());
            result.put("department", student.getDepartment());
            result.put("email", student.getEmail());
        }
        return result;
    }
}
