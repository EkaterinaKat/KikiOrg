package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.katyshevtseva.kikiorg.core.sections.study.repo.PlanRepo;
import com.katyshevtseva.kikiorg.core.sections.study.repo.SubjMarkRepo;
import com.katyshevtseva.kikiorg.core.sections.study.repo.SubjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudyService {
    private final SubjMarkRepo markRepo;
    private final DateService dateService;
    private final SubjectRepo subjectRepo;
    private final PlanRepo planRepo;
    private final PmService pmService;

    public void save(Subject existing, String title, String desc) {
        if (existing == null) {
            existing = new Subject();
            existing.setArchived(false);
        }
        existing.setTitle(title.trim());
        existing.setDescription(GeneralUtils.trim(desc));
        subjectRepo.save(existing);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepo.findAllByOrderByTitle().stream()
                .sorted(Comparator.comparing(Subject::getArchived))
                .collect(Collectors.toList());
    }

    public List<Subject> getActiveSubjects() {
        return subjectRepo.findAllByArchivedFalseOrderByTitle();
    }

    public void saveMark(Subject subject, Date date, int minutes, String comment) {
        getMark(subject, date).ifPresent(markRepo::delete);

        SubjMark mark = new SubjMark(
                subject,
                dateService.createIfNotExistAndGetDateEntity(date),
                minutes,
                GeneralUtils.trim(comment));
        markRepo.save(mark);
        pmService.deleteMark(subject, date);
    }

    public Optional<SubjMark> getMark(Subject subject, Date date) {
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);
        List<SubjMark> marks = markRepo.findBySubjectAndDateEntity(subject, dateEntity);
        if (marks.size() > 1) {
            throw new RuntimeException();
        }
        return marks.isEmpty() ? Optional.empty() : Optional.ofNullable(marks.get(0));
    }

    public void archive(Subject subject) {
        subject.setArchived(!subject.getArchived());
        subjectRepo.save(subject);
    }

    @Transactional
    public void delete(Subject subject) {
        markRepo.deleteBySubject(subject);
        planRepo.deleteBySubject(subject);
        subjectRepo.delete(subject);
    }

    public void delete(StudyTableService.MarkToEdit mark) {
        if (mark.getMark() != null)
            markRepo.delete(mark.getMark());
    }
}
