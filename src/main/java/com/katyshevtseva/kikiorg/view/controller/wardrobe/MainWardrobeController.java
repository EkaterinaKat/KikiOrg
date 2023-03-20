package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.switchcontroller.AbstractSwitchController;
import com.katyshevtseva.fx.switchcontroller.Section;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.*;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

public class MainWardrobeController extends AbstractSwitchController implements SectionController {
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
                new Section("Statistics", new StatisticsController(),
                        controller -> windowCreator().getNode(WARDROBE_STATISTICS, controller)),
                new Section("Pieces", new PieceController(),
                        controller -> windowCreator().getNode(PIECE, controller)),
                new Section("Outfits", new OutfitController(),
                        controller -> windowCreator().getNode(OUTFIT, controller)));
    }

    private void placeButton(Button button) {
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), button);
    }
}
