package com.collabcampus.service;
import com.collabcampus.entity.*;
import com.collabcampus.enums.ElectionStatus;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ElectionService {
    private final ElectionRepository electionRepo;
    private final CandidateRepository candidateRepo;
    private final VoteRepository voteRepo;
    private final UserRepository userRepo;

    public List<Election> getAll() { return electionRepo.findAllByOrderByCreatedAtDesc(); }
    public Election getById(Long id) { return electionRepo.findById(id).orElseThrow(); }

    public Election create(String title, String desc, LocalDateTime nomStart, LocalDateTime nomEnd,
                           LocalDateTime voteStart, LocalDateTime voteEnd, Long createdById) {
        User creator = userRepo.findById(createdById).orElseThrow();
        return electionRepo.save(Election.builder().title(title).description(desc)
            .nominationStart(nomStart).nominationEnd(nomEnd).votingStart(voteStart).votingEnd(voteEnd)
            .status(ElectionStatus.UPCOMING).createdBy(creator).build());
    }

    public Candidate nominate(Long electionId, Long userId, String manifesto) {
        if (candidateRepo.existsByElectionIdAndUserId(electionId, userId))
            throw new RuntimeException("Already nominated");
        Election e = electionRepo.findById(electionId).orElseThrow();
        User u = userRepo.findById(userId).orElseThrow();
        return candidateRepo.save(Candidate.builder().election(e).user(u).manifesto(manifesto).approved(true).build());
    }

    public Vote castVote(Long electionId, Long candidateId, Long voterId) {
        if (voteRepo.existsByVoterIdAndElectionId(voterId, electionId))
            throw new RuntimeException("You have already voted");
        Election e = electionRepo.findById(electionId).orElseThrow();
        if (e.getStatus() != ElectionStatus.VOTING_OPEN) throw new RuntimeException("Voting is not open");
        Candidate c = candidateRepo.findById(candidateId).orElseThrow();
        User voter = userRepo.findById(voterId).orElseThrow();
        return voteRepo.save(Vote.builder().election(e).candidate(c).voter(voter).build());
    }

    public Map<String, Object> getResults(Long electionId) {
        List<Candidate> candidates = candidateRepo.findByElectionId(electionId);
        List<Map<String,Object>> results = candidates.stream().map(c -> {
            Map<String,Object> r = new HashMap<>();
            r.put("candidateId", c.getId());
            r.put("name", c.getUser().getName());
            r.put("manifesto", c.getManifesto());
            r.put("votes", voteRepo.countByCandidateId(c.getId()));
            return r;
        }).sorted((a,b) -> Long.compare((Long)b.get("votes"), (Long)a.get("votes")))
          .collect(Collectors.toList());
        return Map.of("electionId", electionId, "results", results);
    }

    public Election updateStatus(Long id, String status) {
        Election e = electionRepo.findById(id).orElseThrow();
        e.setStatus(ElectionStatus.valueOf(status));
        return electionRepo.save(e);
    }
}
