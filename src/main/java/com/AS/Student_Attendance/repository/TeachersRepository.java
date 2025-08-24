package com.AS.Student_Attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.AS.Student_Attendance.entity.Teachers;

import jakarta.persistence.criteria.CriteriaBuilder.In;

public interface TeachersRepository extends JpaRepository<Teachers, Integer> {
}
