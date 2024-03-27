package com.katyshevtseva.kikiorg.core.sections.study;

import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjValue;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;

public class StudyUtils {

    static boolean hasNumericValues(Subject subject) {
        try {
            for (SubjValue value : subject.getValues()) {
                Integer.parseInt(value.getTitle());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
