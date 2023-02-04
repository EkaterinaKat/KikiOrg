package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.switchcontroller.AbstractSwitchController;
import com.katyshevtseva.fx.switchcontroller.Section;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

import static com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator.windowCreator;

public class MainWardrobeController extends AbstractSwitchController {
    @FXML
    private Pane mainPane;
    @FXML
    private HBox buttonBox;

    @FXML
    private void initialize() {
        init(getSections(), mainPane, this::placeButton);
    }

    private List<Section> getSections() {
        return Arrays.asList(
                new Section("Statistics", new StatisticsController(), windowCreator()::getWardrobeStatisticsNode),
                new Section("Pieces", new PieceController(), windowCreator()::getPiecesNode),
                new Section("Outfits", new OutfitController(), windowCreator()::getOutfitsNode));
    }

    private void placeButton(Button button) {
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), button);
    }
}
