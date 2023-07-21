package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Category;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.OutfitSeason;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.OUTFIT;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.OUTFIT_GRID;

class OutfitController implements SectionController {
    private final WardrobeService service = Core.getInstance().wardrobeService();
    private final OutfitGridController gridController = new OutfitGridController(this::getOutfitPage);
    @FXML
    private Button outfitCreateButton;
    @FXML
    private ComboBox<OutfitSeason> seasonComboBox;
    @FXML
    private Button clearSeasonButton;
    @FXML
    private Pane outfitGridContainer;
    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private void initialize() {
        outfitCreateButton.setOnAction(event -> WindowBuilder.openDialog(OUTFIT,
                new OutfitDialogController(categoryComboBox.getValue(), outfit -> gridController.loadPage())));
        tuneFilters();
        outfitGridContainer.getChildren().add(WindowBuilder.getNode(OUTFIT_GRID, gridController));
    }

    Page<Outfit> getOutfitPage(int pageNum) {
        return service.getOutfitPage(pageNum, seasonComboBox.getValue(), categoryComboBox.getValue());
    }

    private void tuneFilters() {
        FxUtils.setComboBoxItems(seasonComboBox, OutfitSeason.values());
        seasonComboBox.setOnAction(event -> gridController.loadPage());
        clearSeasonButton.setOnAction(event -> {
            seasonComboBox.setValue(null);
            gridController.loadPage();
        });

        FxUtils.setComboBoxItems(categoryComboBox, Category.values(), Category.GOING_OUT);
        categoryComboBox.setOnAction(event -> gridController.loadPage());
    }
}
