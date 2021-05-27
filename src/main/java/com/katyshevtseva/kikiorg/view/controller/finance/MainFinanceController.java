package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.controller.AbstractSwitchController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainFinanceController extends AbstractSwitchController implements FxController {
    @FXML
    private Button replenishmentButton;
    @FXML
    private Button expensesButton;
    @FXML
    private Button accountsButton;
    @FXML
    private Button checkButton;
    @FXML
    private Pane mainPane;
    @FXML
    private Button historyButton;
    @FXML
    private Button transferButton;
    @FXML
    private Button itemHierarchyButton;
    @FXML
    private Button reportButton;
    @FXML
    private Button analysisButton;

    private final ReplenishmentController replenishmentController = new ReplenishmentController();
    private final AccountsController accountsController = new AccountsController();
    private final ExpensesController expensesController = new ExpensesController();
    private final CheckController checkController = new CheckController();
    private final HistoryController historyController = new HistoryController();
    private final TransferController transferController = new TransferController();
    private final ItemHierarchyController itemHierarchyController = new ItemHierarchyController();
    private final ReportController reportController = new ReportController();
    private final AnalysisController analysisController = new AnalysisController();

    private Node replenishmentNode;
    private Node accountsNode;
    private Node expensesNode;
    private Node checkNode;
    private Node historyNode;
    private Node transferNode;
    private Node itemHierarchyNode;
    private Node reportNode;
    private Node analysisNode;

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(replenishmentButton, accountsButton, expensesButton, checkButton,
                transferButton, historyButton, itemHierarchyButton, reportButton, analysisButton));
        replenishmentButtonListener();
        replenishmentButton.setOnAction(event -> replenishmentButtonListener());
        accountsButton.setOnAction(event -> accountsButtonListener());
        expensesButton.setOnAction(event -> expensesButtonListener());
        checkButton.setOnAction(event -> checkButtonListener());
        historyButton.setOnAction(event -> historyButtonListener());
        transferButton.setOnAction(event -> transferButtonListener());
        itemHierarchyButton.setOnAction(event -> itemHierarchyButtonListener());
        reportButton.setOnAction(event -> reportButtonListener());
        analysisButton.setOnAction(event -> analysisButtonListener());
    }

    private void analysisButtonListener() {
        activateMode(analysisButton, analysisNode,
                OrganizerWindowCreator.getInstance()::getFinanceAnalysisNode, analysisController);
    }

    private void itemHierarchyButtonListener() {
        activateMode(itemHierarchyButton, itemHierarchyNode,
                OrganizerWindowCreator.getInstance()::getItemHierarchyNode, itemHierarchyController);
    }

    private void reportButtonListener() {
        activateMode(reportButton, reportNode,
                OrganizerWindowCreator.getInstance()::getFinanceReportNode, reportController);
    }

    private void historyButtonListener() {
        activateMode(historyButton, historyNode,
                OrganizerWindowCreator.getInstance()::getHistoryNode, historyController);
    }

    private void transferButtonListener() {
        activateMode(transferButton, transferNode,
                OrganizerWindowCreator.getInstance()::getTransferNode, transferController);
    }

    private void replenishmentButtonListener() {
        activateMode(replenishmentButton, replenishmentNode,
                OrganizerWindowCreator.getInstance()::getReplenishmentNode, replenishmentController);
    }

    private void accountsButtonListener() {
        activateMode(accountsButton, accountsNode,
                OrganizerWindowCreator.getInstance()::getAccountsNode, accountsController);
    }

    private void expensesButtonListener() {
        activateMode(expensesButton, expensesNode,
                OrganizerWindowCreator.getInstance()::getExpensesNode, expensesController);
    }

    private void checkButtonListener() {
        activateMode(checkButton, checkNode,
                OrganizerWindowCreator.getInstance()::getCheckNode, checkController);
    }
}
