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
    private Button reportButton;
    @FXML
    private Pane mainPane;

    private Node adminNode;
    private Node checkListNode;
    private Node reportNode;

    private AdminSubmodeController adminController = new AdminSubmodeController();
    private CheckListSubmodeController checkListController = new CheckListSubmodeController();
    private ReportSubmodeController reportController = new ReportSubmodeController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(checkListButton, adminButton, reportButton));
        checkListButton.setOnAction(event -> checkListButtonListener());
        adminButton.setOnAction(event -> adminButtonListener());
        reportButton.setOnAction(event -> reportButtonListener());
        checkListButtonListener();
    }

    private void checkListButtonListener() {
        activateMode(checkListButton, checkListNode, OrganizerWindowCreator.getInstance()::getCheckListSubmodeNode, checkListController);
    }

    private void adminButtonListener() {
        activateMode(adminButton, adminNode, OrganizerWindowCreator.getInstance()::getHabitAdminSubmodeNode, adminController);
    }

    private void reportButtonListener() {
        activateMode(reportButton, reportNode, OrganizerWindowCreator.getInstance()::getHabitsReportSubmodeNode, reportController);
    }
}
