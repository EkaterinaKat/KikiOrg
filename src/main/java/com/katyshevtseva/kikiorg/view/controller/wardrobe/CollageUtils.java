package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtceva.collage.logic.Collage;
import com.katyshevtceva.collage.logic.CollageBuilder;
import com.katyshevtseva.kikiorg.core.Core;

class CollageUtils {
    static final int COLLAGE_SIZE = 850;

    static Collage createEmptyCollage() {
        return new CollageBuilder()
                .height(COLLAGE_SIZE)
                .width(COLLAGE_SIZE)
                .color("#F08080")
                .editingMode(true)
                .allExistingImages(WrdImageUtils.toImageUrlAndPieceContainers(Core.getInstance().wardrobeService().getAllPieces()))
                .build();
    }
}
