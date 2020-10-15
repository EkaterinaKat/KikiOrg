package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.view.controller.AbstractSwitchController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class HabitsModeController extends AbstractSwitchController implements FxController {
    @FXML
    private Button checkListButton;
    @FXML
    private Button adminButton;
    @FXML
    private Pane mainPane;
    private Node adminNode;
    private AdminSubmodeController adminController = new AdminSubmodeController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(checkListButton, adminButton));
        checkListButton.setOnAction(event -> checkListButtonListener());
        adminButton.setOnAction(event -> adminButtonListener());
        adminButtonListener();
    }

    private void checkListButtonListener() {

    }

    private void adminButtonListener() {
        activateMode(adminButton, adminNode, OrganizerWindowCreator.getInstance()::getHabitAdminSubmodeNode, adminController);
    }
}
