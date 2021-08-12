package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.AbstractSwitchController;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainWardrobeController extends AbstractSwitchController implements FxController {
    @FXML
    private Button outfitsButton;
    @FXML
    private Button piecesButton;
    @FXML
    private Pane mainPane;

    private Node outfitsNode;
    private Node piecesNode;

    private OutfitController outfitController = new OutfitController();
    private PiecesController piecesController = new PiecesController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(outfitsButton, piecesButton));
        outfitsButton.setOnAction(event -> outfitsButtonListener());
        piecesButton.setOnAction(event -> piecesButtonListener());
        outfitsButtonListener();
    }

    private void outfitsButtonListener() {
        activateMode(outfitsButton, outfitsNode, OrganizerWindowCreator.getInstance()::getOutfitsNode, outfitController);
    }

    private void piecesButtonListener() {
        activateMode(piecesButton, piecesNode, OrganizerWindowCreator.getInstance()::getPiecesNode, piecesController);
    }
}
