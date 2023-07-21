package com.katyshevtseva.kikiorg.core.sections.wardrobe.enums;

import com.katyshevtseva.hierarchy.StaticHierarchySchemaLine;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.PieceType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype.*;

public enum PieceSupertype implements StaticHierarchySchemaLine, PieceType {
    UP("Верх", Arrays.asList(OTHER_UP, TOP, HOODIE, JUMPER, T_SHORT, SHIRT, SWEATSHIRT, SWEATER, CARDIGAN)),
    DOWN("Низ", Arrays.asList(OTHER_TROUSERS, SKIRT, PANTS, JEANS, LEGGINGS, SPORTS_TROUSERS, SHORTS)),
    FOOTWEAR("Обувь", Arrays.asList(OTHER_FOOTWEAR, SHOES, LOW_BOOTS, SNEAKERS, BOOTS)),
    ACCESSORIES("Аксессуары", Arrays.asList(HEADDRESS, SCARF, GLOVES, BACKPACK, BAG));

    private final String title;
    private final List<PieceSubtype> types;

    PieceSupertype(String title, List<PieceSubtype> types) {
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

    public List<PieceSubtype> getTypes() {
        return types;
    }

    @Override
    public List<PieceSubtype> getSubtypes() {
        return getTypes();
    }

    public static List<PieceSupertype> getSupertypesBySubtype(PieceSubtype subtype) {
        return Arrays.stream(PieceSupertype.values())
                .filter(supertype -> supertype.getTypes().contains(subtype))
                .collect(Collectors.toList());
    }
}
