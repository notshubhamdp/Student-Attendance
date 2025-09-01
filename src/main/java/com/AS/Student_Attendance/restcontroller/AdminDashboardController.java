package com.AS.Student_Attendance.restcontroller;

import com.AS.Student_Attendance.repository.StudentsRepository;
import com.AS.Student_Attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {
	// ...existing code...
	@Autowired
	private StudentsRepository studentsRepository;
	@Autowired
	private AttendanceRepository attendanceRepository;

	@GetMapping("/admin_dashboard")
	public String adminDashboard() {
		return "admin_dashboard";
	}

	@GetMapping("/students_admin")
	public String studentsPage(Model model) {
		model.addAttribute("students", studentsRepository.findAll());
		return "students";
	}


	@GetMapping("/admin/reports")
	public String reportsPage(Model model) {
		model.addAttribute("attendanceRecords", attendanceRepository.findAll());
		return "reports";
	}
}
