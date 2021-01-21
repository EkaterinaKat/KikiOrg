package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.kikiorg.view.controller.AbstractSwitchController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
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
    private Button addOutfitButton;
    @FXML
    private Pane mainPane;

    private Node outfitsNode;
    private Node piecesNode;
    private Node addOutfitNode;

    private OutfitsController outfitsController = new OutfitsController();
    private PiecesController piecesController = new PiecesController();
    private AddOutfitController addOutfitController = new AddOutfitController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(outfitsButton, piecesButton, addOutfitButton));
        outfitsButton.setOnAction(event -> outfitsButtonListener());
        piecesButton.setOnAction(event -> piecesButtonListener());
        addOutfitButton.setOnAction(event -> addOutfitButtonListener());
        outfitsButtonListener();
    }

    private void outfitsButtonListener() {
        activateMode(outfitsButton, outfitsNode, OrganizerWindowCreator.getInstance()::getOutfitsNode, outfitsController);
    }

    private void piecesButtonListener() {
        activateMode(piecesButton, piecesNode, OrganizerWindowCreator.getInstance()::getPiecesNode, piecesController);
    }

    private void addOutfitButtonListener() {
        activateMode(addOutfitButton, addOutfitNode, OrganizerWindowCreator.getInstance()::getAddOutfitNode, addOutfitController);
    }
}
