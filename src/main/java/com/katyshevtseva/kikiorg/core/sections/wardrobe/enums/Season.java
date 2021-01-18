package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum Season {
    W("Зима"), S("Лето"), DS("Демисезон");

    private String title;

    Season(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
