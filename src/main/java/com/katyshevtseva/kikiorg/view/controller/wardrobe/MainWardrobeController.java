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
    private Button statisticsButton;
    @FXML
    private Pane mainPane;

    private Node outfitsNode;
    private Node piecesNode;
    private Node statisticsNode;

    private final OutfitController outfitController = new OutfitController();
    private final PieceController pieceController = new PieceController();
    private final StatisticsController statisticsController = new StatisticsController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(outfitsButton, piecesButton, statisticsButton));
        outfitsButton.setOnAction(event -> outfitsButtonListener());
        piecesButton.setOnAction(event -> piecesButtonListener());
        statisticsButton.setOnAction(event -> statisticsButtonListener());
        statisticsButtonListener();
    }

    private void outfitsButtonListener() {
        activateMode(outfitsButton, outfitsNode, OrganizerWindowCreator.getInstance()::getOutfitsNode, outfitController);
    }

    private void piecesButtonListener() {
        activateMode(piecesButton, piecesNode, OrganizerWindowCreator.getInstance()::getPiecesNode, pieceController);
    }

    private void statisticsButtonListener() {
        activateMode(statisticsButton, statisticsNode, OrganizerWindowCreator.getInstance()::getWardrobeStatisticsNode, statisticsController);
    }
}
