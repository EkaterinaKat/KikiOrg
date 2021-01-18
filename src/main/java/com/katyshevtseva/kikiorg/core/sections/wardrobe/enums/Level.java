package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum Level {
    T("Верх"), B("Низ"), TB("Верхониз"), H("Головной убор"), S("Обувь");

    private String title;

    Level(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
