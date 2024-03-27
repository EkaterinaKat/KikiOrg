package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.kikiorg.core.date.DateService;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjValue;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.katyshevtseva.kikiorg.core.sections.study.repo.SubjMarkRepo;
import com.katyshevtseva.kikiorg.core.sections.study.repo.SubjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ZeroMarkCreationService2 {
    private final SubjMarkRepo markRepo;
    private final DateService dateService;
    private final SubjectRepo subjectRepo;

    void createZeroMarks(Subject subject) {
        subject = subjectRepo.findById(subject.getId()).orElseThrow(RuntimeException::new);
        if (!StudyUtils.hasNumericValues(subject))
            throw new RuntimeException();

        SubjMark firstMark = markRepo.findFirstBySubjectOrderByDateEntityValue(subject);
        if (firstMark == null) {
            return;
        }

//        System.out.println("***** START *****");
//        System.out.println("First date " + DateUtils.READABLE_DATE_FORMAT.format(firstMark.getDate()));
        SubjValue zeroValue = getZeroValue(subject);
        Date date = firstMark.getDate();
        Date today = new Date();
        while (DateUtils.before(date, today)) {
            if (!markRepo.existsBySubjectAndDateEntityValue(subject, date)) {
//                System.out.println("Create " + DateUtils.READABLE_DATE_FORMAT.format(date));
                SubjMark mark = new SubjMark(
                        subject,
                        dateService.createIfNotExistAndGetDateEntity(date),
                        zeroValue,
                        null
                );
                markRepo.save(mark);
            }
            date = DateUtils.shiftDate(date, DateUtils.TimeUnit.DAY, 1);
        }
//        System.out.println("***** END *****");
    }

    private SubjValue getZeroValue(Subject subject) {
        for (SubjValue value : subject.getValues()) {
            try {
                int intValue = Integer.parseInt(value.getTitle());
                if (intValue == 0)
                    return value;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        throw new RuntimeException();
    }
}
