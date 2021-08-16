package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtceva.collage.logic.*;
import com.katyshevtseva.fx.ImageContainer;
import com.katyshevtseva.fx.Point;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.CollageEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.ComponentEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.ImageAndPieceContainer;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.toImageContainer;
import static com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.toImageUrlAndPieceContainers;

class CollageUtils {
    static final int COLLAGE_SIZE = 850;

    static CollageEntity saveCollage(CollageEntity existing, Collage collage) {
        CollageEntity savedCollageEntity = Core.getInstance().wardrobeService().saveCollage(existing);

        for (Component component : collage.getComponents())
            saveComponent(component, savedCollageEntity);

        return savedCollageEntity;
    }

    private static void saveComponent(Component component, CollageEntity savedCollageEntity) {
        List<Piece> pieces = component.getImageContainers().stream()
                .map(imageContainer -> ((ImageAndPieceContainer) imageContainer).getPiece()).collect(Collectors.toList());
        Piece frontPiece = ((ImageAndPieceContainer) component.getFrontImageContainer()).getPiece();

        Core.getInstance().wardrobeService().saveComponent(
                component.getId(),
                component.getRelativeX(),
                component.getRelativeY(),
                component.getZ(),
                component.getRelativeWidth(),
                pieces,
                frontPiece,
                savedCollageEntity);
    }

    static Pane getCollagePreview(CollageEntity collageEntity) {
        List<StaticComponent> staticComponents = collageEntity.getComponents().stream()
                .map(componentEntity -> new StaticComponent(
                        toImageContainer(componentEntity.getFrontPiece()),
                        new Point(componentEntity.getRelativeX(), componentEntity.getRelativeY()),
                        componentEntity.getRelativeWidth(),
                        componentEntity.getZ()))
                .collect(Collectors.toList());

        return CollagePreviewBuilder.buildPreview(new Size(400, 400), staticComponents);
    }

    static Collage reproduceCollage(CollageEntity collageEntity) {
        if (collageEntity == null)
            return createEmptyCollage();

        Collage collage = new CollageBuilder()
                .allExistingImages(getAllExistingImages())
                .height(COLLAGE_SIZE)
                .width(COLLAGE_SIZE)
                .build();

        for (ComponentEntity componentEntity : collageEntity.getComponents()) {
            collage.addComponent(entityToComponent(componentEntity, collage));
        }

        return collage;
    }

    private static Component entityToComponent(ComponentEntity componentEntity, Collage collage) {
        List<ImageContainer> imageContainers = toImageUrlAndPieceContainers(new ArrayList<>(componentEntity.getPieces()));
        ImageContainer frontImageContainer = WrdImageUtils.toImageUrlAndPieceContainer(componentEntity.getFrontPiece());

        return new ComponentBuilder(collage, imageContainers)
                .id(componentEntity.getId())
                .relativeWidth(componentEntity.getRelativeWidth())
                .z(componentEntity.getZ())
                .frontImage(frontImageContainer)
                .relativePosition(new Point(componentEntity.getRelativeX(), componentEntity.getRelativeY()))
                .build();
    }

    static Collage createEmptyCollage() {
        return new CollageBuilder()
                .height(COLLAGE_SIZE)
                .width(COLLAGE_SIZE)
                .allExistingImages(getAllExistingImages())
                .build();
    }

    private static List<ImageContainer> getAllExistingImages() {
        return toImageUrlAndPieceContainers(Core.getInstance().wardrobeService().getAllPieces());
    }
}
