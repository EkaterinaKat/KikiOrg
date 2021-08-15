package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtceva.collage.logic.Collage;
import com.katyshevtceva.collage.logic.CollageBuilder;
import com.katyshevtceva.collage.logic.Component;
import com.katyshevtceva.collage.logic.ComponentBuilder;
import com.katyshevtseva.fx.ImageContainer;
import com.katyshevtseva.fx.Point;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.CollageEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.ComponentEntity;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.ImageUrlAndPieceContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class CollageUtils {
    static final int COLLAGE_SIZE = 850;
    static final int PREVIEW_COLLAGE_SIZE = 200;

    enum CollageType {
        EDITABLE, PREVIEW
    }

    static CollageEntity saveCollage(CollageEntity existing, Collage collage) {
        CollageEntity savedCollageEntity = Core.getInstance().wardrobeService().saveCollage(existing, collage.getColor());

        for (Component component : collage.getComponents())
            saveComponent(component, savedCollageEntity);

        return savedCollageEntity;
    }

    private static void saveComponent(Component component, CollageEntity savedCollageEntity) {
        List<Piece> pieces = component.getImageContainers().stream()
                .map(imageContainer -> ((ImageUrlAndPieceContainer) imageContainer).getPiece()).collect(Collectors.toList());
        Piece frontPiece = ((ImageUrlAndPieceContainer) component.getFrontImageContainer()).getPiece();

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

    static Collage reproduceCollage(CollageEntity collageEntity, CollageType collageType) {
        if (collageEntity == null)
            return createEmptyCollage(collageType);

        Collage collage = new CollageBuilder()
                .allExistingImages(getAllExistingImages(collageType))
                .height(getCollageSize(collageType))
                .width(getCollageSize(collageType))
                .color(collageEntity.getColor())
                .editingMode(collageType == CollageType.EDITABLE)
                .build();

        for (ComponentEntity componentEntity : collageEntity.getComponents()) {
            collage.addComponent(entityToComponent(componentEntity, collage));
        }

        return collage;
    }

    private static Component entityToComponent(ComponentEntity componentEntity, Collage collage) {
        List<ImageContainer> imageContainers = WrdImageUtils.toImageUrlAndPieceContainers(componentEntity.getPieces());
        ImageContainer frontImageContainer = WrdImageUtils.toImageUrlAndPieceContainer(componentEntity.getFrontPiece());

        return new ComponentBuilder(collage, imageContainers)
                .id(componentEntity.getId())
                .relativeWidth(componentEntity.getRelativeWidth())
                .z(componentEntity.getZ())
                .frontImage(frontImageContainer)
                .relativePosition(new Point(componentEntity.getRelativeX(), componentEntity.getRelativeY()))
                .build();
    }

    static Collage createEmptyCollage(CollageType collageType) {
        return new CollageBuilder()
                .height(getCollageSize(collageType))
                .width(getCollageSize(collageType))
                .editingMode(collageType == CollageType.EDITABLE)
                .allExistingImages(getAllExistingImages(collageType))
                .build();
    }

    private static int getCollageSize(CollageType collageType) {
        return collageType == CollageType.EDITABLE ? COLLAGE_SIZE : PREVIEW_COLLAGE_SIZE;
    }

    private static List<ImageContainer> getAllExistingImages(CollageType collageType) {
        return collageType == CollageType.EDITABLE ?
                WrdImageUtils.toImageUrlAndPieceContainers(Core.getInstance().wardrobeService().getAllPieces())
                : new ArrayList<>();
    }

}
