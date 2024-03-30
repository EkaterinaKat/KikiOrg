package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxImageCreationUtil;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.MultipleChoiceController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Category;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.utils.WrdImageUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.katyshevtseva.fx.FxUtils.*;
import static com.katyshevtseva.fx.ImageSizeUtil.placeImageInSquare;
import static com.katyshevtseva.kikiorg.view.utils.OrgUtils.setDate;

class PieceDialogController implements FxController {
    private ImageContainer selectedImage;
    private final Piece existing;
    private final OneArgKnob<Piece> onSaveListener;
    MultipleChoiceController<Category> categoryController;
    @FXML
    private Pane imagePane;
    @FXML
    private TextArea descTextArea;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<PieceSubtype> typeComboBox;
    @FXML
    private Pane categoryPane;
    @FXML
    private Button saveButton;

    PieceDialogController(Piece existing, OneArgKnob<Piece> onSaveListener) {
        this.existing = existing;
        this.onSaveListener = onSaveListener;
    }

    @FXML
    private void initialize() {
        associateButtonWithControls(saveButton, typeComboBox, descTextArea);
        saveButton.setOnAction(event -> save());
        setComboBoxItems(typeComboBox, PieceSubtype.getSortedByTitleValues());
        tuneCategoryPane();
        showImage(FxImageCreationUtil.getIcon(FxImageCreationUtil.IconPicture.GREY_PLUS));
        setExistingPieceInfo();
    }

    private void tuneCategoryPane() {
        categoryPane.getChildren().clear();

        List<Category> initCategoryList = existing == null ? new ArrayList<>(Collections.singletonList(Category.GOING_OUT)) :
                new ArrayList<>(existing.getCategories());

        Component<MultipleChoiceController<Category>> categoryComponent = new ComponentBuilder()
                .setSize(new Size(150, 300))
                .getMultipleChoiceComponent(Arrays.asList(Category.values()), initCategoryList, true);

        categoryController = categoryComponent.getController();
        categoryPane.getChildren().add(categoryComponent.getNode());
    }

    private void save() {
        Piece saved = Core.getInstance().wardrobeService.savePiece(
                existing,
                descTextArea.getText().trim(),
                selectedImage.getFileName(),
                typeComboBox.getValue(),
                categoryController.getSelectedItems(),
                getDate(startDatePicker),
                getDate(endDatePicker));
        onSaveListener.execute(saved);
        closeWindowThatContains(typeComboBox);
    }

    private void openImageSelectionDialog() {
        new StandardDialogBuilder().openImageSelectionDialog(
                WrdImageUtils.getFreeImagesForPieceCreation(),
                imageContainer -> {
                    selectedImage = imageContainer;
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
            ImageContainer imageContainer = WrdImageUtils.getImageContainer(existing);

            showImage(imageContainer.getImage());
            descTextArea.setText(existing.getDescription());
            typeComboBox.setValue(existing.getType());
            selectedImage = imageContainer;
            setDate(startDatePicker, existing.getStartDate());
            setDate(endDatePicker, existing.getEndDate());
            startDatePicker.setDisable(true);
            endDatePicker.setDisable(true);
        }
    }
}
