package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.Utils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.CoreUtils;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.Imagable;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

class ImageSelectionDialogController implements FxController {
    private static final int IMAGE_SIZE = 184;
    private static final int COLUMN_NUM = 5;
    @FXML
    private GridPane gridPane;
    private List<Imagable> imagableList;
    private ImageClickHandler imageClickHandler;

    ImageSelectionDialogController(List<Imagable> imagableList, ImageClickHandler imageClickHandler) {
        this.imagableList = imagableList;
        this.imageClickHandler = imageClickHandler;
    }

    @FXML
    private void initialize() {
        for (int i = 0; i < imagableList.size(); i++) {
            Imagable imagable = imagableList.get(i);
            ImageView imageView = new ImageView(ImageService.getJavafxImageByImagable(imagable));
            imageView.setFitHeight(IMAGE_SIZE);
            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setOnMouseClicked(event -> {
                imageClickHandler.execute(imagable);
                Utils.closeWindowThatContains(gridPane);
            });
            gridPane.add(imageView,
                    CoreUtils.getColumnByIndexAndColumnNum(i, COLUMN_NUM), CoreUtils.getRowByIndexAndColumnNum(i, COLUMN_NUM));
        }
    }

    @FunctionalInterface
    interface ImageClickHandler {
        void execute(Imagable imagable);
    }
}
