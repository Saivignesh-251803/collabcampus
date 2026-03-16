package com.collabcampus.service;
import com.collabcampus.entity.*;
import com.collabcampus.enums.SubmissionStatus;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignRepo;
    private final SubmissionRepository subRepo;
    private final GradeAppealRepository appealRepo;
    private final CourseRepository courseRepo;
    private final UserRepository userRepo;

    public Assignment create(String title, String desc, int maxMarks, LocalDateTime deadline, Long courseId, Long facultyId) {
        Course c = courseRepo.findById(courseId).orElseThrow();
        User f = userRepo.findById(facultyId).orElseThrow();
        return assignRepo.save(Assignment.builder().title(title).description(desc)
            .maxMarks(maxMarks).deadline(deadline).course(c).faculty(f).build());
    }

    public List<Assignment> getByCourse(Long courseId) { return assignRepo.findByCourseId(courseId); }
    public List<Assignment> getByFaculty(Long facultyId) { return assignRepo.findByFacultyId(facultyId); }

    public Submission submit(Long assignmentId, Long studentId, String content) {
        if (subRepo.existsByAssignmentIdAndStudentId(assignmentId, studentId))
            throw new RuntimeException("Already submitted");
        Assignment a = assignRepo.findById(assignmentId).orElseThrow();
        if (a.getDeadline() != null && a.getDeadline().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Deadline has passed");
        User s = userRepo.findById(studentId).orElseThrow();
        return subRepo.save(Submission.builder().assignment(a).student(s).content(content)
            .status(SubmissionStatus.SUBMITTED).build());
    }

    public Submission grade(Long submissionId, int marks, String feedback) {
        Submission s = subRepo.findById(submissionId).orElseThrow();
        s.setMarksObtained(marks); s.setFeedback(feedback);
        s.setStatus(SubmissionStatus.GRADED); s.setGradedAt(LocalDateTime.now());
        return subRepo.save(s);
    }

    public GradeAppeal raiseAppeal(Long submissionId, Long studentId, String reason) {
        Submission s = subRepo.findById(submissionId).orElseThrow();
        s.setStatus(SubmissionStatus.APPEALED); subRepo.save(s);
        User st = userRepo.findById(studentId).orElseThrow();
        return appealRepo.save(GradeAppeal.builder().submission(s).student(st).reason(reason).status("PENDING").build());
    }

    public GradeAppeal resolveAppeal(Long appealId, String response, Integer newMarks) {
        GradeAppeal a = appealRepo.findById(appealId).orElseThrow();
        a.setFacultyResponse(response); a.setStatus("RESOLVED");
        a.setResolvedAt(LocalDateTime.now());
        if (newMarks != null) {
            a.getSubmission().setMarksObtained(newMarks);
            a.getSubmission().setStatus(SubmissionStatus.APPEAL_RESOLVED);
            subRepo.save(a.getSubmission());
        }
        return appealRepo.save(a);
    }

    public List<GradeAppeal> getPendingAppeals() { return appealRepo.findByStatus("PENDING"); }
    public List<Submission> getByAssignment(Long assignmentId) { return subRepo.findByAssignmentId(assignmentId); }
    public List<Submission> getByStudent(Long studentId) { return subRepo.findByStudentId(studentId); }
}
