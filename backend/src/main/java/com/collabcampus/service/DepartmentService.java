package com.collabcampus.service;
import com.collabcampus.entity.Department;
import com.collabcampus.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository deptRepo;
    public List<Department> getAll() { return deptRepo.findAll(); }
    public Department getById(Long id) { return deptRepo.findById(id).orElseThrow(); }
    public Department create(String name, String code, String description, String hodName) {
        if (deptRepo.existsByName(name)) throw new RuntimeException("Department already exists");
        return deptRepo.save(Department.builder().name(name).code(code)
            .description(description).hodName(hodName).active(true).build());
    }
    public Department update(Long id, String name, String description, String hodName) {
        Department d = getById(id);
        if (name != null) d.setName(name);
        if (description != null) d.setDescription(description);
        if (hodName != null) d.setHodName(hodName);
        return deptRepo.save(d);
    }
    public void delete(Long id) { deptRepo.deleteById(id); }
}
