package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;


class AccountsController implements FxController {
    @FXML
    private TextField accountTitleField;
    @FXML
    private TextArea accountDescArea;
    @FXML
    private Button addAccountButton;
    @FXML
    private TableView<Account> table;
    @FXML
    private TableColumn<Account, String> titleColumn;
    @FXML
    private TableColumn<Account, String> descColumn;
    @FXML
    private TableColumn<Account, Long> amountColumn;
    @FXML
    private TableColumn<Account, Void> editColumn;

    @FXML
    private void initialize() {
        addAccountButton.setOnAction(event -> addAccount());
        associateButtonWithControls(addAccountButton, accountTitleField, accountDescArea);
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
        FxUtils.adjustButtonColumn(editColumn, "Edit",
                account ->
                        OrgUtils.getDialogBuilder().openTextFieldAndTextAreaDialog(account.getTitle(), account.getDescription(),
                                (title, desc) -> {
                                    Core.getInstance().financeService().alterAccount(account, title, desc);
                                    fillTable();
                                }));
    }

    private void fillTable() {
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        accounts.addAll(Core.getInstance().financeService().getAllAccounts());
        table.setItems(accounts);
    }
}
