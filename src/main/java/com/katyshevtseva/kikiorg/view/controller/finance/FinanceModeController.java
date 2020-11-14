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

public class FinanceModeController extends AbstractSwitchController implements FxController {
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

    private ReplenishmentSubmodeController replenishmentController;
    private AccountsSubmodeController accountsController;
    private ExpensesSubmodeController expensesController;
    private CheckSubmodeController checkSubmodeController;
    private UserSubmodeController userSubmodeController;

    private Node replenishmentSubmodeNode;
    private Node accountsSubmodeNode;
    private Node expensesSubmodeNode;
    private Node checkSubmodeNode;
    private Node userSubmodeNode;

    private void updateDisplayedData() {
        reportTextArea.setText(Core.getInstance().financeReportService().getReport());
    }

    public FinanceModeController() {
        replenishmentController = new ReplenishmentSubmodeController();
        expensesController = new ExpensesSubmodeController();
        checkSubmodeController = new CheckSubmodeController();
        userSubmodeController = new UserSubmodeController();
        accountsController = new AccountsSubmodeController(replenishmentController, expensesController, checkSubmodeController);
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
        activateMode(replenishmentButton, replenishmentSubmodeNode,
                OrganizerWindowCreator.getInstance()::getReplenishmentSubmodeNode, replenishmentController);
    }

    private void accountsButtonListener() {
        activateMode(accountsButton, accountsSubmodeNode,
                OrganizerWindowCreator.getInstance()::getAccountsSubmodeNode, accountsController);
    }

    private void expensesButtonListener() {
        activateMode(expensesButton, expensesSubmodeNode,
                OrganizerWindowCreator.getInstance()::getExpensesSubmodeNode, expensesController);
    }

    private void checkButtonListener() {
        activateMode(checkButton, checkSubmodeNode,
                OrganizerWindowCreator.getInstance()::getCheckSubmodeNode, checkSubmodeController);
    }

    private void userButtonListener() {
        activateMode(userButton, userSubmodeNode,
                OrganizerWindowCreator.getInstance()::getUserSubmodeNode, userSubmodeController);
    }
}
