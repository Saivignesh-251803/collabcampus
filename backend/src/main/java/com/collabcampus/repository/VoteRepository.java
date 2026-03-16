package com.collabcampus.repository;
import com.collabcampus.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface VoteRepository extends JpaRepository<Vote,Long> {
    boolean existsByVoterIdAndElectionId(Long voterId, Long electionId);
    List<Vote> findByCandidateId(Long candidateId);
    long countByCandidateId(Long candidateId);
}
