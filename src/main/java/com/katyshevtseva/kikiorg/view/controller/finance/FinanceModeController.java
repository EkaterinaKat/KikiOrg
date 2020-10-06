package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.finance.FinanceManager;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FinanceModeController implements FxController {
    private List<Button> buttons = new ArrayList<>();
    @FXML
    private Button replenishmentButton;
    @FXML
    private Button expensesButton;
    @FXML
    private Button accountsButton;
    @FXML
    private Button updateButton;
    @FXML
    private TextArea reportTextArea;
    @FXML
    private Pane mainPane;
    private ReplenishmentSubmodeController replenishmentController;
    private AccountsSubmodeController accountsController;
    private ExpensesSubmodeController expensesController;
    private Node replenishmentSubmodeNode;
    private Node accountsSubmodeNode;
    private Node expensesSubmodeNode;

    private void updateDisplayedData() {
        reportTextArea.setText(FinanceManager.getInstance().getReport());
    }

    public FinanceModeController() {
        replenishmentController = new ReplenishmentSubmodeController();
        expensesController = new ExpensesSubmodeController();
        accountsController = new AccountsSubmodeController(replenishmentController, expensesController);
    }

    @FXML
    private void initialize() {
        buttons.addAll(Arrays.asList(replenishmentButton, accountsButton, expensesButton));
        replenishmentButtonListener();
        replenishmentButton.setOnAction(event -> replenishmentButtonListener());
        accountsButton.setOnAction(event -> accountsButtonListener());
        expensesButton.setOnAction(event -> expensesButtonListener());
        updateButton.setOnAction(event -> updateDisplayedData());
    }

    private void replenishmentButtonListener() {
        activateButton(replenishmentButton);
        if (replenishmentSubmodeNode == null)
            replenishmentSubmodeNode = OrganizerWindowCreator.getInstance().getReplenishmentSubmodeNode(replenishmentController);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(replenishmentSubmodeNode);
    }

    private void accountsButtonListener() {
        activateButton(accountsButton);
        if (accountsSubmodeNode == null)
            accountsSubmodeNode = OrganizerWindowCreator.getInstance().getAccountsSubmodeNode(accountsController);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(accountsSubmodeNode);
    }

    private void expensesButtonListener() {
        activateButton(expensesButton);
        if (expensesSubmodeNode == null)
            expensesSubmodeNode = OrganizerWindowCreator.getInstance().getExpensesSubmodeNode(expensesController);
        mainPane.getChildren().clear();
        mainPane.getChildren().add(expensesSubmodeNode);
    }

    private void activateButton(Button buttonToActivate) {
        for (Button button : buttons) {
            button.setDisable(false);
        }
        buttonToActivate.setDisable(true);
    }
}
