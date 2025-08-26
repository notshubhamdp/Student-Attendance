package com.AS.Student_Attendance.restcontroller;

import com.AS.Student_Attendance.entity.Attendance;
import com.AS.Student_Attendance.entity.User;
import com.AS.Student_Attendance.entity.Courses;
import com.AS.Student_Attendance.enumDto.AttendanceStatus;
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
import java.util.Map;


@Controller
public class AttendanceController {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AttendanceController.class);

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
		logger.info("[ATTENDANCE] Department from session: {}", department);
		if (department == null) {
			logger.warn("[ATTENDANCE] Department is null in session. Redirecting to login.");
			return "redirect:/login";
		}
		List<User> students = userRepository.findByRoleAndStatusAndDepartment(com.AS.Student_Attendance.enumDto.Role.STUDENT, com.AS.Student_Attendance.enumDto.ApprovalStatus.APPROVED, department);
		logger.info("[ATTENDANCE] Approved students found: {}", students.size());
		List<Courses> courses = coursesRepository.findAll();
		model.addAttribute("students", students);
		model.addAttribute("courses", courses);
		return "attendance";
	}

	@PostMapping("/attendance/mark")
	public String markAttendance(HttpSession session, @RequestParam(required = false) Integer mark_ , @RequestParam Map<String, String> params) {
		// Find which student was marked
		Integer userId = null;
		for (String key : params.keySet()) {
			if (key.startsWith("mark_")) {
				userId = Integer.valueOf(params.get(key));
				break;
			}
		}
		if (userId == null) {
			return "redirect:/teacher/dashboard";
		}
		String courseKey = "courseId_" + userId;
		String statusKey = "status_" + userId;
		Integer courseId = params.containsKey(courseKey) ? Integer.valueOf(params.get(courseKey)) : null;
		String statusRaw = params.get(statusKey);
		AttendanceStatus status = null;
		if ("true".equalsIgnoreCase(statusRaw) || "present".equalsIgnoreCase(statusRaw)) {
			status = AttendanceStatus.PRESENT;
		} else if ("false".equalsIgnoreCase(statusRaw) || "absent".equalsIgnoreCase(statusRaw)) {
			status = AttendanceStatus.ABSENT;
		} else if ("late".equalsIgnoreCase(statusRaw)) {
			status = AttendanceStatus.LATE;
		}
		logger.info("Attendance marking: userId={}, courseId={}, statusRaw={}, status={}", userId, courseId, statusRaw, status);
		User student = userRepository.findById(userId).orElse(null);
		Courses course = (courseId != null) ? coursesRepository.findById(courseId).orElse(null) : null;
		if (student == null || course == null || status == null) {
			logger.warn("Attendance marking failed: student={}, course={}, status={}", student, course, status);
			return "redirect:/teacher/dashboard";
		}
		// Check for duplicate entry (same student, course, date)
		LocalDate today = LocalDate.now();
		boolean alreadyMarked = attendanceRepository.existsByUserUserIdAndClassEntityCourseIdAndAttendanceDate(
			student.getUserId().longValue(), course.getCourseId().longValue(), today);
		if (alreadyMarked) {
			session.setAttribute("attendanceError", "Attendance already marked for this student and course today.");
			return "redirect:/attendance";
		}
		Attendance attendance = new Attendance();
		attendance.setUser(student);
		attendance.setClassEntity(course);
		attendance.setAttendanceDate(today);
		logger.info("[ATTENDANCE DEBUG] Saving attendance: userId={}, courseId={}, status={}, statusRaw={}", userId, courseId, status, statusRaw);
		attendance.setStatus(status);
		attendance.setCreatedAt(new Time(new Date().getTime()));
		attendanceRepository.save(attendance);
		logger.info("Attendance saved for userId={}, status={}", userId, status);
		session.setAttribute("attendanceMsg", "Attendance marked for " + student.getFirstName() + " as " + status.name().toLowerCase() + ".");
		session.removeAttribute("attendanceError");
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
		logger.info("[DEBUG] Fetching attendance for student: {} ({} records)", student.getUsername(), attendanceRecords.size());
		for (Attendance rec : attendanceRecords) {
			logger.info("[DEBUG] Attendance: date={}, course={}, status={}", rec.getAttendanceDate(), rec.getClassEntity().getCourseName(), rec.getStatus());
		}
		model.addAttribute("attendanceRecords", attendanceRecords);
		return "student_attendance";
	}
}
