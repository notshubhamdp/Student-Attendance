package com.AS.Student_Attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.AS.Student_Attendance.repository.UserRepository;
import com.AS.Student_Attendance.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomePage {
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/delete-account")
    public String deleteAccount(@AuthenticationPrincipal UserDetails principal, HttpServletRequest request) {
        if (principal != null) {
            String username = principal.getUsername();
            User user = userRepository.findByUsername(username);
            if (user != null && (user.getRole().name().equals("STUDENT") || user.getRole().name().equals("TEACHER"))) {
                userRepository.delete(user);
            }
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }


    @GetMapping("/teachers")
    public String teachers() {
        return "teachers";
    }

    @GetMapping("/courses")
    public String courses() {
        return "courses";
    }



    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/attendance/view")
    public String attendance() {
        return "attendance";
    }

    @GetMapping("/profile/view")
    public String profile() {
        return "profile";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }


    @GetMapping("/teacher_dashboard")
    public String teacherDashboard() {
        return "teacher_dashboard";
    }

    @GetMapping("/student_dashboard")
    public String studentDashboard(Model model) {
        // For demo: get the first student user. In production, get from session/auth
        User student = userRepository.findAll().stream()
            .filter(u -> u.getRole().name().equals("STUDENT"))
            .findFirst().orElse(null);
    String studentName = student != null ? (student.getFirstName() + " " + student.getLastName()) : "Student";
    model.addAttribute("studentName", studentName);
    String department = student != null ? student.getDepartment() : "Department";
    model.addAttribute("department", department);
        String email = student != null ? student.getEmail() : "Email";
        model.addAttribute("email", email);
        String rollNo = "";
        // If roll number is stored in User entity, fetch it. Otherwise, set as needed.
        // Example: If you have getRollNo(), use student.getRollNo()
        // For now, set as placeholder
        if (student != null) {
            try {
                java.lang.reflect.Method method = student.getClass().getMethod("getRollNo");
                rollNo = (String) method.invoke(student);
            } catch (Exception e) {
                rollNo = "Assigned by Teacher";
            }
        } else {
            rollNo = "Assigned by Teacher";
        }
        model.addAttribute("rollNo", rollNo);
        return "student_dashboard";
    }
    
}
    
