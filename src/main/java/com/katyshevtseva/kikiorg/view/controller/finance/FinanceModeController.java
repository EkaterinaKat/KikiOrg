package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.springframework.stereotype.Controller;

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
    private Pane mainPane;
    private ReplenishmentSubmodeController replenishmentController = new ReplenishmentSubmodeController();
    private AccountsSubmodeController accountsController = new AccountsSubmodeController();
    private ExpensesSubmodeController expensesController = new ExpensesSubmodeController();
    private Node replenishmentSubmodeNode;
    private Node accountsSubmodeNode;
    private Node expensesSubmodeNode;

    public void updateDisplayedData() {

    }

    @FXML
    private void initialize() {
        buttons.addAll(Arrays.asList(replenishmentButton, accountsButton, expensesButton));
        replenishmentButtonListener();
        replenishmentButton.setOnAction(event -> replenishmentButtonListener());
        accountsButton.setOnAction(event -> accountsButtonListener());
        expensesButton.setOnAction(event -> expensesButtonListener());
    }

    private void replenishmentButtonListener() {
        activateButton(replenishmentButton);
        if (replenishmentSubmodeNode == null)
            replenishmentSubmodeNode = OrganizerWindowCreator.getInstance().getReplenishmentSubmodeNode(replenishmentController);
        replenishmentController.updateDisplayedData();
        mainPane.getChildren().clear();
        mainPane.getChildren().add(replenishmentSubmodeNode);
    }

    private void accountsButtonListener() {
        activateButton(accountsButton);
        if (accountsSubmodeNode == null)
            accountsSubmodeNode = OrganizerWindowCreator.getInstance().getAccountsSubmodeNode(accountsController);
        accountsController.updateDisplayedData();
        mainPane.getChildren().clear();
        mainPane.getChildren().add(accountsSubmodeNode);
    }

    private void expensesButtonListener() {
        activateButton(expensesButton);
        if (expensesSubmodeNode == null)
            expensesSubmodeNode = OrganizerWindowCreator.getInstance().getExpensesSubmodeNode(expensesController);
        expensesController.updateDisplayedData();
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
