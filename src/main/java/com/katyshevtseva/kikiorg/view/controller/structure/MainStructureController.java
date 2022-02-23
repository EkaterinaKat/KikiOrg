package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.AbstractSwitchController;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainStructureController extends AbstractSwitchController implements FxController {
    @FXML
    private Button boardButton;
    @FXML
    private Button coursesButton;
    @FXML
    private Pane mainPane;

    private Node boardNode;
    private Node coursesNode;

    private final BoardController boardController = new BoardController();
    private final CoursesController coursesController = new CoursesController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(boardButton, coursesButton));
        boardButton.setOnAction(event -> boardButtonListener());
        coursesButton.setOnAction(event -> coursesButtonListener());
        boardButtonListener();
    }

    private void coursesButtonListener() {
        activateMode(coursesButton, coursesNode, OrganizerWindowCreator.getInstance()::getCoursesNode, coursesController);
    }

    private void boardButtonListener() {
        activateMode(boardButton, boardNode, OrganizerWindowCreator.getInstance()::getStructureBoardNode, boardController);
    }
}
