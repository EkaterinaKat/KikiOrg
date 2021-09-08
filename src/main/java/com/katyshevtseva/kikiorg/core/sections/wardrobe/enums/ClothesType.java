package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum ClothesType {

    FOOTWEAR("Обувь"), TROUSERS("Штаны"), SKIRT("Юбка"), DRESS("Платье"),
    HEADDRESS("Головной убор"), SCARF("Шарф"), GLOVES("Перчатки"), BAG("Сумка"),
    OUTERWEAR("Верхняя одежда"), BACKPACK("Рюкзак"), HOODIE("Худи");

    private final String title;

    ClothesType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
