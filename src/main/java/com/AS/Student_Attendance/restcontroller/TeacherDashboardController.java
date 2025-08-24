package com.AS.Student_Attendance.restcontroller;

import com.AS.Student_Attendance.entity.User;
import com.AS.Student_Attendance.enumDto.ApprovalStatus;
import com.AS.Student_Attendance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.AS.Student_Attendance.enumDto.Role;

@Controller
@RequestMapping("/teacher")
public class TeacherDashboardController {
	private static final Logger logger = LoggerFactory.getLogger(TeacherDashboardController.class);

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/dashboard")
	public String teacherDashboard(HttpSession session, Model model) {
		logger.info("Teacher dashboard endpoint triggered");
		String department = (String) session.getAttribute("department");
		logger.info("Teacher dashboard department from session: {}", department);
		if (department == null || department.isEmpty()) {
			model.addAttribute("pendingStudents", List.of());
			model.addAttribute("approvedStudents", List.of());
		} else {
			List<User> pendingStudents = userRepository.findByRoleAndStatusAndDepartment(Role.STUDENT, ApprovalStatus.PENDING, department);
			logger.info("Pending students found: {}", pendingStudents.size());
			model.addAttribute("pendingStudents", pendingStudents);
			List<User> approvedStudents = userRepository.findByRoleAndStatusAndDepartment(Role.STUDENT, ApprovalStatus.APPROVED, department);
			logger.info("Approved students found: {}", approvedStudents.size());
			model.addAttribute("approvedStudents", approvedStudents);
		}
		return "teacher_dashboard";
	}

	@PostMapping("/approve")
public String approveStudent(@RequestParam Integer userId, @RequestParam String rollNo, HttpSession session, Model model) {
	User student = userRepository.findById(userId).orElse(null);
	if (student != null) {
		if (rollNo == null || rollNo.trim().isEmpty()) {
			// If rollNo is missing, show error and do not approve
			session.setAttribute("approveError", "Roll No. is required to approve student.");
			return "redirect:/teacher/dashboard";
		}
		student.setStatus(ApprovalStatus.APPROVED);
		student.setRollNo(rollNo);
		userRepository.save(student);
	}
	session.removeAttribute("approveError");
	return "redirect:/teacher/dashboard";
	}

	@PostMapping("/reject")
	public String rejectStudent(@RequestParam Integer userId) {
		User student = userRepository.findById(userId).orElse(null);
		if (student != null) {
			student.setStatus(ApprovalStatus.REJECTED);
			userRepository.save(student);
		}
		return "redirect:/teacher/dashboard";
	}
}
