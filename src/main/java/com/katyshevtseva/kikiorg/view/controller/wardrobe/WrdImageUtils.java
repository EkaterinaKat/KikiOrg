package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.ImageContainer;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class WrdImageUtils {
    private static final String DIRECTORY_URL = "D:\\Some_files\\wardrobe\\";

    static List<ImageContainer> getFreeImagesForPieceCreation() {
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
                ImageContainer imageContainer = ImageCreator.getInstance().getImageContainer(file.getName());

                freeImages.add(new ImageAndFileNameContainer() {
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
                        return file.getName();
                    }
                });
            }
        }
        return freeImages;
    }

    private static List<File> getAllImageFiles() {
        return Arrays.asList(Objects.requireNonNull(new File(DIRECTORY_URL).listFiles()));
    }

    static List<ImageContainer> toImageUrlAndPieceContainers(List<Piece> pieces) {
        return pieces.stream().map(WrdImageUtils::toImageUrlAndPieceContainer).collect(Collectors.toList());
    }

    static ImageContainer toImageUrlAndPieceContainer(Piece piece) {
        ImageContainer imageContainer = ImageCreator.getInstance().getImageContainer(piece);

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
        };
    }

    static ImageAndFileNameContainer toImageUrlAndFileNameContainer(Piece piece) {
        ImageContainer imageContainer = ImageCreator.getInstance().getImageContainer(piece);

        return new ImageAndFileNameContainer() {
            @Override
            public String getFileName() {
                return piece.getImageFileName();
            }

            @Override
            public Image getImage() {
                return imageContainer.getImage();
            }

            @Override
            public String getPath() {
                return imageContainer.getPath();
            }
        };
    }

    interface ImageAndPieceContainer extends ImageContainer {
        Piece getPiece();
    }

    interface ImageAndFileNameContainer extends ImageContainer {
        String getFileName();
    }
}
