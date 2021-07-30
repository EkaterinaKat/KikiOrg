package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.ImageContainer;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;

import java.util.List;
import java.util.stream.Collectors;

class ImageUtils {
    private static final String DIRECTORY_URL = "D:\\Some_files\\wardrobe\\";

    static List<ImageContainer> toImageContainers(List<Piece> pieces) {
        return pieces.stream()
                .map(piece -> (ImageContainer) () -> DIRECTORY_URL + piece.getImageFileName())
                .collect(Collectors.toList());
    }
}
