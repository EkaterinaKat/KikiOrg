package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.AbstractSwitchController;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
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
    private Button criterionButton;
    @FXML
    private Button historyButton;
    @FXML
    private Pane mainPane;

    private Node adminNode;
    private Node checkListNode;
    private Node reportNode;
    private Node analysisNode;
    private Node criterionNode;
    private Node historyNode;

    private final AdminController adminController = new AdminController();
    private final CheckListController checkListController = new CheckListController();
    private final ReportController reportController = new ReportController();
    private final AnalysisController analysisController = new AnalysisController();
    private final CriterionController criterionController = new CriterionController();
    private final HistoryController historyController = new HistoryController();

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(checkListButton, adminButton, reportButton, analysisButton, criterionButton, historyButton));
        checkListButton.setOnAction(event -> checkListButtonListener());
        adminButton.setOnAction(event -> adminButtonListener());
        reportButton.setOnAction(event -> reportButtonListener());
        analysisButton.setOnAction(event -> analysisButtonListener());
        criterionButton.setOnAction(event -> criterionButtonListener());
        historyButton.setOnAction(event -> historyButtonListener());
        checkListButtonListener();
    }

    private void historyButtonListener() {
        activateMode(historyButton, historyNode, OrganizerWindowCreator.getInstance()::getHabitsHistoryNode, historyController);
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

    private void criterionButtonListener() {
        activateMode(criterionButton, criterionNode, OrganizerWindowCreator.getInstance()::getHabitsCriterionNode, criterionController);
    }
}
