package com.collabcampus.service;
import com.collabcampus.entity.*;
import com.collabcampus.enums.EventType;
import com.collabcampus.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service @RequiredArgsConstructor
public class CalendarService {
    private final AcademicEventRepository eventRepo;
    private final UserRepository userRepo;

    public List<AcademicEvent> getAll() { return eventRepo.findAllByOrderByEventDateAsc(); }

    public List<AcademicEvent> getByMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return eventRepo.findByEventDateBetween(start, end);
    }

    public AcademicEvent create(String title, String desc, String type, LocalDate date, LocalDate endDate, Long createdById) {
        User creator = userRepo.findById(createdById).orElseThrow();
        return eventRepo.save(AcademicEvent.builder().title(title).description(desc)
            .type(EventType.valueOf(type)).eventDate(date).endDate(endDate)
            .createdBy(creator).build());
    }

    public void delete(Long id) { eventRepo.deleteById(id); }
}
