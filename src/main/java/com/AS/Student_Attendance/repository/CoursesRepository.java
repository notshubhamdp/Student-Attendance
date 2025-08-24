package com.AS.Student_Attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AS.Student_Attendance.entity.Courses;

public interface CoursesRepository extends JpaRepository<Courses, Integer> {
}
