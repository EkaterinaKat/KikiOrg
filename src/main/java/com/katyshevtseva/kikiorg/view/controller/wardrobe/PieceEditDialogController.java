package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.katyshevtseva.fx.FxUtils.*;

class PieceEditDialogController implements FxController {
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea descTextArea;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<ClothesType> clothesTypeComboBox;
    @FXML
    private VBox seasonsPane;
    @FXML
    private VBox purposesPane;
    @FXML
    private Button saveButton;
    private String selectedImageName;
    private List<CheckBox> seasonsCheckBoxes;
    private List<CheckBox> purposesCheckBoxes;
    private Piece existing;
    private PieceSavingHandler pieceSavingHandler;

    PieceEditDialogController(Piece existing, PieceSavingHandler pieceSavingHandler) {
        this.existing = existing;
        this.pieceSavingHandler = pieceSavingHandler;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(saveButton, clothesTypeComboBox, descTextArea);
        saveButton.setOnAction(event -> save());
        setComboBoxItems(clothesTypeComboBox, ClothesType.values());
        adjustCheckBoxPanes();
        adjustImageAdding();
        setExistingPieceInfo();
    }

    private void setExistingPieceInfo() {
        if (existing != null) {
            imageView.setImage(ImageService.getJavafxImageByImagable(existing));
            descTextArea.setText(existing.getDescription());
            clothesTypeComboBox.setValue(existing.getType());
            selectedImageName = existing.getImageName();
            startDatePicker.setValue(existing.getStartDate() != null ?
                    new java.sql.Date(existing.getStartDate().getValue().getTime()).toLocalDate() : null);
            endDatePicker.setValue(existing.getEndDate() != null ?
                    new java.sql.Date(existing.getEndDate().getValue().getTime()).toLocalDate() : null);
        }
    }

    private void adjustImageAdding() {
        setEmptyImage();
        imageView.setOnMouseClicked(event -> {
            OrganizerWindowCreator.getInstance().openImageSelectionDialog(new ImageSelectionDialogController(
                    ImageService.getFreeImages(Core.getInstance().wardrobeService().getAllPieces()),
                    imagable -> {
                        selectedImageName = imagable.getImageName();
                        imageView.setImage(ImageService.getJavafxImageByImagable(imagable));
                    }));
        });
    }

    private void save() {
        Piece saved = Core.getInstance().wardrobeService().savePiece(
                existing,
                selectedImageName,
                descTextArea.getText().trim(),
                startDatePicker.getValue() != null ? Date.valueOf(startDatePicker.getValue()) : null,
                endDatePicker.getValue() != null ? Date.valueOf(endDatePicker.getValue()) : null,
                clothesTypeComboBox.getValue(),
                getSelectedPurposes(),
                getSelectedSeasons());

        pieceSavingHandler.execute(saved);
        closeWindowThatContains(clothesTypeComboBox);
    }

    private Set<Season> getSelectedSeasons() {
        Set<Season> selectedSeasons = new HashSet<>();
        for (CheckBox checkBox : seasonsCheckBoxes)
            if (checkBox.isSelected())
                selectedSeasons.add(Season.getByTitleOnNull(checkBox.getText()));
        return selectedSeasons;
    }

    private Set<Purpose> getSelectedPurposes() {
        Set<Purpose> selectedPurposes = new HashSet<>();
        for (CheckBox checkBox : purposesCheckBoxes)
            if (checkBox.isSelected())
                selectedPurposes.add(Purpose.getByTitleOnNull(checkBox.getText()));
        return selectedPurposes;
    }

    private void adjustCheckBoxPanes() {
        seasonsPane.getChildren().clear();
        purposesPane.getChildren().clear();

        seasonsCheckBoxes = new ArrayList<>();
        purposesCheckBoxes = new ArrayList<>();

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

    private void setEmptyImage() {
        imageView.setImage(new Image("images/piece_creation_plus.png"));
    }

    interface PieceSavingHandler {
        void execute(Piece piece);
    }
}
