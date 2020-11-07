package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.modes.habits.HabitsService;
import com.katyshevtseva.kikiorg.core.modes.habits.HabitsReportService;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.modes.habits.entity.ReportCell;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.sql.Date.valueOf;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

class ReportSubmodeController implements FxController {
    @FXML
    private TableView<Habit> habitsTable;
    @FXML
    private TableColumn<Habit, String> titleColumn;
    @FXML
    private TableColumn<Habit, Boolean> checkBoxColumn;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private GridPane reportTable;
    @FXML
    private Button showButton;
    private List<Habit> selectedHabits = new ArrayList<>();
    private static final int CELL_PREF_HEIGHT = 30;
    private static final int CELL_PREF_WIDTH = 80;
    private static final int CELL_MAX_WIDTH = 100;

    @FXML
    private void initialize() {
        showButton.setOnAction(event -> showReport());
        Utils.associateButtonWithControls(showButton, startDatePicker, endDatePicker);
        setInitialDatesd();
        adjustTableColumns();
        fillHabitsTable();
        habitsTable.setEditable(true);
    }

    private void adjustTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        checkBoxColumn.setCellValueFactory(param -> {
            Habit habit = param.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(habit.isActive());
            booleanProperty.addListener((observable, oldValue, newValue) -> {
                if (newValue)
                    selectedHabits.add(habit);
                else
                    selectedHabits.remove(habit);
            });
            return booleanProperty;
        });
        checkBoxColumn.setCellFactory(param -> {
            CheckBoxTableCell<Habit, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void fillHabitsTable() {
        List<Habit> allHabits = HabitsService.getInstance().getAllHabits();
        ObservableList<Habit> observableList = FXCollections.observableArrayList();
        observableList.addAll(allHabits);
        habitsTable.setItems(observableList);
        for (Habit habit : allHabits) {
            if (habit.isActive())
                selectedHabits.add(habit);
        }
    }

    private void setInitialDatesd() {
        Calendar calendar = Calendar.getInstance();
        startDatePicker.setValue(LocalDate.of(
                calendar.get(YEAR),
                calendar.get(MONTH), // Calendar считает месяцы с 0, а LocalDate с 1, поэтому происходит смещение на 1 месяц
                calendar.get(Calendar.DATE)));
        endDatePicker.setValue(LocalDate.now());
    }

    private void showReport() {
        if (selectedHabits.size() == 0)
            return;
        List<List<ReportCell>> report = HabitsReportService.getInstance().getReport(selectedHabits,
                valueOf(startDatePicker.getValue()), valueOf(endDatePicker.getValue()));

        for (int i = 0; i < report.size(); i++) {
            List<ReportCell> reportLine = report.get(i);
            for (int j = 0; j < reportLine.size(); j++) {
                ReportCell reportCell = reportLine.get(j);
                fillCell(reportCell, i, j);
            }
        }
    }

    private void fillCell(ReportCell reportCell, int row, int column) {
        StackPane pane = new StackPane();
        pane.setPrefHeight(CELL_PREF_HEIGHT);
        pane.setPrefWidth(CELL_PREF_WIDTH);
        pane.setMaxWidth(CELL_MAX_WIDTH);
        pane.setStyle(" -fx-background-color: " + reportCell.getColor() + "; ");
        Label label = new Label(reportCell.getText());
        pane.getChildren().add(label);
        HBox.setMargin(label, new Insets(8));
        StackPane.setAlignment(label, Pos.CENTER);
        reportTable.add(pane, column, row);
    }
}
