package com.katyshevtseva.kikiorg.view.controller;

import com.katyshevtseva.kikiorg.view.controller.finance.MainFinanceController;
import com.katyshevtseva.kikiorg.view.controller.habits.MainHabitsController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainController extends AbstractSwitchController implements WindowBuilder.FxController {
    @FXML
    private Button habitsButton;
    @FXML
    private Button financeButton;
    @FXML
    private Pane mainPane;
    private final MainFinanceController mainFinanceController = new MainFinanceController();
    private final MainHabitsController mainHabitsController = new MainHabitsController();
    private Node financeModeNode;
    private Node habitsModeNode;

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(habitsButton, financeButton));
        financeButton.setOnAction(event -> financeButtonListener());
        habitsButton.setOnAction(event -> habitsButtonListener());
        financeButtonListener();
    }

    private void financeButtonListener() {
        activateMode(financeButton, financeModeNode,
                OrganizerWindowCreator.getInstance()::getMainFinanceNode, mainFinanceController);
    }

    private void habitsButtonListener() {
        activateMode(habitsButton, habitsModeNode,
                OrganizerWindowCreator.getInstance()::getMainHabitsNode, mainHabitsController);
    }
}
