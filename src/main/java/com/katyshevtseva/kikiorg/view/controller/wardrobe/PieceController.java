package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.GalleryController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService.PieceFilter;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.ClothesType;
import com.katyshevtseva.kikiorg.view.controller.pagination.PaginationPaneController;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.WrdImageUtils.ImageAndPieceContainer;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
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
    private ComboBox<ClothesType> typeComboBox;
    @FXML
    private Button showAllButton;
    @FXML
    private Button archiveButton;
    @FXML
    private ComboBox<PieceFilter> filterComboBox;

    @FXML
    private void initialize() {
        FxUtils.setComboBoxItems(filterComboBox, PieceFilter.values());
        filterComboBox.setValue(PieceFilter.ACTIVE);
        filterComboBox.setOnAction(event -> paginationPaneController.loadPage());
        tunePiecesGallery();
        pieceCreateButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openPieceEditDialog(
                        new PieceDialogController(null, piece -> {
                            paginationPaneController.loadPage();
                            showPieceFullInfo(piece);
                        })));
        tunePagination();
        FxUtils.setComboBoxItems(typeComboBox, ClothesType.getSortedByTitleValues());
        typeComboBox.setOnAction(event -> paginationPaneController.loadPage());
        showAllButton.setOnAction(event -> {
            typeComboBox.setValue(null);
            paginationPaneController.loadPage();
        });
    }

    private void tunePagination() {
        paginationPaneController = new PaginationPaneController<>(this::getPiecePage, this::setContent);
        paginationPane.getChildren().add(OrganizerWindowCreator.getInstance().getPaginationPaneNode(paginationPaneController));
    }

    private Page<Piece> getPiecePage(int pageNum) {
        return service.getPiecePage(pageNum, typeComboBox.getValue(), filterComboBox.getValue());
    }

    private void showPieceFullInfo(Piece piece) {
        imagePane.getChildren().clear();
        imagePane.getChildren().add(placeImageInSquare(new ImageView(ImageCreator.getInstance().getImageContainer(piece).getImage()), 350));
        infoLabel.setText(piece.getFullDesc());
        editButton.setVisible(true);
        archiveButton.setVisible(true);
        editButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openPieceEditDialog(
                        new PieceDialogController(piece, savedPiece -> {
                            paginationPaneController.loadPage();
                            showPieceFullInfo(savedPiece);
                        })));
        archiveButton.setOnAction(event -> {
                    boolean availableForArchive = service.pieceAvailableForArchive(piece);
                    if (availableForArchive) {
                        new StandardDialogBuilder().openQuestionDialog("Archive?", b -> {
                            if (b) {
                                service.archivePiece(piece);
                                paginationPaneController.loadPage();
                            }
                        });
                    } else {
                        new StandardDialogBuilder().openInfoDialog("Невозможно архивировать используемую вещь");
                    }
                }
        );
        archiveButton.setDisable(piece.getEndDate() != null);
    }

    private void setContent(List<Piece> pieces) {
        galleryController.setImageContainers(WrdImageUtils.toImageUrlAndPieceContainers(pieces));
        clearPieceInfo();
    }

    private void clearPieceInfo() {
        imagePane.getChildren().clear();
        infoLabel.setText("");
        editButton.setVisible(false);
        archiveButton.setVisible(false);
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
