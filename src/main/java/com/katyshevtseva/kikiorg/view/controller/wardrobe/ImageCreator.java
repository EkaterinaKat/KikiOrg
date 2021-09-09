package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxImageCreationUtil;
import com.katyshevtseva.fx.ImageContainer;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

class ImageCreator {
    private static final String DIRECTORY_URL = "D:\\Some_files\\wardrobe\\";
    private static ImageCreator instance;
    private final Map<String, ImageContainer> cache = new HashMap<>();

    static ImageCreator getInstance() {
        if (instance == null)
            instance = new ImageCreator();
        return instance;
    }

    ImageContainer getImageContainer(Piece piece) {
        return getImageContainer(piece.getImageFileName());
    }

    ImageContainer getImageContainer(String fileName) {
        ImageContainer imageContainer = cache.get(fileName);

        if (imageContainer != null) {
            return imageContainer;
        }

        imageContainer = new ImageContainer() {
            @Override
            public Image getImage() {
                return FxImageCreationUtil.getImageByAbsolutePath(DIRECTORY_URL + fileName, 400.0, false);
            }

            @Override
            public String getPath() {
                return DIRECTORY_URL + fileName;
            }
        };
        cache.put(fileName, imageContainer);

        return imageContainer;
    }
}
