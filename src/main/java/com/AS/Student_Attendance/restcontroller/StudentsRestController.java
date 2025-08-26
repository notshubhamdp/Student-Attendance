package com.AS.Student_Attendance.restcontroller;

import com.AS.Student_Attendance.entity.Students;
import com.AS.Student_Attendance.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentsRestController {
    @Autowired
    private StudentsRepository studentsRepository;

    @GetMapping
    public List<Students> getAllStudents() {
        return studentsRepository.findAll();
    }

    @GetMapping("/{id}")
    public Students getStudentById(@PathVariable Integer id) {
        return studentsRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Object createStudent(@RequestBody Students student) {
        if (studentsRepository.existsByFirstNameAndLastName(student.getFirstName(), student.getLastName())) {
            return java.util.Collections.singletonMap("error", "A student with this name already exists.");
        }
        return studentsRepository.save(student);
    }

    @PutMapping("/{id}")
    public Students updateStudent(@PathVariable Integer id, @RequestBody Students student) {
        student.setStudentId(id);
        return studentsRepository.save(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id) {
        studentsRepository.deleteById(id);
    }
}
