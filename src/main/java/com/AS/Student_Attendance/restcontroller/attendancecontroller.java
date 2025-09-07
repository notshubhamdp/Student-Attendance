package com.AS.Student_Attendance.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.AS.Student_Attendance.entity.Attendance;
import com.AS.Student_Attendance.repository.AttendanceRepository;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@PostMapping("/mark")
	public Attendance markAttendance(@RequestBody Attendance attendance) {
		return attendanceRepository.save(attendance);
	}

	@GetMapping("/all")
	public List<Attendance> getAllAttendance() {
		return attendanceRepository.findAll();
	}
}

