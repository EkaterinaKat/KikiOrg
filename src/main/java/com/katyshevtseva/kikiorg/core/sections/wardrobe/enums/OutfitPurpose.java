package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

// Сейчас (в июле 2023) мне эта классификация аутфитов не нужна, но я не стала её удалять, вдруг передумаю
public enum OutfitPurpose {

    GOING_OUT_COLLECTED("На выход");

    private final String title;

    OutfitPurpose(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static OutfitPurpose getByTitleOnNull(String title) {
        for (OutfitPurpose purpose : OutfitPurpose.values()) {
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
