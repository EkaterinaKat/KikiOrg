package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxImageCreationUtil;
import com.katyshevtseva.fx.FxImageCreationUtil.IconPicture;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.GalleryController;
import com.katyshevtseva.fx.component.controller.PaginationPaneController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.general.Page;
import com.katyshevtseva.image.ImageContainer;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeService.PieceFilter;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.Season;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.utils.WrdImageUtils;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.utils.WrdImageUtils.ImageAndPieceContainer;
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
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.PIECE;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.OUTFIT_GRID;
import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.CLOTHES_TYPE_SELECT_DIALOG_SIZE;

class PieceController implements SectionController {
    private final WardrobeService service = Core.getInstance().wardrobeService();
    private GalleryController galleryController;
    private PaginationPaneController<Piece> paginationPaneController;
    private ClothesType clothesType;
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
    private Label typeLabel;
    @FXML
    private Button showAllButton;
    @FXML
    private Button archiveButton;
    @FXML
    private Button showOutfitsButton;
    @FXML
    private ComboBox<PieceFilter> filterComboBox;

    @FXML
    private void initialize() {
        FxUtils.setComboBoxItems(filterComboBox, PieceFilter.values());
        filterComboBox.setValue(PieceFilter.ACTIVE);
        filterComboBox.setOnAction(event -> paginationPaneController.loadPage());
        tunePiecesGallery();
        pieceCreateButton.setOnAction(event ->
                WindowBuilder.openDialog(PIECE,
                        new PieceDialogController(null, piece -> {
                            paginationPaneController.loadPage();
                            showPieceFullInfo(piece);
                        })));
        tunePagination();
        adjustTypeLabel();
        showAllButton.setOnAction(event -> {
            typeLabel.setText("Select clothes type");
            clothesType = null;
            paginationPaneController.loadPage();
        });
    }

    private void adjustTypeLabel() {
        typeLabel.setOnMouseClicked(event -> new StandardDialogBuilder()
                .setSize(CLOTHES_TYPE_SELECT_DIALOG_SIZE)
                .setTitle("Select type")
                .openNoFxmlContainerDialog(
                        new TypeSelectDialogController(type -> {
                            clothesType = type;
                            typeLabel.setText(type.getTitle());
                            paginationPaneController.loadPage();
                        })));
    }

    private void tunePagination() {
        Component<PaginationPaneController<Piece>> component =
                new ComponentBuilder().getPaginationComponent(this::getPiecePage, this::setContent);
        paginationPaneController = component.getController();
        paginationPane.getChildren().add(component.getNode());
    }

    private Page<Piece> getPiecePage(int pageNum) {
        return service.getPiecePage(pageNum, clothesType, filterComboBox.getValue());
    }

    private void showPieceFullInfo(Piece piece) {
        ImageContainer imageContainer = WrdImageUtils.getImageContainer(piece);

        imagePane.getChildren().clear();
        imagePane.getChildren().add(placeImageInSquare(new ImageView(imageContainer.getImage()), 350));
        infoLabel.setText(piece.getFullDesc());
        editButton.setVisible(true);
        archiveButton.setVisible(true);
        showOutfitsButton.setVisible(true);
        showOutfitsButton.setOnAction(event -> {
            OutfitGridController outfitGridController = new OutfitGridController(pageNum -> service.getOutfitsByPiece(pageNum, piece));
            new StandardDialogBuilder().setSize(920, 1200).openNodeContainerDialog(
                    WindowBuilder.getNode(OUTFIT_GRID, outfitGridController));
        });
        editButton.setOnAction(event ->
                WindowBuilder.openDialog(PIECE,
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
        if (piece.getEndDate() == null) {
            archiveButton.setText("Archive");
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
        } else {
            archiveButton.setText("Return to work");
            archiveButton.setOnAction(event -> {
                        service.returnToWork(piece);
                        paginationPaneController.loadPage();
                    }
            );
        }
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
        showOutfitsButton.setVisible(false);
    }

    private void tunePiecesGallery() {
        Component<GalleryController> component = new ComponentBuilder().setSize(new Size(750, 750))
                .getGalleryComponent(3,
                        WrdImageUtils.toImageUrlAndPieceContainers(new ArrayList<>()),
                        imageContainer -> showPieceFullInfo(((ImageAndPieceContainer) imageContainer).getPiece()));

        galleryController = component.getController();
        galleryPane.getChildren().add(component.getNode());
        galleryController.setIconSupplier(this::getSeasonImageView);
    }

    private ImageView getSeasonImageView(ImageContainer imageContainer) {
        IconPicture iconPicture = null;
        Season season = Core.getInstance().pieceSeasonService()
                .getPieceSeasonOrNull(((ImageAndPieceContainer) imageContainer).getPiece());

        if (season == null) {
            return null;
        }

        switch (season) {
            case W:
                iconPicture = IconPicture.WINTER;
                break;
            case S:
                iconPicture = IconPicture.SUMMER;
                break;
            case DS:
                iconPicture = IconPicture.AUTUMN;
                break;
            case N:
                return null;
        }

        return new ImageView(FxImageCreationUtil.getIcon(iconPicture));
    }
}
