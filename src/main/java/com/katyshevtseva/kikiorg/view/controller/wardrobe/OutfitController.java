package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
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
    private ComboBox<Purpose> purposeComboBox;
    @FXML
    private ComboBox<Season> seasonComboBox;
    @FXML
    private Button showAllButton;
    @FXML
    private Pane outfitGridContainer;

    @FXML
    private void initialize() {
        outfitCreateButton.setOnAction(event -> WindowBuilder.openDialog(OUTFIT,
                new OutfitDialogController(null, outfit -> gridController.loadPage())));
        tuneFilters();
        outfitGridContainer.getChildren().add(WindowBuilder.getNode(OUTFIT_GRID, gridController));
    }

    Page<Outfit> getOutfitPage(int pageNum) {
        return service.getOutfitPage(pageNum, purposeComboBox.getValue(), seasonComboBox.getValue());
    }

    private void tuneFilters() {
        FxUtils.setComboBoxItems(purposeComboBox, Purpose.values());
        FxUtils.setComboBoxItems(seasonComboBox, Season.values());
        purposeComboBox.setOnAction(event -> gridController.loadPage());
        seasonComboBox.setOnAction(event -> gridController.loadPage());
        showAllButton.setOnAction(event -> {
            purposeComboBox.setValue(null);
            seasonComboBox.setValue(null);
            gridController.loadPage();
        });
    }
}
