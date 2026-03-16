package com.collabcampus.service;
import com.collabcampus.entity.*;
import com.collabcampus.enums.NoticePriority;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepo;
    private final UserRepository userRepo;
    private final DepartmentRepository deptRepo;

    public List<Notice> getAll() { return noticeRepo.findByActiveTrueOrderByCreatedAtDesc(); }
    public List<Notice> getByDept(Long deptId) { return noticeRepo.findByDepartmentIdAndActiveTrue(deptId); }
    public List<Notice> getPendingApproval() { return noticeRepo.findByApprovedFalse(); }

    public Notice create(String title, String content, String priority, Long postedById, Long deptId) {
        User postedBy = userRepo.findById(postedById).orElseThrow();
        NoticePriority p = NoticePriority.valueOf(priority.toUpperCase());
        boolean autoApproved = postedBy.getRole().name().equals("ADMIN") || p != NoticePriority.URGENT;
        Notice.NoticeBuilder b = Notice.builder().title(title).content(content)
            .priority(p).postedBy(postedBy).active(true).approved(autoApproved);
        if (deptId != null) b.department(deptRepo.findById(deptId).orElse(null));
        return noticeRepo.save(b.build());
    }

    public Notice approve(Long id) {
        Notice n = noticeRepo.findById(id).orElseThrow();
        n.setApproved(true); return noticeRepo.save(n);
    }

    public void delete(Long id) {
        Notice n = noticeRepo.findById(id).orElseThrow();
        n.setActive(false); noticeRepo.save(n);
    }
}
