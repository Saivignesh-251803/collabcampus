package com.collabcampus.repository;
import com.collabcampus.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    List<Assignment> findByCourseId(Long courseId);
    List<Assignment> findByFacultyId(Long facultyId);
}
