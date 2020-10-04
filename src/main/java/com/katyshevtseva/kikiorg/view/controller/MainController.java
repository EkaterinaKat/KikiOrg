package com.katyshevtseva.kikiorg.view.controller;

import com.katyshevtseva.kikiorg.view.controller.finance.FinanceModeController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainController implements WindowBuilder.FxController {
    private List<Button> buttons = new ArrayList<>();
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
    private FinanceModeController financeModeController;
    private Node financeModeNode;

    @FXML
    private void initialize() {
        buttons.addAll(Arrays.asList(todayButton, habitsButton, regularTasksButton, irregularTasksButton,
                eventsButton, studiesButton, financeButton));
        financeButton.setOnAction(event -> financeButtonListener());
        financeButtonListener();
        todayButton.setDisable(true);
        habitsButton.setDisable(true);
        regularTasksButton.setDisable(true);
        irregularTasksButton.setDisable(true);
        eventsButton.setDisable(true);
        studiesButton.setDisable(true);
        financeButton.setDisable(true);
    }

    private void financeButtonListener() {
        activateButton(financeButton);
        if (financeModeNode == null)
            financeModeNode = OrganizerWindowCreator.getInstance().getFinanceModeNode(financeModeController);
        financeModeController.updateDisplayedData();
        mainPane.getChildren().clear();
        mainPane.getChildren().add(financeModeNode);
    }

    private void activateButton(Button buttonToActivate) {
        for (Button button : buttons) {
            button.setDisable(false);
        }
        buttonToActivate.setDisable(true);
    }
}
