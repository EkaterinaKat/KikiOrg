package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.ImageContainer;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.GalleryController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.view.controller.pagination.PaginationPaneController;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.ImageAndPieceContainer;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.katyshevtseva.fx.ImageSizeUtil.placeImageInSquare;

class PieceController implements FxController {
    private final WardrobeService service = Core.getInstance().wardrobeService();
    private GalleryController galleryController;
    private PaginationPaneController<Piece> paginationPaneController;
    @FXML
    private Button pieceCreateButton;
    @FXML
    private Pane imagePane;
    @FXML
    private Label infoLabel;
    @FXML
    private Button editButton;
    @FXML
    private VBox galleryPane;
    @FXML
    private Pane paginationPane;

    @FXML
    private void initialize() {
        tunePiecesGallery();
        pieceCreateButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openPieceEditDialog(
                        new PieceDialogController(null, piece -> {
                            paginationPaneController.loadPage();
                            showPieceFullInfo(piece);
                        })));
        tunePagination();
    }

    private void tunePagination() {
        paginationPaneController = new PaginationPaneController<>(service::getPiecePage, this::setContent);
        paginationPane.getChildren().add(OrganizerWindowCreator.getInstance().getPaginationPaneNode(paginationPaneController));
    }

    private void showPieceFullInfo(Piece piece) {
        imagePane.getChildren().clear();

        if (piece == null) {
            infoLabel.setText("");
            editButton.setVisible(false);
            return;
        }

        imagePane.getChildren().add(placeImageInSquare(new ImageView(ImageCreator.getInstance().getImageContainer(piece).getImage()), 350));
        infoLabel.setText(piece.getFullDesc());
        editButton.setVisible(true);
        editButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openPieceEditDialog(
                        new PieceDialogController(piece, savedPiece -> {
                            paginationPaneController.loadPage();
                            showPieceFullInfo(savedPiece);
                        })));
    }

    private void setContent(List<Piece> pieces) {
        List<ImageContainer> imageContainers = WrdImageUtils.toImageUrlAndPieceContainers(pieces);
        Collections.reverse(imageContainers);
        galleryController.setImageContainers(imageContainers);
        showPieceFullInfo(null);
    }

    private void tunePiecesGallery() {
        Component<GalleryController> component = new ComponentBuilder().setSize(new Size(750, 750))
                .getGalleryComponent(3,
                        WrdImageUtils.toImageUrlAndPieceContainers(new ArrayList<>()),
                        imageContainer -> showPieceFullInfo(((ImageAndPieceContainer) imageContainer).getPiece()));

        galleryController = component.getController();
        galleryPane.getChildren().add(component.getNode());
    }
}
