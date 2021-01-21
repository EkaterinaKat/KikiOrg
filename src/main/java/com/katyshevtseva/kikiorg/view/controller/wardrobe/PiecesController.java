package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.CoreUtils;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.List;


class PiecesController implements FxController {
    private static final int IMAGE_SIZE = 184;
    private static final int COLUMN_NUM = 3;
    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView imageView;
    @FXML
    private Label infoLabel;
    @FXML
    private Button editButton;
    private List<Piece> pieces;

    @FXML
    private void initialize() {
        loadDataAndFillGridPane();
        showPiece(pieces.get(0));
    }

    private void loadDataAndFillGridPane() {
        pieces = Core.getInstance().wardrobeService().getAllPieces();
        gridPane.getChildren().clear();
        gridPane.add(getNewPieceImageView(), 0, 0);
        for (int i = 0; i < pieces.size(); i++) {
            Piece piece = pieces.get(i);
            ImageView imageView = new ImageView();
            imageView.setFitWidth(IMAGE_SIZE);
            imageView.setFitHeight(IMAGE_SIZE);
            imageView.setImage(ImageService.getJavafxImageByImagable(piece));
            imageView.setOnMouseClicked(event -> showPiece(piece));
            gridPane.add(imageView, CoreUtils.getColumnByIndexAndColumnNum(i + 1, COLUMN_NUM),
                    CoreUtils.getRowByIndexAndColumnNum(i + 1, COLUMN_NUM));
        }
    }

    private void openPieceEditWindow(Piece piece) {
        OrganizerWindowCreator.getInstance().openPieceEditDialog(new PieceEditDialogController(piece, piece1 -> {
            loadDataAndFillGridPane();
            showPiece(piece1);
        }));
    }

    private ImageView getNewPieceImageView() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(IMAGE_SIZE);
        imageView.setFitHeight(IMAGE_SIZE);
        imageView.setImage(new Image("piece_creation_plus.png"));
        imageView.setOnMouseClicked(event -> openPieceEditWindow(null));
        return imageView;
    }

    private void showPiece(Piece piece) {
        imageView.setImage(ImageService.getJavafxImageByImagable(piece));
        infoLabel.setText(piece.getFullDesc());
        editButton.setOnAction(event -> openPieceEditWindow(piece));
    }
}
