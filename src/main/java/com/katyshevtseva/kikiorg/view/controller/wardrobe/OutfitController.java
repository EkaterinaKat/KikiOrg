package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.PaginationPaneController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.List;

class OutfitController implements FxController {
    private final WardrobeService service = Core.getInstance().wardrobeService();
    @FXML
    private Button outfitCreateButton;
    @FXML
    private ComboBox<Purpose> purposeComboBox;
    @FXML
    private ComboBox<Season> seasonComboBox;
    @FXML
    private Button showAllButton;
    @FXML
    private Button outfitEditButton;
    @FXML
    private Button outfitDeleteButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label infoLabel;
    @FXML
    private Pane paginationPane;
    private PaginationPaneController<Outfit> paginationPaneController;

    @FXML
    private void initialize() {
        outfitCreateButton.setOnAction(event -> OrganizerWindowCreator.getInstance().openOutfitDialog(
                new OutfitDialogController(null, outfit -> {
                    paginationPaneController.loadPage();
                    showOutfitInfo(outfit);
                })));

        tunePagination();
        tuneFilters();
    }

    private void tunePagination() {
        Component<PaginationPaneController<Outfit>> component =
                new ComponentBuilder().getPaginationComponent(this::getOutfitPage, this::setContent);
        paginationPaneController = component.getController();
        paginationPane.getChildren().add(component.getNode());
    }

    Page<Outfit> getOutfitPage(int pageNum) {
        return service.getOutfitPage(pageNum, purposeComboBox.getValue(), seasonComboBox.getValue());
    }

    private void tuneFilters() {
        FxUtils.setComboBoxItems(purposeComboBox, Purpose.values());
        FxUtils.setComboBoxItems(seasonComboBox, Season.values());
        purposeComboBox.setOnAction(event -> paginationPaneController.loadPage());
        seasonComboBox.setOnAction(event -> paginationPaneController.loadPage());
        showAllButton.setOnAction(event -> {
            purposeComboBox.setValue(null);
            seasonComboBox.setValue(null);
            paginationPaneController.loadPage();
        });
    }

    private void showOutfitInfo(Outfit outfit) {
        infoLabel.setText(outfit.getFullDesc());
        outfitEditButton.setVisible(true);
        outfitDeleteButton.setVisible(true);
        outfitEditButton.setOnAction(event -> OrganizerWindowCreator.getInstance().openOutfitDialog(
                new OutfitDialogController(outfit, savedOutfit -> {
                    paginationPaneController.loadPage();
                    showOutfitInfo(savedOutfit);
                })));
        outfitDeleteButton.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Delete?",
                b -> {
                    if (b) {
                        service.deleteOutfit(outfit);
                        paginationPaneController.loadPage();
                    }
                }));
    }

    private void clearOutfitInfo() {
        infoLabel.setText("");
        outfitEditButton.setVisible(false);
        outfitDeleteButton.setVisible(false);
    }

    private void setContent(List<Outfit> outfits) {
        gridPane.getChildren().clear();
        clearOutfitInfo();
        int index = 0;

        for (Outfit outfit : outfits) {
            Pane pane = CollageUtils.getCollagePreview(outfit.getCollageEntity());
            pane.setStyle("-fx-border-color: #000000;");
            pane.setOnMouseClicked(event -> showOutfitInfo(outfit));
            gridPane.add(pane, GeneralUtils.getColumnByIndexAndColumnNum(index, 2),
                    GeneralUtils.getRowByIndexAndColumnNum(index, 2));
            index++;
        }
    }
}
