package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

import com.katyshevtseva.hierarchy.StaticHierarchySchemaLine;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.ClothesType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesSubtype.*;

public enum ClothesSupertype implements StaticHierarchySchemaLine, ClothesType {
    UP("Верх", Arrays.asList(OTHER_UP, TOP, ROLL_NECK, HOODIE, JUMPER, T_SHORT, SHIRT, SWEATSHIRT, SWEATER, CARDIGAN)),
    DOWN("Низ", Arrays.asList(OTHER_TROUSERS, SKIRT, PANTS, JEANS, LEGGINGS, SPORTS_TROUSERS)),
    FOOTWEAR("Обувь", Arrays.asList(OTHER_FOOTWEAR, SHOES, LOW_BOOTS, SNEAKERS, BOOTS)),
    ACCESSORIES("Аксессуары", Arrays.asList(HEADDRESS, SCARF, GLOVES, BACKPACK, BAG));

    private final String title;
    private final List<ClothesSubtype> types;

    ClothesSupertype(String title, List<ClothesSubtype> types) {
        this.title = title;
        this.types = types;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public String getDescription() {
        return title;
    }

    public List<ClothesSubtype> getTypes() {
        return types;
    }

    @Override
    public List<ClothesSubtype> getSubtypes() {
        return getTypes();
    }

    public static List<ClothesSupertype> getSupertypesBySubtype(ClothesSubtype subtype) {
        return Arrays.stream(ClothesSupertype.values())
                .filter(supertype -> supertype.getTypes().contains(subtype))
                .collect(Collectors.toList());
    }
}
