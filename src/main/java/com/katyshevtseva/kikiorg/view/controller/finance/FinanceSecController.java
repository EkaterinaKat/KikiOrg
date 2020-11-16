package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.controller.AbstractSwitchController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class FinanceSecController extends AbstractSwitchController implements FxController {
    @FXML
    private Button replenishmentButton;
    @FXML
    private Button expensesButton;
    @FXML
    private Button accountsButton;
    @FXML
    private Button checkButton;
    @FXML
    private Button userButton;
    @FXML
    private Pane mainPane;
    @FXML
    private Button reportButton;
    @FXML
    private Button transferButton;

    private ReplenishmentSubsecController replenishmentController = new ReplenishmentSubsecController();
    private AccountsSubsecController accountsController = new AccountsSubsecController();
    private ExpensesSubsecController expensesController = new ExpensesSubsecController();
    private CheckSubsecController checkController = new CheckSubsecController();
    private UserSubsecController userController = new UserSubsecController();
    private HistorySubsecController reportController = new HistorySubsecController();
    private TransferSubsecController transferController = new TransferSubsecController();

    private Node replenishmentNode;
    private Node accountsNode;
    private Node expensesNode;
    private Node checkNode;
    private Node userNode;
    private Node reportNode;
    private Node transferNode;

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(replenishmentButton, accountsButton, expensesButton, checkButton, userButton,
                transferButton, reportButton));
        replenishmentButtonListener();
        replenishmentButton.setOnAction(event -> replenishmentButtonListener());
        accountsButton.setOnAction(event -> accountsButtonListener());
        expensesButton.setOnAction(event -> expensesButtonListener());
        checkButton.setOnAction(event -> checkButtonListener());
        userButton.setOnAction(event -> userButtonListener());
        reportButton.setOnAction(event -> reportButtonListener());
        transferButton.setOnAction(event -> transferButtonListener());
    }

    private void reportButtonListener() {
        activateMode(reportButton, reportNode,
                OrganizerWindowCreator.getInstance()::getFinanceReportSubsecNode, reportController);
    }

    private void transferButtonListener() {
        activateMode(transferButton, transferNode,
                OrganizerWindowCreator.getInstance()::getTransferSubsecNode, transferController);
    }

    private void replenishmentButtonListener() {
        activateMode(replenishmentButton, replenishmentNode,
                OrganizerWindowCreator.getInstance()::getReplenishmentSubsecNode, replenishmentController);
    }

    private void accountsButtonListener() {
        activateMode(accountsButton, accountsNode,
                OrganizerWindowCreator.getInstance()::getAccountsSubsecNode, accountsController);
    }

    private void expensesButtonListener() {
        activateMode(expensesButton, expensesNode,
                OrganizerWindowCreator.getInstance()::getExpensesSubsecNode, expensesController);
    }

    private void checkButtonListener() {
        activateMode(checkButton, checkNode,
                OrganizerWindowCreator.getInstance()::getCheckSubsecNode, checkController);
    }

    private void userButtonListener() {
        activateMode(userButton, userNode,
                OrganizerWindowCreator.getInstance()::getUserSubsecNode, userController);
    }
}
