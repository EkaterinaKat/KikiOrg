package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtceva.collage.logic.Collage;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;
import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.kikiorg.view.controller.wardrobe.CollageUtils.COLLAGE_SIZE;

class OutfitDialogController implements FxController {
    private Outfit existing;
    private OneArgKnob<Outfit> onSaveListener;
    private List<CheckBox> seasonsCheckBoxes = new ArrayList<>();
    private List<CheckBox> purposesCheckBoxes = new ArrayList<>();
    @FXML
    private Pane collagePane;
    @FXML
    private Button componentAddButton;
    @FXML
    private VBox seasonsPane;
    @FXML
    private VBox purposesPane;
    @FXML
    private Button saveButton;

    OutfitDialogController(Outfit existing, OneArgKnob<Outfit> onSaveListener) {
        this.existing = existing;
        this.onSaveListener = onSaveListener;
    }

    @FXML
    private void initialize() {
        adjustCheckBoxPanes();
        saveButton.setOnAction(event -> save());
        setCollagePaneSize();
        tuneCollage();
    }

    private void tuneCollage() {
        Collage collage = CollageUtils.createEmptyCollage();
        collagePane.getChildren().add(collage.getPane());
        componentAddButton.setOnAction(event -> collage.createComponent());
    }

    private void setCollagePaneSize() {
        collagePane.setMinWidth(COLLAGE_SIZE);
        collagePane.setMinHeight(COLLAGE_SIZE);
        collagePane.setMaxWidth(COLLAGE_SIZE);
        collagePane.setMaxHeight(COLLAGE_SIZE);
    }

    private void save() {
        Outfit saved = Core.getInstance().wardrobeService().saveOutfit(
                existing,
                getSelectedSeasons(),
                getSelectedPurposes(),
                null);  //todo
        onSaveListener.execute(saved);
        closeWindowThatContains(saveButton);
    }

    private Set<Season> getSelectedSeasons() {
        return seasonsCheckBoxes.stream()
                .filter(CheckBox::isSelected)
                .map(checkBox -> Season.getByTitleOnNull(checkBox.getText()))
                .collect(Collectors.toSet());
    }

    private Set<Purpose> getSelectedPurposes() {
        return purposesCheckBoxes.stream()
                .filter(CheckBox::isSelected)
                .map(checkBox -> Purpose.getByTitleOnNull(checkBox.getText()))
                .collect(Collectors.toSet());
    }

    private void adjustCheckBoxPanes() {
        for (Season season : Season.values()) {
            CheckBox checkBox = new CheckBox(season.getTitle());
            seasonsCheckBoxes.add(checkBox);
            seasonsPane.getChildren().addAll(checkBox, getPaneWithHeight(10));
            if (existing != null)
                checkBox.setSelected(existing.getSeasons().contains(season));
        }

        for (Purpose purpose : Purpose.values()) {
            CheckBox checkBox = new CheckBox(purpose.getTitle());
            purposesCheckBoxes.add(checkBox);
            purposesPane.getChildren().addAll(checkBox, getPaneWithHeight(10));
            if (existing != null)
                checkBox.setSelected(existing.getPurposes().contains(purpose));
        }
    }
}
