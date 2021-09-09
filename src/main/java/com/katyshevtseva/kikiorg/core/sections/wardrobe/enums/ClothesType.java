package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

public enum ClothesType {

    FOOTWEAR("Прочая обувь"), TROUSERS("Прочие штаны"), SKIRT("Юбка"), DRESS("Платье"),
    HEADDRESS("Головной убор"), SCARF("Шарф"), GLOVES("Перчатки/варежки"), BAG("Сумка"),
    OUTERWEAR("Верхняя одежда"), BACKPACK("Рюкзак"), HOODIE("Худи"), T_SHORT("Футболка"),
    SWEATSHIRT("Свитшот"), SWEATER("Свитер"), CARDIGAN("Кардиган"), ROLL_NECK("Кофта с воротом"),
    LIGHT_OUTWEAR("Легкая верхняя одежда"), JUMPER("Джемпер"), OTHER("Прочее"), JEANS("Джинсы"),
    LEGGINGS("Леггинсы"), TOP("Топ/майка"), PANTS("Брюки"), SNEAKERS("Кроссовки/кеды"),
    BOOTS("Сапоги"), SPORTS_TROUSERS("Спортивные штаны"), SHIRT("Рубашка"), LOW_BOOTS("Ботинки"),
    SHOES("Туфли"), UNDERWEAR("Нижнее бельё"), UP("Прочий верх");

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
