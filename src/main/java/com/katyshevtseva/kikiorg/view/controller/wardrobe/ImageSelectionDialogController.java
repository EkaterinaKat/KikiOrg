package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.CoreUtils;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.Imagable;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;

class ImageSelectionDialogController implements FxController {
    private static final int IMAGE_SIZE = 184;
    private static final int COLUMN_NUM = 5;
    @FXML
    private GridPane gridPane;
    private List<Imagable> imagableList;
    private OneArgKnob<Imagable> imageClickHandler;

    ImageSelectionDialogController(List<Imagable> imagableList, OneArgKnob<Imagable> imageClickHandler) {
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
                closeWindowThatContains(gridPane);
            });
            gridPane.add(imageView,
                    CoreUtils.getColumnByIndexAndColumnNum(i, COLUMN_NUM), CoreUtils.getRowByIndexAndColumnNum(i, COLUMN_NUM));
        }
    }
}
