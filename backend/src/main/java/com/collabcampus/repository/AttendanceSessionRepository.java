package com.collabcampus.repository;
import com.collabcampus.entity.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession,Long> {
    Optional<AttendanceSession> findByQrTokenAndActiveTrue(String qrToken);
    List<AttendanceSession> findByCourseId(Long courseId);
    List<AttendanceSession> findByFacultyId(Long facultyId);
}
