package com.AS.Student_Attendance.restcontroller;

import com.AS.Student_Attendance.entity.Attendance;
import com.AS.Student_Attendance.entity.User;
import com.AS.Student_Attendance.entity.Courses;
import com.AS.Student_Attendance.repository.AttendanceRepository;
import com.AS.Student_Attendance.repository.UserRepository;
import com.AS.Student_Attendance.repository.CoursesRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Controller
public class AttendanceController {
	@Autowired
	private AttendanceRepository attendanceRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CoursesRepository coursesRepository;

	// Teacher marks attendance for students
	@GetMapping("/attendance")
	public String attendancePage(HttpSession session, Model model) {
		String department = (String) session.getAttribute("department");
		if (department == null) {
			return "redirect:/login";
		}
		List<User> students = userRepository.findByRoleAndStatusAndDepartment(com.AS.Student_Attendance.enumDto.Role.STUDENT, com.AS.Student_Attendance.enumDto.ApprovalStatus.APPROVED, department);
		List<Courses> courses = coursesRepository.findAll();
		model.addAttribute("students", students);
		model.addAttribute("courses", courses);
		return "attendance";
	}

	@PostMapping("/attendance/mark")
	public String markAttendance(@RequestParam Integer userId, @RequestParam Integer courseId, @RequestParam Boolean present, HttpSession session) {
		User student = userRepository.findById(userId).orElse(null);
		Courses course = coursesRepository.findById(courseId).orElse(null);
		if (student == null || course == null) {
			return "redirect:/attendance";
		}
		Attendance attendance = new Attendance();
		attendance.setUser(student);
		attendance.setClassEntity(course);
		attendance.setAttendanceDate(LocalDate.now());
		attendance.setPresent(present);
		attendance.setCreatedAt(new Time(new Date().getTime()));
		attendanceRepository.save(attendance);
		return "redirect:/attendance";
	}

	// Student views their attendance
	@GetMapping("/student/attendance")
	public String studentAttendance(HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");
		if (username == null) {
			return "redirect:/login";
		}
		User student = userRepository.findByUsername(username);
		if (student == null) {
			return "redirect:/login";
		}
		List<Attendance> attendanceRecords = attendanceRepository.findByUser(student);
		model.addAttribute("attendanceRecords", attendanceRecords);
		return "student_attendance";
	}
}
