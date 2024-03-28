package com.katyshevtseva.kikiorg.core.tests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SomeStuffToDo {
//    private final IndicatorRepo indicatorRepo;
//    private final SubjectRepo subjectRepo;
//    private final IndMarkRepo indMarkRepo;
//    private final SubjMarkRepo subjMarkRepo;
//    private final CircsRepo circsRepo;

    //        @PostConstruct
    public void execute() {
//        Indicator indicator = indicatorRepo.findById(22L).get();
//        List<IndMark> marks = indMarkRepo.findByIndicator(indicator);
//
//        Subject subject = subjectRepo.save(new Subject(indicator.getTitle(), indicator.getDescription(), false));
//
//        for (IndMark mark : marks) {
//            if (mark.getValue().getId() == 152) {
//                saveSubjMark(subject, mark, 60);
//            } else if (mark.getValue().getId() == 155) {
//                saveSubjMark(subject, mark, 120);
//            } else if (mark.getValue().getId() == 156) {
//                saveSubjMark(subject, mark, 180);
//            } else {
//                saveSubjMark(subject, mark, 0);
//                CircsType type = CircsType.findByTitle(mark.getValue().getDescription());
//                circsRepo.save(new Circs(mark.getDateEntity(), type, mark.getComment()));
//            }
//        }

        System.out.println("******************************************************************");
    }
//
//    private void saveSubjMark(Subject subject, IndMark mark, int min) {
//        SubjMark subjMark = new SubjMark(subject, mark.getDateEntity(), min, mark.getComment());
//        subjMarkRepo.save(subjMark);
//    }
}
