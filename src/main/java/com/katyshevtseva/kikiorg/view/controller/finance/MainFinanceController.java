package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.AbstractSwitchController;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainFinanceController extends AbstractSwitchController implements FxController {
    @FXML
    private Pane mainPane;
    @FXML
    private Button historyButton;
    @FXML
    private Button itemHierarchyButton;
    @FXML
    private Button reportButton;
    @FXML
    private Button adminButton;
    @FXML
    private Button ledgerButton;

    private final HistoryController historyController = new HistoryController();
    private final ItemHierarchyController itemHierarchyController = new ItemHierarchyController();
    private final ReportController reportController = new ReportController();
    private final AdminController adminController = new AdminController();
    private final LedgerController ledgerController = new LedgerController();

    private Node historyNode;
    private Node itemHierarchyNode;
    private Node adminNode;
    private Node ledgerNode;

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(ledgerButton, historyButton, itemHierarchyButton, adminButton));
        ledgerButtonListener();
        historyButton.setOnAction(event -> historyButtonListener());
        itemHierarchyButton.setOnAction(event -> itemHierarchyButtonListener());
        reportButton.setOnAction(event -> reportButtonListener());
        adminButton.setOnAction(event -> adminButtonListener());
        ledgerButton.setOnAction(event -> ledgerButtonListener());
    }

    private void ledgerButtonListener() {
        activateMode(ledgerButton, ledgerNode,
                OrganizerWindowCreator.getInstance()::getLedgerNode, ledgerController);
    }

    private void adminButtonListener() {
        activateMode(adminButton, adminNode,
                OrganizerWindowCreator.getInstance()::getFinanceAdminNode, adminController);
    }

    private void itemHierarchyButtonListener() {
        activateMode(itemHierarchyButton, itemHierarchyNode,
                OrganizerWindowCreator.getInstance()::getItemHierarchyNode, itemHierarchyController);
    }

    private void reportButtonListener() {
        OrganizerWindowCreator.getInstance().openFinanceReportDialog(reportController);
    }

    private void historyButtonListener() {
        activateMode(historyButton, historyNode,
                OrganizerWindowCreator.getInstance()::getHistoryNode, historyController);
    }
}
