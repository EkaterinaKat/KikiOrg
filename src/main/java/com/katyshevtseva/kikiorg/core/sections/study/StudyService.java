package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.kikiorg.core.date.DateEntity;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjValue;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.katyshevtseva.kikiorg.core.sections.study.repo.SubjMarkRepo;
import com.katyshevtseva.kikiorg.core.sections.study.repo.SubjValueRepo;
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
    private final SubjValueRepo valueRepo;

    public void save(Subject existing, String title, String desc) {
        if (existing == null) {
            existing = new Subject();
            existing.setArchived(false);
            existing.setHidden(false);
        }
        existing.setTitle(title.trim());
        existing.setDescription(GeneralUtils.trim(desc));
        subjectRepo.save(existing);
    }

    public void save(Subject subject, SubjValue existing, String title, String desc, String color) {
        if (existing == null) {
            existing = new SubjValue();
            existing.setDefaultValue(false);
        }
        existing.setSubject(subject);
        existing.setTitle(title.trim());
        existing.setDescription(GeneralUtils.trim(desc));
        existing.setColor(GeneralUtils.trim(color));
        valueRepo.save(existing);
    }

    public void makeDefault(SubjValue value) {
        List<SubjValue> values = valueRepo.findAllBySubject(value.getSubject());
        values.forEach(val -> {
            val.setDefaultValue(false);
            valueRepo.save(val);
        });

        if (!value.isDefaultValue()) { //чтобы можно было отменять выбор уже выбранного без выбора нового
            value.setDefaultValue(true);
            valueRepo.save(value);
        }
    }

    public List<Subject> getAllSubjects() {
        return subjectRepo.findAllByOrderByTitle().stream()
                .sorted(Comparator.comparing(Subject::getHidden))
                .sorted(Comparator.comparing(Subject::getArchived))
                .collect(Collectors.toList());
    }

    public List<Subject> getActiveSubjects() {
        return subjectRepo.findAllByArchivedFalseOrderByTitle().stream()
                .sorted(Comparator.comparing(Subject::getHidden))
                .collect(Collectors.toList());
    }

    public List<Subject> getActiveNotHiddenSubjects() {
        return subjectRepo.findAllByArchivedFalseAndHiddenFalseOrderByTitle();
    }

    public List<Subject> getActiveHiddenSubjects() {
        return subjectRepo.findAllByArchivedFalseAndHiddenTrueOrderByTitle();
    }


    public List<Subject> getSubjectsWithNumericValues() {
        return getActiveSubjects().stream().filter(StudyUtils::hasNumericValues).collect(Collectors.toList());
    }

    public void saveMark(Subject subject, Date date, SubjValue value, String comment) {
        if (value != null) {
            validateSubjectAndValue(subject, value);
        }

        getMark(subject, date).ifPresent(markRepo::delete);

        if (value != null || !GeneralUtils.isEmpty(comment)) {
            SubjMark mark = new SubjMark(
                    subject,
                    dateService.createIfNotExistAndGetDateEntity(date),
                    value,
                    GeneralUtils.trim(comment));
            markRepo.save(mark);
        }
    }

    public Optional<SubjMark> getMark(Subject subject, Date date) {
        DateEntity dateEntity = dateService.createIfNotExistAndGetDateEntity(date);
        List<SubjMark> marks = markRepo.findBySubjectAndDateEntity(subject, dateEntity);
        if (marks.size() > 1) {
            throw new RuntimeException();
        }
        return marks.isEmpty() ? Optional.empty() : Optional.ofNullable(marks.get(0));
    }

    private void validateSubjectAndValue(Subject subject, SubjValue value) {
        if (!subject.getValues().contains(value)) {
            throw new RuntimeException();
        }
    }

    public void archive(Subject subject) {
        subject.setArchived(!subject.getArchived());
        subjectRepo.save(subject);
    }

    public void hide(Subject subject) {
        subject.setHidden(!subject.getHidden());
        subjectRepo.save(subject);
    }

    @Transactional
    public void delete(Subject subject) {
        markRepo.deleteBySubject(subject);
        valueRepo.deleteBySubject(subject);
        subjectRepo.delete(subject);
    }

    public void delete(StudyTableService.MarkToEdit mark) {
        if (mark.getMark() != null)
            markRepo.delete(mark.getMark());
    }
}
