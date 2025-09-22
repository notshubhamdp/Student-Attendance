package com.AS.Student_Attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.AS.Student_Attendance.entity.Attendance;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
	List<Attendance> findByUser(com.AS.Student_Attendance.entity.User user);

<<<<<<< HEAD
	List<Attendance> findByUserAndClassEntityCourseId(com.AS.Student_Attendance.entity.User user, Long courseId);

	boolean existsByUserUserIdAndClassEntityCourseIdAndAttendanceDate(Long userId, Long courseId, java.time.LocalDate date);
=======
	List<Attendance> findByUserAndCourseCourseId(com.AS.Student_Attendance.entity.User user, Long courseId);

	boolean existsByUserUserIdAndCourseCourseIdAndAttendanceDate(Long userId, Long courseId, java.time.LocalDate date);

    List<Attendance> findByUser_UserId(Integer userId);
>>>>>>> 7c9ad0ca1501f4f3551d1fc2b055a5056b42f060
}
