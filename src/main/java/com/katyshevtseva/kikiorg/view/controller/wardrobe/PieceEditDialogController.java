package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Purpose;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class PieceEditDialogController implements FxController {
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea descTextArea;
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
    private Piece piece;
    private PieceSavingHandler pieceSavingHandler;

    PieceEditDialogController(Piece piece, PieceSavingHandler pieceSavingHandler) {
        this.piece = piece;
        this.pieceSavingHandler = pieceSavingHandler;
    }

    @FXML
    private void initialize() {
        Utils.associateButtonWithControls(saveButton, clothesTypeComboBox, descTextArea);
        saveButton.setOnAction(event -> save());
        clothesTypeComboBox.setItems(FXCollections.observableArrayList(ClothesType.values()));
        adjustCheckBoxPanes();
        adjustImageAdding();
        setExistingPieceInfo();
    }

    private void setExistingPieceInfo() {
        if (piece != null) {
            imageView.setImage(ImageService.getJavafxImageByImagable(piece));
            descTextArea.setText(piece.getDescription());
            clothesTypeComboBox.setValue(piece.getType());
            selectedImageName = piece.getImageName();
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
        if (piece == null)
            piece = new Piece();
        piece.setImageName(selectedImageName);
        piece.setDescription(descTextArea.getText().trim());
        piece.setType(clothesTypeComboBox.getValue());
        piece.setPurposes(getSelectedPurposes());
        piece.setSeasons(getSelectedSeasons());
        Core.getInstance().wardrobeService().savePiece(piece);

        pieceSavingHandler.execute(piece);
        Utils.closeWindowThatContains(clothesTypeComboBox);
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
            seasonsPane.getChildren().addAll(checkBox, Utils.getPaneWithHeight(10));
            if (piece != null)
                checkBox.setSelected(piece.getSeasons().contains(season));
        }

        for (Purpose purpose : Purpose.values()) {
            CheckBox checkBox = new CheckBox(purpose.getTitle());
            purposesCheckBoxes.add(checkBox);
            purposesPane.getChildren().addAll(checkBox, Utils.getPaneWithHeight(10));
            if (piece != null)
                checkBox.setSelected(piece.getPurposes().contains(purpose));
        }
    }

    private void setEmptyImage() {
        imageView.setImage(new Image("piece_creation_plus.png"));
    }

    interface PieceSavingHandler {
        void execute(Piece piece);
    }
}
