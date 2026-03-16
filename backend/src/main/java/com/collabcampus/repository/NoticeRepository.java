package com.collabcampus.repository;
import com.collabcampus.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface NoticeRepository extends JpaRepository<Notice,Long> {
    List<Notice> findByActiveTrueOrderByCreatedAtDesc();
    List<Notice> findByDepartmentIdAndActiveTrue(Long deptId);
    List<Notice> findByApprovedFalse();
}
