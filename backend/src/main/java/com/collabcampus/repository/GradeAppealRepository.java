package com.collabcampus.repository;
import com.collabcampus.entity.GradeAppeal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GradeAppealRepository extends JpaRepository<GradeAppeal,Long> {
    List<GradeAppeal> findByStudentId(Long studentId);
    List<GradeAppeal> findByStatus(String status);
}
