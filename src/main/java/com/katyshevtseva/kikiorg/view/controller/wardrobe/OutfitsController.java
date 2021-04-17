package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.CoreUtils;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;

class OutfitsController implements FxController {
    private static final int OUTFIT_BLOCK_SIZE = 440;
    private static final int COLUMN_NUM = 2;
    @FXML
    private GridPane gridPane;
    @FXML
    private VBox searchPane;
    @FXML
    private Button newOutfitButton;
    private List<Outfit> outfits;

    @FXML
    private void initialize() {
        outfits = Core.getInstance().wardrobeService().getAllOutfits();
        fillGridPane(outfits);
        adjustSearchPane();
        newOutfitButton.setOnAction(event -> openOutfitDialog(null));
    }

    private void fillGridPane(List<Outfit> outfits) {
        gridPane.getChildren().clear();
        for (int i = 0; i < outfits.size(); i++) {
            gridPane.add(getOutfitBlock(outfits.get(i)), CoreUtils.getColumnByIndexAndColumnNum(i, COLUMN_NUM),
                    CoreUtils.getRowByIndexAndColumnNum(i, COLUMN_NUM));
        }
    }

    private Node getOutfitBlock(Outfit outfit) {
        return new Pane();
    }

    private void openOutfitDialog(Outfit outfit) {
        OrganizerWindowCreator.getInstance().openOutfitDialog(new OutfitDialogController(outfit));
    }

    private void adjustSearchPane() {
        List<CheckBox> seasonCheckBoxes = new ArrayList<>();
        List<CheckBox> purposeCheckBoxes = new ArrayList<>();
        searchPane.getChildren().addAll(getPaneWithHeight(30), new Label("Seasons:"), getPaneWithHeight(10));
        for (Season season : Season.values()) {
            CheckBox checkBox = new CheckBox(season.getTitle());
            seasonCheckBoxes.add(checkBox);
            searchPane.getChildren().addAll(checkBox, getPaneWithHeight(10));
        }
        searchPane.getChildren().addAll(new Label("Purposes:"), getPaneWithHeight(10));
        for (Purpose purpose : Purpose.values()) {
            CheckBox checkBox = new CheckBox(purpose.getTitle());
            purposeCheckBoxes.add(checkBox);
            searchPane.getChildren().addAll(checkBox, getPaneWithHeight(10));
        }
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> {
            List<Season> selectedSeasons = new ArrayList<>();
            List<Purpose> selectedPurposes = new ArrayList<>();
            for (CheckBox checkBox : seasonCheckBoxes)
                if (checkBox.isSelected())
                    selectedSeasons.add(Season.getByTitleOnNull(checkBox.getText()));
            for (CheckBox checkBox : purposeCheckBoxes)
                if (checkBox.isSelected())
                    selectedPurposes.add(Purpose.getByTitleOnNull(checkBox.getText()));
            fillGridPane(Core.getInstance().wardrobeService().getOutfitsBySeasonsAndPurposes(selectedSeasons, selectedPurposes));
        });
        searchPane.getChildren().add(searchButton);
    }
}
