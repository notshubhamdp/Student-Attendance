package com.AS.Student_Attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.AS.Student_Attendance.entity.Attendance;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Integer> {
	List<Attendance> findByUser(com.AS.Student_Attendance.entity.User user);
}
