package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;


import com.katyshevtseva.hierarchy.StaticHierarchySchemaLine;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.PieceType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum PieceSubtype implements StaticHierarchySchemaLine, PieceType {

    OTHER_FOOTWEAR("Прочая обувь"), OTHER_TROUSERS("Прочие штаны"), SKIRT("Юбка"), DRESS("Платье"),
    HEADDRESS("Головной убор"), SCARF("Шарф"), GLOVES("Перчатки/варежки"), BAG("Сумка"),
    OUTERWEAR("Верхняя одежда"), BACKPACK("Рюкзак"), HOODIE("Худи"), T_SHORT("Футболка"),
    SWEATSHIRT("Свитшот"), SWEATER("Свитер"), CARDIGAN("Кардиган"), SHORTS("Шорты"),
    LIGHT_OUTWEAR("Легкая верхняя одежда"), JUMPER("Джемпер"), OTHER("Прочее"), JEANS("Джинсы"),
    LEGGINGS("Леггинсы"), TOP("Топ/майка"), PANTS("Брюки"), SNEAKERS("Кроссовки/кеды"),
    BOOTS("Сапоги"), SPORTS_TROUSERS("Спортивные штаны"), SHIRT("Рубашка"), LOW_BOOTS("Ботинки"),
    SHOES("Туфли"), OTHER_UP("Прочий верх");

    private final String title;

    PieceSubtype(String title) {
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

    public static List<PieceSubtype> getSortedByTitleValues() {
        return Arrays.stream(PieceSubtype.values()).sorted(Comparator.comparing(PieceSubtype::getTitle)).collect(Collectors.toList());
    }

    @Override
    public List<PieceSubtype> getSubtypes() {
        return Collections.singletonList(this);
    }
}
