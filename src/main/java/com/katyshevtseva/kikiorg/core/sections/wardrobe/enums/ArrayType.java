package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum ArrayType {
    T("Верх"), Bt("Низ"), TB("Верхониз"), H("Головной убор"), F("Обувь"), Bg("Сумка"),
    Sc("Шарф"), G("Перчатки"), O("Верхняя одежда");

    private String title;

    ArrayType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
