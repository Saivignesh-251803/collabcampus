package com.collabcampus.repository;
import com.collabcampus.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ClubRepository extends JpaRepository<Club,Long> {
    List<Club> findByActiveTrue();
}
