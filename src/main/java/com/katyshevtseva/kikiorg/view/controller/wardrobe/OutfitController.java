package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Date;

class OutfitController implements FxController {
    private final WardrobeService service = Core.getInstance().wardrobeService();
    @FXML
    private Button outfitCreateButton;
    @FXML
    private Button outfitEditButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label infoLabel;

    @FXML
    private void initialize() {
        updateOutfitGallery();

        outfitCreateButton.setOnAction(event -> OrganizerWindowCreator.getInstance().openOutfitDialog(
                new OutfitDialogController(null, outfit -> {
                    updateOutfitGallery();
                    showOutfitInfo(outfit);
                })));
    }

    private void showOutfitInfo(Outfit outfit) {
        infoLabel.setText(outfit.getFullDesc());
        outfitEditButton.setVisible(true);
        outfitEditButton.setOnAction(event -> OrganizerWindowCreator.getInstance().openOutfitDialog(
                new OutfitDialogController(outfit, savedOutfit -> {
                    updateOutfitGallery();
                    showOutfitInfo(savedOutfit);
                })));
    }

    private void updateOutfitGallery() {
        Date start = new Date();

        gridPane.getChildren().clear();
        int index = 0;

        for (Outfit outfit : service.getAllOutfits()) {
            Pane pane = CollageUtils.getCollagePreview(outfit.getCollageEntity());
            pane.setOnMouseClicked(event -> showOutfitInfo(outfit));
            gridPane.add(pane, GeneralUtils.getColumnByIndexAndColumnNum(index, 2),
                    GeneralUtils.getRowByIndexAndColumnNum(index, 2));
            index++;
        }

        Date end = new Date();
        System.out.println(end.getTime() - start.getTime());
    }
}
