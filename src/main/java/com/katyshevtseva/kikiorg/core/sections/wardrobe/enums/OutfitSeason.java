package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum OutfitSeason {
    W("Зима"), S("Лето"), DS("Демисезон");

    private String title;

    OutfitSeason(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static OutfitSeason getByTitleOnNull(String title) {
        for (OutfitSeason season : OutfitSeason.values()) {
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
