package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum ClothesType {

    F("Обувь"), T("Штаны"), Sk("Юбка"), D("Платье"), H("Головной убор"), Sc("Шарф"),
    G("Перчатки"), B("Сумка"), O("Верхняя одежда"), U("Верх");

    private String title;

    ClothesType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
