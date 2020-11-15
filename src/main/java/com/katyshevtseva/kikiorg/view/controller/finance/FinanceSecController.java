package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.view.controller.AbstractSwitchController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
    private Button updateButton;
    @FXML
    private Button checkButton;
    @FXML
    private Button userButton;
    @FXML
    private TextArea reportTextArea;
    @FXML
    private Pane mainPane;

    private ReplenishmentSubsecController replenishmentController;
    private AccountsSubsecController accountsController;
    private ExpensesSubsecController expensesController;
    private CheckSubsecController checkSubsecController;
    private UserSubsecController userSubsecController;

    private Node replenishmentSubsecNode;
    private Node accountsSubsecNode;
    private Node expensesSubsecNode;
    private Node checkSubsecNode;
    private Node userSubsecNode;

    private void updateDisplayedData() {
        reportTextArea.setText(Core.getInstance().financeReportService().getReport());
    }

    public FinanceSecController() {
        replenishmentController = new ReplenishmentSubsecController();
        expensesController = new ExpensesSubsecController();
        checkSubsecController = new CheckSubsecController();
        userSubsecController = new UserSubsecController();
        accountsController = new AccountsSubsecController(replenishmentController, expensesController, checkSubsecController);
    }

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(replenishmentButton, accountsButton, expensesButton, checkButton, userButton));
        replenishmentButtonListener();
        replenishmentButton.setOnAction(event -> replenishmentButtonListener());
        accountsButton.setOnAction(event -> accountsButtonListener());
        expensesButton.setOnAction(event -> expensesButtonListener());
        checkButton.setOnAction(event -> checkButtonListener());
        userButton.setOnAction(event -> userButtonListener());
        updateButton.setOnAction(event -> updateDisplayedData());
    }

    private void replenishmentButtonListener() {
        activateMode(replenishmentButton, replenishmentSubsecNode,
                OrganizerWindowCreator.getInstance()::getReplenishmentSubsecNode, replenishmentController);
    }

    private void accountsButtonListener() {
        activateMode(accountsButton, accountsSubsecNode,
                OrganizerWindowCreator.getInstance()::getAccountsSubsecNode, accountsController);
    }

    private void expensesButtonListener() {
        activateMode(expensesButton, expensesSubsecNode,
                OrganizerWindowCreator.getInstance()::getExpensesSubsecNode, expensesController);
    }

    private void checkButtonListener() {
        activateMode(checkButton, checkSubsecNode,
                OrganizerWindowCreator.getInstance()::getCheckSubsecNode, checkSubsecController);
    }

    private void userButtonListener() {
        activateMode(userButton, userSubsecNode,
                OrganizerWindowCreator.getInstance()::getUserSubsecNode, userSubsecController);
    }
}
