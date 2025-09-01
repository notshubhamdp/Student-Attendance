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
		String role = (String) session.getAttribute("role");
		if ("ADMIN".equalsIgnoreCase(role)) {
			logger.warn("[ATTENDANCE] Admin tried to access attendance marking. Redirecting to admin dashboard.");
			return "redirect:/admin_dashboard";
		}
		String department = (String) session.getAttribute("department");
		logger.info("[ATTENDANCE] Department from session: {}", department);
		if (department == null) {
			logger.warn("[ATTENDANCE] Department is null in session. Redirecting to login.");
			return "redirect:/login";
		}
	List<com.AS.Student_Attendance.enumDto.Role> allowedRoles = java.util.Arrays.asList(com.AS.Student_Attendance.enumDto.Role.STUDENT, com.AS.Student_Attendance.enumDto.Role.TEACHER);
	List<User> students = userRepository.findByRoleInAndStatusAndDepartment(allowedRoles, com.AS.Student_Attendance.enumDto.ApprovalStatus.APPROVED, department);
		logger.info("[ATTENDANCE] Approved students found: {}", students.size());
		List<Courses> courses = coursesRepository.findAll();
		model.addAttribute("students", students);
		model.addAttribute("courses", courses);
		return "attendance";
	}

	@PostMapping("/attendance/mark")
	public String markAttendance(HttpSession session, @RequestParam(required = false) Integer mark_ , @RequestParam Map<String, String> params) {
		logger.info("[DEBUG] Mark Attendance called. Params: {}", params);
		String role = (String) session.getAttribute("role");
		if ("ADMIN".equalsIgnoreCase(role)) {
			logger.warn("[ATTENDANCE] Admin tried to mark attendance. Redirecting to admin dashboard.");
			return "redirect:/admin_dashboard";
		}
		Integer userId = null;
		for (String key : params.keySet()) {
			if (key.startsWith("mark_")) {
				try {
					userId = Integer.valueOf(params.get(key));
				} catch (Exception e) {
					logger.error("[ERROR] Failed to parse userId from key {}: {}", key, e.getMessage());
				}
				break;
			}
		}
		// Fallback: if no mark_ param found, try 'userId' param
		if (userId == null && params.containsKey("userId")) {
			try {
				userId = Integer.valueOf(params.get("userId"));
				logger.warn("[FALLBACK] Using userId param from form: {}", userId);
			} catch (Exception e) {
				logger.error("[ERROR] Failed to parse fallback userId: {}", e.getMessage());
			}
		}
		if (userId == null) {
			logger.error("[ERROR] userId is null. Params: {}", params);
			return "redirect:/teacher/dashboard";
		}
		String courseKey = "courseId_" + userId;
		String statusKey = "status_" + userId;
		Integer courseId = null;
		String statusRaw = null;
		// Try suffixed keys first
		try {
			if (params.containsKey(courseKey)) {
				courseId = Integer.valueOf(params.get(courseKey));
			}
		} catch (Exception e) {
			logger.error("[ERROR] Failed to parse courseId: {}", e.getMessage());
		}
		if (params.containsKey(statusKey)) {
			statusRaw = params.get(statusKey);
		}
		// Fallback: try unsuffixed keys (per-row form)
		if (courseId == null && params.containsKey("courseId")) {
			try {
				courseId = Integer.valueOf(params.get("courseId"));
			} catch (Exception e) {
				logger.error("[ERROR] Failed to parse fallback courseId: {}", e.getMessage());
			}
		}
		if (statusRaw == null && params.containsKey("status")) {
			statusRaw = params.get("status");
		}
		AttendanceStatus status = null;
		if ("true".equalsIgnoreCase(statusRaw) || "present".equalsIgnoreCase(statusRaw)) {
			status = AttendanceStatus.PRESENT;
		} else if ("false".equalsIgnoreCase(statusRaw) || "absent".equalsIgnoreCase(statusRaw)) {
			status = AttendanceStatus.ABSENT;
		} else if ("late".equalsIgnoreCase(statusRaw)) {
			status = AttendanceStatus.LATE;
		}
		logger.info("[DEBUG] Attendance marking: userId={}, courseId={}, statusRaw={}, status={}", userId, courseId, statusRaw, status);
		User student = userRepository.findById(userId).orElse(null);
		Courses course = (courseId != null) ? coursesRepository.findById(courseId).orElse(null) : null;
		if (student == null || course == null || status == null) {
			logger.error("[ERROR] Attendance marking failed: student={}, course={}, status={}", student, course, status);
			session.setAttribute("attendanceError", "Attendance marking failed. Please check student, course, and status.");
			return "redirect:/reports";
		}
		LocalDate today = LocalDate.now();
		boolean alreadyMarked = false;
		try {
			alreadyMarked = attendanceRepository.existsByUserUserIdAndClassEntityCourseIdAndAttendanceDate(
				student.getUserId().longValue(), course.getCourseId().longValue(), today);
		} catch (Exception e) {
			logger.error("[ERROR] DB check for alreadyMarked failed: {}", e.getMessage());
		}
		if (alreadyMarked) {
			session.setAttribute("attendanceError", "Attendance already marked for this student and course today.");
			logger.warn("[WARN] Duplicate attendance for userId={}, courseId={}, date={}", userId, courseId, today);
			return "redirect:/reports";
		}
		try {
			Attendance attendance = new Attendance();
			attendance.setUser(student);
			attendance.setClassEntity(course);
			attendance.setAttendanceDate(today);
			attendance.setStatus(status);
			attendance.setCreatedAt(new Time(new Date().getTime()));
			attendanceRepository.save(attendance);
			logger.info("[SUCCESS] Attendance saved for userId={}, status={}", userId, status);
			session.setAttribute("attendanceMsg", "Attendance marked for " + student.getFirstName() + " as " + status.name().toLowerCase() + ".");
			session.removeAttribute("attendanceError");
		} catch (Exception e) {
			logger.error("[ERROR] Failed to save attendance: {}", e.getMessage());
			session.setAttribute("attendanceError", "Error saving attendance: " + e.getMessage());
		}
		return "redirect:/reports";
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
