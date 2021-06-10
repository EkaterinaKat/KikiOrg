package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.controller.AbstractSwitchController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainTrackerController extends AbstractSwitchController implements FxController {
    @FXML
    private Button boardButton;
    @FXML
    private Button projectsButton;
    @FXML
    private Pane mainPane;

    private Node boardNode;
    private Node projectsNode;

    private BoardController boardController = new BoardController();
    private ProjectsController projectsController = new ProjectsController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(boardButton, projectsButton));
        boardButton.setOnAction(event -> boardButtonListener());
        projectsButton.setOnAction(event -> projectsButtonListener());
        boardButtonListener();
    }

    private void projectsButtonListener() {
        activateMode(projectsButton, projectsNode, OrganizerWindowCreator.getInstance()::getProjectsNode, projectsController);
    }

    private void boardButtonListener() {
        activateMode(boardButton, boardNode, OrganizerWindowCreator.getInstance()::getBoardNode, boardController);
    }
}
