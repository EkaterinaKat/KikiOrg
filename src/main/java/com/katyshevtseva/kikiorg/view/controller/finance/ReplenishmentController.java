package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.date.DateCorrector;
import com.katyshevtseva.fx.FxUtils;
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

import static com.katyshevtseva.fx.FxUtils.*;

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
    private TableColumn<Source, Void> editColumn;

    @FXML
    private void initialize() {
        disableNonNumericChars(amountTextField);
        doneButton.setOnAction(event -> saveReplenishment());
        addSourceButton.setOnAction(event -> {
            addSource();
            fillTable();
        });
        associateButtonWithControls(doneButton, amountTextField, sourceComboBox, accountComboBox, datePicker);
        associateButtonWithControls(addSourceButton, sourceTitleField, sourceDescArea);
        setSourceComboBoxItems();
        FxUtils.setComboBoxItemsAndSetSelectedFirstItem(accountComboBox, Core.getInstance().financeService().getAllAccounts());
        datePicker.setValue(new java.sql.Date(DateCorrector.getProperDate().getTime()).toLocalDate());
        adjustColumns();
        fillTable();
    }

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descColumn.setCellFactory(tc -> {
            TableCell<Source, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        FxUtils.adjustButtonColumn(editColumn, "Edit",
                source ->
                        OrgUtils.getDialogBuilder().openTextFieldAndTextAreaDialog(source.getTitle(), source.getDescription(),
                                (title, desc) -> {
                                    Core.getInstance().financeService().alterSource(source, title, desc);
                                    fillTable();
                                }));
    }

    private void fillTable() {
        ObservableList<Source> sources = FXCollections.observableArrayList();
        sources.addAll(Core.getInstance().financeService().getAllSources());
        table.setItems(sources);
    }

    private void setSourceComboBoxItems() {
        setComboBoxItems(sourceComboBox, Core.getInstance().financeService().getAllSources());
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
}
