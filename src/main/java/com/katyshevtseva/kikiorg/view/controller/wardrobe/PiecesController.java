package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.GalleryController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.ImageNameAndPieceContainer;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

class PiecesController implements FxController {
    private final WardrobeService service = Core.getInstance().wardrobeService();
    private GalleryController galleryController;
    @FXML
    private Button pieceCreateButton;
    @FXML
    private ImageView imageView;
    @FXML
    private Label infoLabel;
    @FXML
    private Button editButton;
    @FXML
    private VBox galleryPane;

    @FXML
    private void initialize() {
        tunePiecesGallery();
        pieceCreateButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openPieceEditDialog(
                        new PieceEditDialogController(null, piece -> {
                            galleryController.setImageContainers(WrdImageUtils.toExtendedImageContainers(service.getAllPieces()));
                            showPieceFullInfo(piece);
                        })));
    }

    private void showPieceFullInfo(Piece piece) {
        imageView.setImage(WrdImageUtils.getImageByPiece(piece));
        infoLabel.setText(piece.getFullDesc());
        editButton.setVisible(true);
        editButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openPieceEditDialog(
                        new PieceEditDialogController(piece, savedPiece -> {
                            galleryController.setImageContainers(WrdImageUtils.toExtendedImageContainers(service.getAllPieces()));
                            showPieceFullInfo(savedPiece);
                        })));
    }

    private void tunePiecesGallery() {
        Component<GalleryController> component = new ComponentBuilder().setSize(new Size(800, 750))
                .getGalleryComponent(3,
                        WrdImageUtils.toExtendedImageContainers(service.getAllPieces()),
                        imageContainer -> showPieceFullInfo(((ImageNameAndPieceContainer) imageContainer).getPiece()));

        galleryController = component.getController();
        galleryPane.getChildren().add(component.getNode());
    }
}
