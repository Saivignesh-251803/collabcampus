package com.collabcampus.repository;
import com.collabcampus.entity.AcademicEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface AcademicEventRepository extends JpaRepository<AcademicEvent,Long> {
    List<AcademicEvent> findByEventDateBetween(LocalDate start, LocalDate end);
    List<AcademicEvent> findAllByOrderByEventDateAsc();
}
