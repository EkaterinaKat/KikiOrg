package com.katyshevtseva.kikiorg.view.controller.wardrobe.utils;

import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.image.ImageContainerCache;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.WARDROBE_IMAGES_LOCATION;

public class WrdImageUtils {
    private static final ImageContainerCache icc = ImageContainerCache.getInstance();

    public static List<ImageContainer> getFreeImagesForPieceCreation() {
        List<ImageContainer> freeImages = new ArrayList<>();
        List<Piece> existingPieces = Core.getInstance().wardrobeService().getAllPieces();

        for (File file : getAllImageFiles()) {
            boolean imageIsFree = true;
            for (Piece piece : existingPieces) {
                if (piece.getImageFileName().equals(file.getName())) {
                    imageIsFree = false;
                }
            }
            if (imageIsFree) {
                freeImages.add(icc.getImageContainer(file.getName(), WARDROBE_IMAGES_LOCATION, 400));
            }
        }
        return freeImages;
    }

    private static List<File> getAllImageFiles() {
        return Arrays.asList(Objects.requireNonNull(new File(WARDROBE_IMAGES_LOCATION).listFiles()));
    }

    public static List<ImageContainer> toImageUrlAndPieceContainers(List<Piece> pieces) {
        return pieces.stream().map(WrdImageUtils::toImageAndPieceContainer).collect(Collectors.toList());
    }

    public static List<com.katyshevtceva.collage.logic.Image> toCollageImages(List<Piece> pieces) {
        return pieces.stream()
                .map(WrdImageUtils::toImageAndPieceContainer)
                .map(com.katyshevtceva.collage.logic.Image::new)
                .collect(Collectors.toList());
    }

    public static ImageContainer getImageContainer(Piece piece) {
        return icc.getImageContainer(piece.getImageFileName(), WARDROBE_IMAGES_LOCATION, 400);
    }

    static ImageContainer toImageAndPieceContainer(Piece piece) {
        ImageContainer imageContainer = getImageContainer(piece);

        return new ImageAndPieceContainer() {
            @Override
            public Piece getPiece() {
                return piece;
            }

            @Override
            public Image getImage() {
                return imageContainer.getImage();
            }

            @Override
            public String getPath() {
                return imageContainer.getPath();
            }

            @Override
            public String getFileName() {
                return piece.getImageFileName();
            }
        };
    }

    public interface ImageAndPieceContainer extends ImageContainer {
        Piece getPiece();
    }
}
