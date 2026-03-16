package com.collabcampus.service;
import com.collabcampus.entity.*;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepo;
    private final DepartmentRepository deptRepo;
    private final UserRepository userRepo;
    private final EnrollmentRepository enrollRepo;

    public List<Course> getAll() { return courseRepo.findByActive(true); }
    public List<Course> getByDept(Long deptId) { return courseRepo.findByDepartmentId(deptId); }
    public List<Course> getByFaculty(Long facultyId) { return courseRepo.findByFacultyId(facultyId); }
    public Course getById(Long id) { return courseRepo.findById(id).orElseThrow(); }

    public Course create(String name, String code, String desc, int credits, int maxSeats, Long deptId, Long facultyId) {
        Department dept = deptRepo.findById(deptId).orElseThrow();
        User faculty = userRepo.findById(facultyId).orElseThrow();
        return courseRepo.save(Course.builder().name(name).code(code).description(desc)
            .credits(credits).maxSeats(maxSeats).department(dept).faculty(faculty).active(true).build());
    }

    public void enroll(Long studentId, Long courseId) {
        if (enrollRepo.existsByStudentIdAndCourseId(studentId, courseId))
            throw new RuntimeException("Already enrolled");
        long count = enrollRepo.countByCourseId(courseId);
        Course c = getById(courseId);
        if (count >= c.getMaxSeats()) throw new RuntimeException("Course is full");
        User student = userRepo.findById(studentId).orElseThrow();
        enrollRepo.save(Enrollment.builder().student(student).course(c).build());
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollRepo.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollRepo.findByCourseId(courseId);
    }
}
