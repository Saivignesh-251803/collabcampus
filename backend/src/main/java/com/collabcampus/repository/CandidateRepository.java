package com.collabcampus.repository;
import com.collabcampus.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    List<Candidate> findByElectionId(Long electionId);
    boolean existsByElectionIdAndUserId(Long electionId, Long userId);
}
