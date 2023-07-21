package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.hierarchy.StaticHierarchySchemaLine;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSupertype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WardrobeHierarchyUtil {

    public static List<StaticHierarchySchemaLine> getSchema() {
        List<StaticHierarchySchemaLine> lines = new ArrayList<>(getUnclassifiedTypes());
        for (PieceSupertype supertype : PieceSupertype.values()) {
            lines.add(supertype);
            lines.addAll(supertype.getTypes());
        }
        return lines;
    }

    private static List<PieceSubtype> getUnclassifiedTypes() {
        List<PieceSubtype> types = new LinkedList<>(Arrays.asList(PieceSubtype.values()));
        for (PieceSupertype supertype : PieceSupertype.values()) {
            types.removeAll(supertype.getTypes());
        }
        return types;
    }
}
