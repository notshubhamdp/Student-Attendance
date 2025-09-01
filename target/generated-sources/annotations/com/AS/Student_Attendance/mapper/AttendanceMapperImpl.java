package com.AS.Student_Attendance.mapper;

import com.AS.Student_Attendance.dto.AttendanceDto;
import com.AS.Student_Attendance.entity.Attendance;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T12:46:05+0530",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.50.v20250729-0351, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class AttendanceMapperImpl implements AttendanceMapper {

    @Override
    public Attendance toAttendanceEntity(AttendanceDto attendanceDto) {
        if ( attendanceDto == null ) {
            return null;
        }

        Attendance attendance = new Attendance();

        attendance.setAttendanceDate( attendanceDto.getAttendanceDate() );
        attendance.setAttendanceId( attendanceDto.getAttendanceId() );
        attendance.setClassEntity( attendanceDto.getClassEntity() );
        attendance.setCreatedAt( attendanceDto.getCreatedAt() );
        attendance.setUser( attendanceDto.getUser() );

        return attendance;
    }

    @Override
    public AttendanceDto toAttendanceDto(Attendance attendance) {
        if ( attendance == null ) {
            return null;
        }

        AttendanceDto attendanceDto = new AttendanceDto();

        attendanceDto.setAttendanceDate( attendance.getAttendanceDate() );
        attendanceDto.setAttendanceId( attendance.getAttendanceId() );
        attendanceDto.setClassEntity( attendance.getClassEntity() );
        attendanceDto.setCreatedAt( attendance.getCreatedAt() );
        attendanceDto.setUser( attendance.getUser() );

        return attendanceDto;
    }
}
