package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.ImageAndFileNameContainer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import static com.katyshevtseva.fx.FxUtils.*;
import static com.katyshevtseva.fx.ImageSizeUtil.placeImageInSquare;
import static com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.toImageUrlAndFileNameContainer;
import static com.katyshevtseva.kikiorg.view.utils.OrgUtils.setDate;

class PieceDialogController implements FxController {
    private ImageAndFileNameContainer selectedImage;
    private final Piece existing;
    private final OneArgKnob<Piece> onSaveListener;
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
    private Button saveButton;

    PieceDialogController(Piece existing, OneArgKnob<Piece> onSaveListener) {
        this.existing = existing;
        this.onSaveListener = onSaveListener;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(saveButton, clothesTypeComboBox, descTextArea);
        saveButton.setOnAction(event -> save());
        setComboBoxItems(clothesTypeComboBox,
                Arrays.stream(ClothesType.values()).sorted(Comparator.comparing(ClothesType::getTitle)).collect(Collectors.toList()));
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
                getDate(endDatePicker));
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
            showImage(ImageCreator.getInstance().getImageContainer(existing).getImage());
            descTextArea.setText(existing.getDescription());
            clothesTypeComboBox.setValue(existing.getType());
            selectedImage = toImageUrlAndFileNameContainer(existing);
            setDate(startDatePicker, existing.getStartDate());
            setDate(endDatePicker, existing.getEndDate());
        }
    }
}
