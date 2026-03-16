package com.collabcampus.repository;
import com.collabcampus.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord,Long> {
    List<AttendanceRecord> findByStudentIdAndCourseId(Long studentId, Long courseId);
    List<AttendanceRecord> findBySessionId(Long sessionId);
    boolean existsByStudentIdAndSessionId(Long studentId, Long sessionId);
    long countByStudentIdAndCourseId(Long studentId, Long courseId);
}
