package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

class PiecesController implements FxController {
    @FXML
    private Button newPieceButton;

    @FXML
    private void initialize() {
        newPieceButton.setOnAction(event -> openPieceEditWindow(null));
    }

    private void openPieceEditWindow(Piece piece) {
        OrganizerWindowCreator.getInstance().openPieceEditDialog(new PieceEditDialogController(piece, this::showPiece));
    }

    private void showPiece(Piece piece) {

    }
}
