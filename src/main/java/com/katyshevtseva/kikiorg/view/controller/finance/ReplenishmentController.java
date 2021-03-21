package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.date.DateCorrector;
import com.katyshevtseva.fx.Utils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Source;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

class ReplenishmentController implements FxController {
    @FXML
    private TextField sourceTitleField;
    @FXML
    private TextArea sourceDescArea;
    @FXML
    private Button addSourceButton;
    @FXML
    private TextField amountTextField;
    @FXML
    private ComboBox<Source> sourceComboBox;
    @FXML
    private ComboBox<Account> accountComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button doneButton;
    @FXML
    private TableView<Source> table;
    @FXML
    private TableColumn<Source, String> titleColumn;
    @FXML
    private TableColumn<Source, String> descColumn;
    @FXML
    private TableColumn<Source, String> ownerColumn;
    @FXML
    private TableColumn<Source, Void> editColumn;

    @FXML
    private void initialize() {
        Utils.disableNonNumericChars(amountTextField);
        doneButton.setOnAction(event -> saveReplenishment());
        addSourceButton.setOnAction(event -> {
            addSource();
            fillTable();
        });
        Utils.associateButtonWithControls(doneButton, amountTextField, sourceComboBox, accountComboBox, datePicker);
        Utils.associateButtonWithControls(addSourceButton, sourceTitleField, sourceDescArea);
        setSourceComboBoxItems();
        setAccountComboBoxItems();
        datePicker.setValue(new java.sql.Date(DateCorrector.getProperDate().getTime()).toLocalDate());
        adjustColumns();
        fillTable();
    }

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        descColumn.setCellFactory(tc -> {
            TableCell<Source, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        adjustButtonColumn();
    }

    private void fillTable() {
        ObservableList<Source> sources = FXCollections.observableArrayList();
        sources.addAll(Core.getInstance().financeService().getSourcesForCurrentUser());
        table.setItems(sources);
    }

    private void setSourceComboBoxItems() {
        ObservableList<Source> sources = FXCollections.observableArrayList(Core.getInstance().financeService().getSourcesForCurrentUser());
        sourceComboBox.setItems(sources);
    }

    private void setAccountComboBoxItems() {
        if (accountComboBox != null) {
            ObservableList<Account> accounts = FXCollections.observableArrayList(Core.getInstance().financeService().getAccountsForCurrentUser());
            accountComboBox.setItems(accounts);
            if (accounts.size() > 0)
                accountComboBox.setValue(accounts.get(0));
        }
    }

    private void addSource() {
        Core.getInstance().financeService().addSource(sourceTitleField.getText(), sourceDescArea.getText());
        sourceTitleField.clear();
        sourceDescArea.setText("-");
        setSourceComboBoxItems();
    }

    private void saveReplenishment() {
        Core.getInstance().financeService().addReplenishment(accountComboBox.getValue(), Long.parseLong(amountTextField.getText()),
                sourceComboBox.getValue(), java.sql.Date.valueOf(datePicker.getValue()));
        amountTextField.clear();
    }

    private void adjustButtonColumn() {
        editColumn.setCellFactory(new Callback<TableColumn<Source, Void>, TableCell<Source, Void>>() {
            @Override
            public TableCell<Source, Void> call(final TableColumn<Source, Void> param) {
                return new TableCell<Source, Void>() {
                    private final Button button = new Button("Edit");

                    {
                        button.setOnAction(event -> {
                            Source source = getTableView().getItems().get(getIndex());
                            OrgUtils.getDialogBuilder().openTextFieldAndTextAreaDialog(source.getTitle(), source.getDescription(),
                                    (title, desc) -> {
                                        Core.getInstance().financeService().alterSource(source, title, desc);
                                        fillTable();
                                    });
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        });
    }
}
