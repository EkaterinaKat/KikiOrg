package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;

import java.util.List;

public interface PieceType {

    String getTitle();

    List<PieceSubtype> getSubtypes();
}
