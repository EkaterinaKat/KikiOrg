package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtceva.collage.logic.Collage;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Outfit;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;
import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.kikiorg.view.controller.wardrobe.CollageUtils.*;
import static com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.toCollageImages;
import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.CLOTHES_TYPE_SELECT_DIALOG_SIZE;

class OutfitDialogController implements FxController {
    private final WardrobeService service = Core.getInstance().wardrobeService();
    private final Outfit existing;
    private final OneArgKnob<Outfit> onSaveListener;
    private final List<CheckBox> seasonsCheckBoxes = new ArrayList<>();
    private final List<CheckBox> purposesCheckBoxes = new ArrayList<>();
    private Collage collage;
    @FXML
    private Pane collagePane;
    @FXML
    private Button componentAddButton;
    @FXML
    private TextArea commentTextArea;
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
        setSaveButtonAbility();

        if (existing != null) {
            commentTextArea.setText(existing.getComment());
        }
    }

    private void setSaveButtonAbility() {
        boolean purposeSelected = false;
        for (CheckBox checkBox : purposesCheckBoxes) {
            if (checkBox.isSelected()) {
                purposeSelected = true;
            }
        }

        boolean seasonSelected = false;
        for (CheckBox checkBox : seasonsCheckBoxes) {
            if (checkBox.isSelected()) {
                seasonSelected = true;
            }
        }

        saveButton.setDisable(!purposeSelected || !seasonSelected);
    }

    private void tuneCollage() {
        collage = existing == null ? createEmptyCollage() : reproduceCollage(existing.getCollageEntity());
        collagePane.getChildren().add(collage.getPane());
        componentAddButton.setOnAction(event -> openPieceTypeSelectDialog());
    }

    private void openPieceTypeSelectDialog() {
        new StandardDialogBuilder()
                .setSize(CLOTHES_TYPE_SELECT_DIALOG_SIZE)
                .setTitle("Select type")
                .openNoFxmlContainerDialog(new TypeSelectDialogController(type ->
                        collage.openImageToAddSelectionDialog(toCollageImages(service.getPiecesToAddToOutfit(type)))));
    }

    private void setCollagePaneSize() {
        collagePane.setMinWidth(COLLAGE_SIZE);
        collagePane.setMinHeight(COLLAGE_SIZE);
        collagePane.setMaxWidth(COLLAGE_SIZE);
        collagePane.setMaxHeight(COLLAGE_SIZE);
    }

    private void save() {
        Outfit saved = service.saveOutfit(
                existing,
                commentTextArea.getText(),
                getSelectedSeasons(),
                getSelectedPurposes(),
                CollageUtils.saveCollage(existing != null ? existing.getCollageEntity() : null, collage));
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
            checkBox.setOnAction(event -> setSaveButtonAbility());
            seasonsCheckBoxes.add(checkBox);
            seasonsPane.getChildren().addAll(checkBox, getPaneWithHeight(10));
            if (existing != null)
                checkBox.setSelected(existing.getSeasons().contains(season));
        }

        for (Purpose purpose : Purpose.values()) {
            CheckBox checkBox = new CheckBox(purpose.getTitle());
            checkBox.setOnAction(event -> setSaveButtonAbility());
            purposesCheckBoxes.add(checkBox);
            purposesPane.getChildren().addAll(checkBox, getPaneWithHeight(10));
            if (existing != null)
                checkBox.setSelected(existing.getPurposes().contains(purpose));
        }
    }
}
