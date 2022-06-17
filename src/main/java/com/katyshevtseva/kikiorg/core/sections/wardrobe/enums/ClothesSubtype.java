package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;


import com.katyshevtseva.hierarchy.StaticHierarchySchemaLine;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.ClothesType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum ClothesSubtype implements StaticHierarchySchemaLine, ClothesType {

    OTHER_FOOTWEAR("Прочая обувь"), OTHER_TROUSERS("Прочие штаны"), SKIRT("Юбка"), DRESS("Платье"),
    HEADDRESS("Головной убор"), SCARF("Шарф"), GLOVES("Перчатки/варежки"), BAG("Сумка"),
    OUTERWEAR("Верхняя одежда"), BACKPACK("Рюкзак"), HOODIE("Худи"), T_SHORT("Футболка"),
    SWEATSHIRT("Свитшот"), SWEATER("Свитер"), CARDIGAN("Кардиган"), ROLL_NECK("Кофта с воротом"),
    LIGHT_OUTWEAR("Легкая верхняя одежда"), JUMPER("Джемпер"), OTHER("Прочее"), JEANS("Джинсы"),
    LEGGINGS("Леггинсы"), TOP("Топ/майка"), PANTS("Брюки"), SNEAKERS("Кроссовки/кеды"),
    BOOTS("Сапоги"), SPORTS_TROUSERS("Спортивные штаны"), SHIRT("Рубашка"), LOW_BOOTS("Ботинки"),
    SHOES("Туфли"), OTHER_UP("Прочий верх");

    private final String title;

    ClothesSubtype(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public String getDescription() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    public static List<ClothesSubtype> getSortedByTitleValues() {
        return Arrays.stream(ClothesSubtype.values()).sorted(Comparator.comparing(ClothesSubtype::getTitle)).collect(Collectors.toList());
    }

    @Override
    public List<ClothesSubtype> getSubtypes() {
        return Collections.singletonList(this);
    }
}
