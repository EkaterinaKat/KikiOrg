package com.katyshevtseva.kikiorg.core.sections.diary;

import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndValue;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;

public class DiaryUtils {

    static boolean hasNumericValues(Indicator indicator) {
        try {
            for (IndValue value : indicator.getValues()) {
                Integer.parseInt(value.getTitle());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
