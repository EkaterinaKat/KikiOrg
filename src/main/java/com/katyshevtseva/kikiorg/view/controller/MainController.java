package com.katyshevtseva.kikiorg.view.controller;

import com.katyshevtseva.kikiorg.view.controller.finance.FinanceModeController;
import com.katyshevtseva.kikiorg.view.controller.habits.HabitsModeController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainController extends AbstractSwitchController implements WindowBuilder.FxController {
    @FXML
    private Button todayButton;
    @FXML
    private Button habitsButton;
    @FXML
    private Button regularTasksButton;
    @FXML
    private Button irregularTasksButton;
    @FXML
    private Button eventsButton;
    @FXML
    private Button studiesButton;
    @FXML
    private Button financeButton;
    @FXML
    private Pane mainPane;
    private FinanceModeController financeModeController = new FinanceModeController();
    private HabitsModeController habitsModeController = new HabitsModeController();
    private Node financeModeNode;
    private Node habitsModeNode;

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(todayButton, habitsButton, regularTasksButton, irregularTasksButton,
                eventsButton, studiesButton, financeButton));
        financeButton.setOnAction(event -> financeButtonListener());
        habitsButton.setOnAction(event -> habitsButtonListener());
        financeButtonListener();
    }

    private void financeButtonListener() {
        activateMode(financeButton, financeModeNode,
                OrganizerWindowCreator.getInstance()::getFinanceModeNode, financeModeController);
        disableEmptyButtons();
    }

    private void habitsButtonListener() {
        activateMode(habitsButton, habitsModeNode,
                OrganizerWindowCreator.getInstance()::getHabitsModeNode, habitsModeController);
        disableEmptyButtons();
    }

    private void disableEmptyButtons() {
        todayButton.setDisable(true);
        regularTasksButton.setDisable(true);
        irregularTasksButton.setDisable(true);
        eventsButton.setDisable(true);
        studiesButton.setDisable(true);
    }
}
