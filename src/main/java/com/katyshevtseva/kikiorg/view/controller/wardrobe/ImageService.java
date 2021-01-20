package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.HavingImage;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ImageService {

    static List<HavingImage> getFreeImages(List<Piece> existingPieces) {
        List<HavingImage> havingImageList = new ArrayList<>();
        File gallery_path = new File("wardrobe");
        File[] images = gallery_path.listFiles();
        for (File image : images) {
            boolean imageIsFree = true;
            for (Piece piece : existingPieces)
                if (piece.getImageName().equals(image.getName()))
                    imageIsFree = false;
            if (imageIsFree)
                havingImageList.add(new FreeImage(image.getName()));
        }
        return havingImageList;
    }

    static class FreeImage implements HavingImage {
        private String imageName;

        FreeImage(String imageName) {
            this.imageName = imageName;
        }

        public String getImageName() {
            return imageName;
        }
    }
}
