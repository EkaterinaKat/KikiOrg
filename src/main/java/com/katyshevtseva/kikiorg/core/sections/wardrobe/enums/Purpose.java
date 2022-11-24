package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum Purpose {

    HOME("Дом"),
    GOING_OUT_COLLECTED("На выход: собранный"),
    GOING_OUT_RELAXED("На выход: расслабленный");

    private final String title;

    Purpose(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static Purpose getByTitleOnNull(String title) {
        for (Purpose purpose : Purpose.values()) {
            if (purpose.title.equals(title))
                return purpose;
        }
        return null;
    }

    @Override
    public String toString() {
        return title;
    }
}
