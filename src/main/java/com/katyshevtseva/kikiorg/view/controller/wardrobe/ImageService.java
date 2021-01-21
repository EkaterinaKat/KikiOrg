package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.Imagable;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

class ImageService {

    static List<Imagable> getFreeImages(List<Piece> existingPieces) {
        List<Imagable> freeImages = new ArrayList<>();
        File[] images = new File("wardrobe").listFiles();
        for (File image : images) {
            boolean imageIsFree = true;
            for (Piece piece : existingPieces)
                if (piece.getImageName().equals(image.getName()))
                    imageIsFree = false;
            if (imageIsFree)
                freeImages.add(new FreeImage(image.getName()));
        }
        return freeImages;
    }

    static Image getJavafxImageByImagable(Imagable imagable) {
        File file = new File("wardrobe\\" + imagable.getImageName());
        try {
            return new Image(file.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    static class FreeImage implements Imagable {
        private String imageName;

        FreeImage(String imageName) {
            this.imageName = imageName;
        }

        public String getImageName() {
            return imageName;
        }
    }
}
