package com.AS.Student_Attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.AS.Student_Attendance.entity.Attendance;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
	List<Attendance> findByUser(com.AS.Student_Attendance.entity.User user);

	List<Attendance> findByUserAndClassEntityCourseId(com.AS.Student_Attendance.entity.User user, Long courseId);

	boolean existsByUserUserIdAndClassEntityCourseIdAndAttendanceDate(Long userId, Long courseId, java.time.LocalDate date);
}
