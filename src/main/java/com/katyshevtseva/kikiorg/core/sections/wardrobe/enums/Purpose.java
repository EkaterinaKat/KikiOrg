package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum Purpose {

    HOME("Дом"), WORK("Работа"), SPORT("Спорт"), SHOP("В магазин"), STROLL("Прогулка"),
    HANGING_OUT("Выход в свет");

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
