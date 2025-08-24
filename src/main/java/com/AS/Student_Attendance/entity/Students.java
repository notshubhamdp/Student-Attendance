package com.AS.Student_Attendance.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Students {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
   @Column(name = "student_id")
   private Integer studentId;

   @Column(name = "roll_no", nullable = false, unique = true)
   private String rollNo;

   @Column(name = "first_name", nullable = false)
   private String firstName;

   @Column(name = "last_name", nullable = false)
   private String lastName;

   @Column(name = "email", nullable = false, unique = true)
   private String email;

   @Column(name = "phone")
   private String phone;

   @Column(name = "department")
   private String department;

   @Column(name = "year")
   private Integer year;

   @Column(name = "created_at", nullable = false)
   private Timestamp createdAt;

    // Getters and Setters
}
