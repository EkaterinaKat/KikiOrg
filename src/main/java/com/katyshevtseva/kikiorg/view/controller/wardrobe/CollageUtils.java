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
import java.util.Set;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.toImageUrlAndPieceContainers;

class CollageUtils {
    static final int COLLAGE_SIZE = 850;

    static CollageEntity saveCollage(CollageEntity existing, Collage collage) {
        CollageEntity savedCollageEntity = Core.getInstance().wardrobeService().saveCollage(existing);

        List<ComponentEntity> componentEntities = collage.getComponents().stream()
                .map(component -> toEntity(component, savedCollageEntity)).collect(Collectors.toList());

        Core.getInstance().wardrobeService().saveComponents(componentEntities, savedCollageEntity);

        return savedCollageEntity;
    }

    private static ComponentEntity toEntity(Component component, CollageEntity collageEntity) {
        Set<Piece> pieces = component.getImageContainers().stream()
                .map(imageContainer -> ((ImageAndPieceContainer) imageContainer).getPiece()).collect(Collectors.toSet());
        Piece frontPiece = ((ImageAndPieceContainer) component.getFrontImageContainer()).getPiece();

        ComponentEntity entity = new ComponentEntity();
        entity.setRelativeX(component.getRelativeX());
        entity.setRelativeY(component.getRelativeY());
        entity.setRelativeWidth(component.getRelativeWidth());
        entity.setZ(component.getZ());
        entity.setPieces(pieces);
        entity.setFrontPiece(frontPiece);
        entity.setCollageEntity(collageEntity);

        return entity;
    }

    static Pane getCollagePreview(CollageEntity collageEntity) {
        List<StaticComponent> staticComponents = collageEntity.getComponents().stream()
                .map(componentEntity -> new StaticComponent(
                        ImageCreator.getInstance().getImageContainer(componentEntity.getFrontPiece()),
                        new Point(componentEntity.getRelativeX(), componentEntity.getRelativeY()),
                        componentEntity.getRelativeWidth(),
                        componentEntity.getZ(),
                        componentEntity.getPieces().size() > 1))
                .collect(Collectors.toList());

        return CollagePreviewBuilder.buildPreview(new Size(370, 370), staticComponents);
    }

    static Collage reproduceCollage(CollageEntity collageEntity) {
        if (collageEntity == null)
            return createEmptyCollage();

        Collage collage = new CollageBuilder()
                .allExistingImages(toImageUrlAndPieceContainers(Core.getInstance().wardrobeService().getAllPieces()))
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
                .allExistingImages(toImageUrlAndPieceContainers(Core.getInstance().wardrobeService().getAllPieces()))
                .build();
    }
}
