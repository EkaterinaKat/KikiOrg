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

    public static Season getByTitleOnNull(String title) {
        for (Season season : Season.values()) {
            if (season.title.equals(title))
                return season;
        }
        return null;
    }

    @Override
    public String toString() {
        return title;
    }
}
