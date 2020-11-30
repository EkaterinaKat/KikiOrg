package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;


class AccountsController implements FxController {
    @FXML
    private TextField accountTitleField;
    @FXML
    private TextArea accountDescArea;
    @FXML
    private Button addAccountButton;
    @FXML
    private Button validationButton;
    @FXML
    private TableView<Account> table;
    @FXML
    private TableColumn<Account, String> titleColumn;
    @FXML
    private TableColumn<Account, String> descColumn;
    @FXML
    private TableColumn<Account, Long> amountColumn;

    @FXML
    private void initialize() {
        addAccountButton.setOnAction(event -> addAccount());
        Utils.associateButtonWithControls(addAccountButton, accountTitleField, accountDescArea);
        validationButton.setOnAction(event -> Core.getInstance().financeService().validateAllAccountsAmount());
        adjustColumns();
        fillTable();
    }

    private void addAccount() {
        Core.getInstance().financeService().addAccount(
                accountTitleField.getText(),
                accountDescArea.getText());
        accountTitleField.clear();
        accountDescArea.setText("-");
        fillTable();
    }

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descColumn.setCellFactory(tc -> {
            TableCell<Account, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    private void fillTable() {
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        accounts.addAll(Core.getInstance().financeService().getAccountsForCurrentUser());
        table.setItems(accounts);
    }
}
