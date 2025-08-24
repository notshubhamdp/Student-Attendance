package com.AS.Student_Attendance.mapper;

import com.AS.Student_Attendance.dto.StudentsDto;
import com.AS.Student_Attendance.entity.Students;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-24T09:53:58+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Students toStudentEntity(StudentsDto studentsDto) {
        if ( studentsDto == null ) {
            return null;
        }

        Students students = new Students();

        students.setCreatedAt( studentsDto.getCreatedAt() );
        students.setDepartment( studentsDto.getDepartment() );
        students.setEmail( studentsDto.getEmail() );
        students.setFirstName( studentsDto.getFirstName() );
        students.setLastName( studentsDto.getLastName() );
        students.setPhone( studentsDto.getPhone() );
        students.setRollNo( studentsDto.getRollNo() );
        students.setStudentId( studentsDto.getStudentId() );
        students.setYear( studentsDto.getYear() );

        return students;
    }

    @Override
    public StudentsDto toStudentsDto(Students students) {
        if ( students == null ) {
            return null;
        }

        StudentsDto studentsDto = new StudentsDto();

        studentsDto.setCreatedAt( students.getCreatedAt() );
        studentsDto.setDepartment( students.getDepartment() );
        studentsDto.setEmail( students.getEmail() );
        studentsDto.setFirstName( students.getFirstName() );
        studentsDto.setLastName( students.getLastName() );
        studentsDto.setPhone( students.getPhone() );
        studentsDto.setRollNo( students.getRollNo() );
        studentsDto.setStudentId( students.getStudentId() );
        studentsDto.setYear( students.getYear() );

        return studentsDto;
    }
}
