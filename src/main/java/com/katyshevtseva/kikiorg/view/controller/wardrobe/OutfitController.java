package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

class OutfitController implements FxController {
    private WardrobeService service = Core.getInstance().wardrobeService();
    @FXML
    private Button outfitCreateButton;
    @FXML
    private Button outfitEditButton;

    @FXML
    private void initialize() {
        outfitCreateButton.setOnAction(event -> OrganizerWindowCreator.getInstance().openOutfitDialog(
                new OutfitDialogController(null, outfit -> {
                    updateOutfitGallery();
                    showOutfitInfo(outfit);
                })));

        outfitEditButton.setOnAction(event -> OrganizerWindowCreator.getInstance().openOutfitDialog(
                new OutfitDialogController(service.getAllOutfits().get(0), outfit -> { //todo временная мера
                    updateOutfitGallery();
                    showOutfitInfo(outfit);
                })));
    }

    private void showOutfitInfo(Outfit outfit) {

    }

    private void updateOutfitGallery() {

    }
}
