package com.AS.Student_Attendance;

import com.AS.Student_Attendance.repository.*;

import jakarta.annotation.PostConstruct;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.AS.Student_Attendance.entity.Attendance;
// ...existing code...
import com.AS.Student_Attendance.entity.User;
import com.AS.Student_Attendance.entity.Courses;
import com.AS.Student_Attendance.enumDto.Role;
import com.AS.Student_Attendance.enumDto.ApprovalStatus;

@SpringBootApplication
public class StudentAttendanceApplication {
	@Autowired
	private UserRepository userRepository;
	// ...existing code...
	@Autowired
	private CoursesRepository coursesRepository;
	@Autowired
	private AttendanceRepository attendanceRepository;
	
	public static void main(String[] args) { SpringApplication.run(StudentAttendanceApplication.class, args); }
	
	@PostConstruct
	public void init() {
	User user = new User();
	user.setFirstName("John");
	user.setLastName("Doe");
	String uniqueSuffix = String.valueOf(System.currentTimeMillis());
	user.setUsername("john.doe" + uniqueSuffix);
	user.setPassword("password123");
	user.setEmail("john.doe" + uniqueSuffix + "@example.com");
	user.setRole(Role.ADMIN);
	user.setCreatedAt(new java.sql.Time(System.currentTimeMillis()));
	user.setStatus(ApprovalStatus.APPROVED);
	user.setRollNo(null);
	userRepository.save(user);


	// Create and save a sample course
	Courses course = new Courses();
	course.setCourseName("Sample Course");
	String uniqueCourseCode = "COURSE" + System.currentTimeMillis();
	course.setCourseCode(uniqueCourseCode);
	course.setCredits(3);
	coursesRepository.save(course);

	Attendance attendance = new Attendance();
	attendance.setUser(user);
	attendance.setClassEntity(course);
	attendance.setAttendanceDate(LocalDate.now());
	attendance.setPresent(true);
	attendance.setCreatedAt(new java.sql.Time(System.currentTimeMillis()));
	attendanceRepository.save(attendance);

		System.out.println("Student Attendance has Started");
	}
}
