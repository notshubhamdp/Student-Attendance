package com.AS.Student_Attendance.restcontroller;

import com.AS.Student_Attendance.entity.Attendance;
import com.AS.Student_Attendance.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class ReportsController {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@GetMapping("/reports")
	public String getAttendanceReports(Model model) {
		List<Attendance> attendanceRecords = attendanceRepository.findAll();
		attendanceRecords.sort((a, b) -> {
			String nameA = a.getUser().getFirstName() + " " + a.getUser().getLastName();
			String nameB = b.getUser().getFirstName() + " " + b.getUser().getLastName();
			return nameA.compareToIgnoreCase(nameB);
		});
		model.addAttribute("attendanceRecords", attendanceRecords);
		return "reports";
	}
}
