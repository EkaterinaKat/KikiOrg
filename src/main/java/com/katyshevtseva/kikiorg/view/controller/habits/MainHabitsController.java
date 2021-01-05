package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.view.controller.AbstractSwitchController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainHabitsController extends AbstractSwitchController implements FxController {
    @FXML
    private Button checkListButton;
    @FXML
    private Button adminButton;
    @FXML
    private Button reportButton;
    @FXML
    private Button analysisButton;
    @FXML
    private Pane mainPane;

    private Node adminNode;
    private Node checkListNode;
    private Node reportNode;
    private Node analysisNode;

    private AdminController adminController = new AdminController();
    private CheckListController checkListController = new CheckListController();
    private ReportController reportController = new ReportController();
    private AnalysisController analysisController = new AnalysisController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(checkListButton, adminButton, reportButton, analysisButton));
        checkListButton.setOnAction(event -> checkListButtonListener());
        adminButton.setOnAction(event -> adminButtonListener());
        reportButton.setOnAction(event -> reportButtonListener());
        analysisButton.setOnAction(event -> analysisButtonListener());
        checkListButtonListener();
    }

    private void checkListButtonListener() {
        activateMode(checkListButton, checkListNode, OrganizerWindowCreator.getInstance()::getCheckListNode, checkListController);
    }

    private void adminButtonListener() {
        activateMode(adminButton, adminNode, OrganizerWindowCreator.getInstance()::getHabitAdminNode, adminController);
    }

    private void reportButtonListener() {
        activateMode(reportButton, reportNode, OrganizerWindowCreator.getInstance()::getHabitsReportNode, reportController);
    }

    private void analysisButtonListener() {
        activateMode(analysisButton, analysisNode, OrganizerWindowCreator.getInstance()::getHabitsAnalysisNode, analysisController);
    }
}
