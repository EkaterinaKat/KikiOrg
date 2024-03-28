package com.katyshevtseva.kikiorg.core.sections.study.enums;

import java.util.Arrays;

public enum CircsType {
    ENTERTAINMENT("Развлечения"),
    DAY_OFF("Выходной"),
    PANIC("не получалось -> программисткая паника"),
    SICK_OF_IT("Задолбалась"),
    INERTIA("По инерции"),
    TRIPS("Поездки"),
    LA("Лень-апатия"),
    DISEASE("Болезнь"),
    HOLIDAYS("Праздники"),
    POOR_ORG("Организационный косяк");

    private final String title;

    CircsType(String title) {
        this.title = title;
    }

    public static CircsType findByTitle(String title) {
        return Arrays.stream(CircsType.values())
                .filter(type -> type.title.equals(title))
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public String getTitle() {
        return title;
    }
}
