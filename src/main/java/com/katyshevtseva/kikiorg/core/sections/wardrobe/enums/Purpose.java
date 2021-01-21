package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum Purpose {

    H("Дом"), W("Работа"), Sp("Спорт"), Sh("В магазин"), St("Прогулка"), O("Выход в свет");

    private String title;

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
