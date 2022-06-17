package com.katyshevtseva.kikiorg.core.sections.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesSubtype;

import java.util.List;

public interface ClothesType {

    String getTitle();

    List<ClothesSubtype> getSubtypes();
}
