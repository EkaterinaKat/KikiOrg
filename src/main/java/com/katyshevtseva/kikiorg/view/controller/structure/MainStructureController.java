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
    private Button activitiesButton;
    @FXML
    private Button paramsButton;
    @FXML
    private Button goalsButton;
    @FXML
    private Button topicalButton;
    @FXML
    private Pane mainPane;

    private Node activitiesNode;
    private Node paramsNode;
    private Node goalsNode;
    private Node topicalNode;

    private final ActivitiesController activitiesController = new ActivitiesController();
    private final ParamsController paramsController = new ParamsController();
    private final GoalsController goalsController = new GoalsController();
    private final TopicalController topicalController = new TopicalController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(activitiesButton, paramsButton, goalsButton, topicalButton));
        activitiesButton.setOnAction(event -> activitiesButtonListener());
        paramsButton.setOnAction(event -> paramsButtonListener());
        goalsButton.setOnAction(event -> goalsButtonListener());
        topicalButton.setOnAction(event -> topicalButtonListener());
        topicalButtonListener();
    }

    private void topicalButtonListener() {
        activateMode(topicalButton, topicalNode, OrganizerWindowCreator.getInstance()::getTopicalNode, topicalController);
    }

    private void goalsButtonListener() {
        activateMode(goalsButton, goalsNode, OrganizerWindowCreator.getInstance()::getGoalsNode, goalsController);
    }

    private void activitiesButtonListener() {
        activateMode(activitiesButton, activitiesNode, OrganizerWindowCreator.getInstance()::getStructureActivitiesNode, activitiesController);
    }

    private void paramsButtonListener() {
        activateMode(paramsButton, paramsNode, OrganizerWindowCreator.getInstance()::getStructureParamsNode, paramsController);
    }
}
