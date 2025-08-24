package com.AS.Student_Attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
// Make sure the User class exists at the specified package path.
// If the package is different, update the import accordingly.
// For example, if the correct package is com.AS.Student_Attendance.entity, use:
import com.AS.Student_Attendance.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	User findByUsername(String username);
	List<User> findByRoleAndStatus(com.AS.Student_Attendance.enumDto.Role role, com.AS.Student_Attendance.enumDto.ApprovalStatus status);
	List<User> findByRoleAndStatusAndDepartment(com.AS.Student_Attendance.enumDto.Role role, com.AS.Student_Attendance.enumDto.ApprovalStatus status, String department);
}
