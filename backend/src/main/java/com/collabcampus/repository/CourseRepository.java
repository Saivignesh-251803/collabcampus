package com.collabcampus.repository;
import com.collabcampus.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByDepartmentId(Long deptId);
    List<Course> findByFacultyId(Long facultyId);
    List<Course> findByActive(boolean active);
}
