package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.ImageAndFileNameContainer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.FxUtils.*;
import static com.katyshevtseva.fx.ImageSizeUtil.placeImageInSquare;
import static com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.toImageUrlAndFileNameContainer;
import static com.katyshevtseva.kikiorg.view.utils.OrgUtils.setDate;

class PieceDialogController implements FxController {
    private ImageAndFileNameContainer selectedImage;
    private List<CheckBox> seasonsCheckBoxes = new ArrayList<>();
    private List<CheckBox> purposesCheckBoxes = new ArrayList<>();
    private Piece existing;
    private OneArgKnob<Piece> onSaveListener;
    @FXML
    private Pane imagePane;
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

    PieceDialogController(Piece existing, OneArgKnob<Piece> onSaveListener) {
        this.existing = existing;
        this.onSaveListener = onSaveListener;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(saveButton, clothesTypeComboBox, descTextArea);
        saveButton.setOnAction(event -> save());
        setComboBoxItems(clothesTypeComboBox, ClothesType.values());
        adjustCheckBoxPanes();
        showImage(new Image("images/piece_creation_plus.png"));
        setExistingPieceInfo();
    }

    private void save() {
        Piece saved = Core.getInstance().wardrobeService().savePiece(
                existing,
                descTextArea.getText().trim(),
                selectedImage.getFileName(),
                clothesTypeComboBox.getValue(),
                getDate(startDatePicker),
                getDate(endDatePicker),
                getSelectedSeasons(),
                getSelectedPurposes());
        onSaveListener.execute(saved);
        closeWindowThatContains(clothesTypeComboBox);
    }

    private void openImageSelectionDialog() {
        new StandardDialogBuilder().openImageSelectionDialog(
                WrdImageUtils.getFreeImagesForPieceCreation(),
                imageContainer -> {
                    selectedImage = (ImageAndFileNameContainer) imageContainer;
                    showImage(selectedImage.getImage());
                });
    }

    private void showImage(Image image) {
        imagePane.getChildren().clear();
        Node imageNode = placeImageInSquare(new ImageView(image), 400);
        imageNode.setOnMouseClicked(event -> openImageSelectionDialog());
        imagePane.getChildren().add(imageNode);
    }

    private void setExistingPieceInfo() {
        if (existing != null) {
            showImage(WrdImageUtils.getImageByPiece(existing));
            descTextArea.setText(existing.getDescription());
            clothesTypeComboBox.setValue(existing.getType());
            selectedImage = toImageUrlAndFileNameContainer(existing);
            setDate(startDatePicker, existing.getStartDate());
            setDate(endDatePicker, existing.getEndDate());
        }
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
