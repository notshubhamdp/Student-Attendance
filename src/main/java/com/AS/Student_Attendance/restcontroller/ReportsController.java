package com.AS.Student_Attendance.restcontroller;

import com.AS.Student_Attendance.entity.Attendance;
import com.AS.Student_Attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ReportsController {

	private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);
	@Autowired
	private AttendanceRepository attendanceRepository;

	@GetMapping("/admin/reports")
	public String getAttendanceReports(Model model) {
		List<Attendance> attendanceRecords = attendanceRepository.findAll();
		for (Attendance a : attendanceRecords) {
			logger.info("[REPORTS DEBUG] AttendanceId={}, User={}, Status={}, Date={}", a.getAttendanceId(), a.getUser().getUsername(), a.getStatus(), a.getAttendanceDate());
		}
		attendanceRecords.sort((a, b) -> {
			String nameA = a.getUser().getFirstName() + " " + a.getUser().getLastName();
			String nameB = b.getUser().getFirstName() + " " + b.getUser().getLastName();
			return nameA.compareToIgnoreCase(nameB);
		});
		model.addAttribute("attendanceRecords", attendanceRecords);
		return "reports";
	}
}
