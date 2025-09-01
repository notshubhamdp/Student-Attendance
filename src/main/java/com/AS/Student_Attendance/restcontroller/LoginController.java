package com.AS.Student_Attendance.restcontroller;

import com.AS.Student_Attendance.entity.User;
import com.AS.Student_Attendance.repository.UserRepository;
import com.AS.Student_Attendance.enumDto.ApprovalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
	// ...existing code...

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public Map<String, Object> login(@RequestBody Map<String, String> payload, HttpSession session) {
		String role = payload.get("role");
		String department = payload.get("department");
		Map<String, Object> response = new HashMap<>();


		if ("STUDENT".equalsIgnoreCase(role)) {
			String username = payload.get("username");
			String password = payload.get("password");
			User user = userRepository.findByUsername(username);
					if (user != null && user.getPassword().equals(password) && user.getRole().name().equals("STUDENT") && user.getDepartment() != null && user.getDepartment().equalsIgnoreCase(department)) {
				if (user.getStatus() == ApprovalStatus.APPROVED) {
					response.put("success", true);
					response.put("role", "STUDENT");
					response.put("message", "Login successful");
					session.setAttribute("username", user.getUsername());
				} else if (user.getStatus() == ApprovalStatus.PENDING) {
					response.put("success", false);
					response.put("message", "Your registration is pending teacher approval.");
				} else if (user.getStatus() == ApprovalStatus.REJECTED) {
					response.put("success", false);
					response.put("message", "Your registration was rejected. Please contact your teacher.");
				} else {
					response.put("success", false);
					response.put("message", "Invalid account status.");
				}
			} else {
				response.put("success", false);
				response.put("message", "Invalid student credentials or department");
			}
			return response;
		}

		if ("TEACHER".equalsIgnoreCase(role)) {
			String username = payload.get("usernameTeacher");
			String password = payload.get("passwordTeacher");
			User user = userRepository.findByUsername(username);
					if (user != null && user.getPassword().equals(password) && user.getRole().name().equals("TEACHER") && user.getDepartment() != null && user.getDepartment().equalsIgnoreCase(department)) {
				if (user.getStatus() == ApprovalStatus.APPROVED) {
					response.put("success", true);
					response.put("role", "TEACHER");
					response.put("message", "Login successful");
					session.setAttribute("username", user.getUsername());
					// Store department in session for teacher dashboard filtering
					session.setAttribute("department", user.getDepartment());
				} else {
					response.put("success", false);
					response.put("message", "Your registration is not approved.");
				}
			} else {
				response.put("success", false);
				response.put("message", "Invalid teacher credentials or department");
			}
			return response;
		}

		response.put("success", false);
		response.put("message", "Invalid role selected");
		return response;
	}
}
