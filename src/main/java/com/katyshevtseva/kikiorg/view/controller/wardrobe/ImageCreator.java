package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxImageCreationUtil;
import com.katyshevtseva.fx.ImageContainer;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.WARDROBE_IMAGES_LOCATION;

class ImageCreator {
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
        return getImageContainer(fileName, WARDROBE_IMAGES_LOCATION);
    }

    ImageContainer getImageContainer(String fileName, String location) {
        String fullPath = location + fileName;
        ImageContainer imageContainer = cache.get(fullPath);

        if (imageContainer != null) {
            return imageContainer;
        }

        imageContainer = new ImageContainer() {
            @Override
            public Image getImage() {
                return FxImageCreationUtil.getImageByAbsolutePath(fullPath, 400.0, false);
            }

            @Override
            public String getPath() {
                return fullPath;
            }
        };
        cache.put(fullPath, imageContainer);

        return imageContainer;
    }
}
