package com.katyshevtseva.kikiorg.core.sections.work;

public enum WorkArea {
    MATH("Математика"), MP("Мои проекты"), GD("Общее развитие"), ENG("Английский");

    private String title;

    WorkArea(String name) {
        this.title = name;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
