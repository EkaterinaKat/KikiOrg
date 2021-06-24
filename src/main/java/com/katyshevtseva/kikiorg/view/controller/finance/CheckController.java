package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class CheckController implements FxController {
    @FXML
    private Pane scatterPane;
    @FXML
    private Pane huddlePane;
    @FXML
    private TableView<Account> table;
    @FXML
    private TableColumn<Account, String> titleColumn;
    @FXML
    private TableColumn<Account, Long> amountColumn;
    @FXML
    private TableColumn<Account, Boolean> checkBoxColumn;
    @FXML
    private Button updateButton;

    @FXML
    private void initialize() {
        scatterPane.getChildren().add(OrganizerWindowCreator.getInstance().getScatterCheckNode(new ScatterCheckController()));
        huddlePane.getChildren().add(OrganizerWindowCreator.getInstance().getHuddleCheckNode(new HuddleCheckController()));
        updateButton.setOnAction(event -> updateTable());
        adjustTable();
        updateTable();
    }

    private void adjustTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        table.setEditable(true);
        checkBoxColumn.setCellValueFactory(param -> new SimpleBooleanProperty(false));
        checkBoxColumn.setCellFactory(param -> {
            CheckBoxTableCell<Account, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    void updateTable() {
        List<Account> accounts = Core.getInstance().financeService().getAllAccounts().stream()
                .sorted(Comparator.comparing(Account::getTitle)).collect(Collectors.toList());
        ObservableList<Account> observableList = FXCollections.observableArrayList();
        observableList.addAll(accounts);
        table.setItems(observableList);
    }
}
