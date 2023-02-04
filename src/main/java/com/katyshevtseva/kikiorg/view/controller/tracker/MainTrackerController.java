package com.katyshevtseva.kikiorg.view.controller.tracker;

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

public class MainTrackerController extends AbstractSwitchController {
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
                new Section("Board", new BoardController(), windowCreator()::getBoardNode),
                new Section("Projects", new ProjectController(), windowCreator()::getProjectsNode));
    }

    private void placeButton(Button button) {
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), button);
    }
}
