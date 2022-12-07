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
    private Pane mainPane;

    private Node activitiesNode;
    private Node paramsNode;

    private final ActivitiesController activitiesController = new ActivitiesController();
    private final ParamsController paramsController = new ParamsController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(activitiesButton, paramsButton));
        activitiesButton.setOnAction(event -> activitiesButtonListener());
        paramsButton.setOnAction(event -> paramsButtonListener());
        activitiesButtonListener();
    }

    private void activitiesButtonListener() {
        activateMode(activitiesButton, activitiesNode, OrganizerWindowCreator.getInstance()::getStructureActivitiesNode, activitiesController);
    }

    private void paramsButtonListener() {
        activateMode(paramsButton, paramsNode, OrganizerWindowCreator.getInstance()::getStructureParamstNode, paramsController);
    }
}
