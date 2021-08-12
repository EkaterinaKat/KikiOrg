package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.ImageContainer;
import com.katyshevtseva.fx.ImageUtils;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
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
                freeImages.add(new ImageNameAndUrlContainer() {
                    @Override
                    public String getFileName() {
                        return file.getName();
                    }

                    @Override
                    public String getUrl() {
                        return file.getAbsolutePath();
                    }
                });
            }
        }
        return freeImages;
    }

    private static List<File> getAllImageFiles() {
        return Arrays.asList(Objects.requireNonNull(new File(DIRECTORY_URL).listFiles()));
    }

    static List<ImageContainer> toExtendedImageContainers(List<Piece> pieces) {
        return pieces.stream()
                .map(piece -> new ImageNameAndPieceContainer() {
                    @Override
                    public Piece getPiece() {
                        return piece;
                    }

                    @Override
                    public String getUrl() {
                        return getPieceImageAbsolutePath(piece);
                    }
                })
                .collect(Collectors.toList());
    }

    static ImageNameAndUrlContainer toImageContainer(Piece piece) {
        return new ImageNameAndUrlContainer() {
            @Override
            public String getFileName() {
                return piece.getImageFileName();
            }

            @Override
            public String getUrl() {
                return DIRECTORY_URL + piece.getImageFileName();
            }
        };
    }

    static Image getImageByPiece(Piece piece) {
        return ImageUtils.getImageByAbsolutePath(getPieceImageAbsolutePath(piece));
    }

    static Image getImageByImageContainer(ImageContainer imageContainer) {
        File file = new File(imageContainer.getUrl());
        if (file.exists()) {
            try {
                return new Image(file.toURI().toURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        new StandardDialogBuilder().openInfoDialog("Ошибка!\n Файл с изображением не найден");
        throw new RuntimeException();
    }

    private static String getPieceImageAbsolutePath(Piece piece) {
        return DIRECTORY_URL + piece.getImageFileName();
    }

    interface ImageNameAndPieceContainer extends ImageContainer {
        Piece getPiece();
    }

    interface ImageNameAndUrlContainer extends ImageContainer {
        String getFileName();
    }
}
