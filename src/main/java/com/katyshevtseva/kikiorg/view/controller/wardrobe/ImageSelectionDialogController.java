package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.HavingImage;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

class ImageSelectionDialogController implements FxController {
    private static final int IMAGE_SIZE = 184;
    private static final int COLUMN_NUM = 5;
    @FXML
    private GridPane gridPane;
    private List<HavingImage> havingImageList;
    private ImageClickHandler imageClickHandler;

    ImageSelectionDialogController(List<HavingImage> havingImageList, ImageClickHandler imageClickHandler) {
        this.havingImageList = havingImageList;
        this.imageClickHandler = imageClickHandler;
    }

    @FXML
    private void initialize() {
        for (int i = 0; i < havingImageList.size(); i++) {
            HavingImage havingImage = havingImageList.get(i);
            ImageView imageView = new ImageView(new Image(havingImage.getImageName()));
            imageView.setFitHeight(IMAGE_SIZE);
            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setOnMouseClicked(event -> {
                imageClickHandler.execute(havingImage);
                Utils.closeWindowThatContains(gridPane);
            });
            gridPane.add(imageView,
                    getColumnByIndexAndColumnNum(i, COLUMN_NUM), getRowByIndexAndColumnNum(i, COLUMN_NUM));
        }
    }

    private int getRowByIndexAndColumnNum(int index, int columnNum) {
        return index / columnNum;
    }

    private int getColumnByIndexAndColumnNum(int index, int columnNum) {
        return index % columnNum;
    }

    @FunctionalInterface
    interface ImageClickHandler {
        void execute(HavingImage havingImage);
    }
}
