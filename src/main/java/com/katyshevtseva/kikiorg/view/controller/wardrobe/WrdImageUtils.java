package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.ImageContainer;
import com.katyshevtseva.fx.ImageUtils;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import javafx.scene.image.Image;

import java.util.List;
import java.util.stream.Collectors;

class WrdImageUtils {
    private static final String DIRECTORY_URL = "D:\\Some_files\\wardrobe\\";

    static List<ImageContainer> toExtendedImageContainers(List<Piece> pieces) {
        return pieces.stream()
                .map(piece -> new ExtendedImageContainer() {
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

    static Image getImageByPiece(Piece piece) {
        return ImageUtils.getImageByAbsolutePath(getPieceImageAbsolutePath(piece));
    }

    private static String getPieceImageAbsolutePath(Piece piece) {
        return DIRECTORY_URL + piece.getImageFileName();
    }

    interface ExtendedImageContainer extends ImageContainer {
        Piece getPiece();
    }
}
