package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.hierarchy.StaticHierarchySchemaLine;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesSubtype;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesSupertype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WardrobeHierarchyUtil {

    public static List<StaticHierarchySchemaLine> getSchema() {
        List<StaticHierarchySchemaLine> lines = new ArrayList<>(getUnclassifiedTypes());
        for (ClothesSupertype supertype : ClothesSupertype.values()) {
            lines.add(supertype);
            lines.addAll(supertype.getTypes());
        }
        return lines;
    }

    private static List<ClothesSubtype> getUnclassifiedTypes() {
        List<ClothesSubtype> types = new LinkedList<>(Arrays.asList(ClothesSubtype.values()));
        for (ClothesSupertype supertype : ClothesSupertype.values()) {
            types.removeAll(supertype.getTypes());
        }
        return types;
    }
}
