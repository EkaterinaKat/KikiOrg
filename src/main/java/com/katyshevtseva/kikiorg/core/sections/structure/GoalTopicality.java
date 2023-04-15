package com.katyshevtseva.kikiorg.core.sections.structure;

import lombok.Getter;

@Getter
public enum GoalTopicality {
    t1("Ничего делать не надо", 1, "#85F9FF"),
    t2("Пока не буду об этом думать", 2, "#98FF85"),
    t3("Надо об этом подумать", 4, "#FFF585"),
    t4("Думаю об этом", 5, "#FFBF85");

    private final String title;
    private final int intValue;
    private final String color;

    GoalTopicality(String title, int intValue, String color) {
        this.title = title;
        this.intValue = intValue;
        this.color = color;
    }

    @Override
    public String toString() {
        return title;
    }
}
