package com.AS.Student_Attendance.restcontroller;

import com.AS.Student_Attendance.entity.Teachers;
import com.AS.Student_Attendance.repository.TeachersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeachersRestController {
    @Autowired
    private TeachersRepository teachersRepository;

    @GetMapping
    public List<Teachers> getAllTeachers() {
        return teachersRepository.findAll();
    }

    @GetMapping("/{id}")
    public Teachers getTeacherById(@PathVariable Integer id) {
        return teachersRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Teachers createTeacher(@RequestBody Teachers teacher) {
        return teachersRepository.save(teacher);
    }

    @PutMapping("/{id}")
    public Teachers updateTeacher(@PathVariable Integer id, @RequestBody Teachers teacher) {
        teacher.setTeacherId(id);
        return teachersRepository.save(teacher);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Integer id) {
        teachersRepository.deleteById(id);
    }
}
