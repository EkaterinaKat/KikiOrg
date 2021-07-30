package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.GalleryController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.ExtendedImageContainer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

class PiecesController implements FxController {
    private final WardrobeService service = Core.getInstance().wardrobeService();
    private List<Piece> pieces;
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
        pieces = service.getAllPieces();
        tunePiecesGallery();
        if (!pieces.isEmpty())
            showPieceFullInfo(pieces.get(0));
    }

    private void showPieceFullInfo(Piece piece) {
        imageView.setImage(WrdImageUtils.getImageByPiece(piece));
        infoLabel.setText(piece.getFullDesc());
    }

    private void tunePiecesGallery() {
        Component<GalleryController> component = new ComponentBuilder().setSize(new Size(700, 750))
                .getGalleryComponent(3,
                        WrdImageUtils.toExtendedImageContainers(pieces),
                        imageContainer -> showPieceFullInfo(((ExtendedImageContainer) imageContainer).getPiece()));

        galleryPane.getChildren().add(component.getNode());
    }
}
