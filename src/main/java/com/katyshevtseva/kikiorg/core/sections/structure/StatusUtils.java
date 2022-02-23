package com.katyshevtseva.kikiorg.core.sections.structure;

import com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus;

import static com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus.NEW;

public class StatusUtils {

    static void validateStatusChange(TargetStatus oldStatus, TargetStatus newStatus) {
        if (oldStatus == newStatus) {
            throw new RuntimeException();
        }
        if (newStatus == NEW) {
            throw new RuntimeException();
        }
    }
}
