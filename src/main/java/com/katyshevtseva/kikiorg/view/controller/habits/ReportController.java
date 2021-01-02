package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.ReportCell;
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
import java.util.Date;
import java.util.List;

import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

class ReportController implements FxController {
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
    private List<Habit> selectedHabits;
    private static final int CELL_PREF_HEIGHT = 30;
    private static final int CELL_PREF_WIDTH = 110;

    @FXML
    private void initialize() {
        selectedHabits = new ArrayList<>();
        showButton.setOnAction(event -> showReport());
        Utils.associateButtonWithControls(showButton, startDatePicker, endDatePicker);
        setInitialDates();
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
        List<Habit> allHabits = Core.getInstance().habitsService().getAllHabits();
        ObservableList<Habit> observableList = FXCollections.observableArrayList();
        observableList.addAll(allHabits);
        habitsTable.setItems(observableList);
        for (Habit habit : allHabits) {
            if (habit.isActive())
                selectedHabits.add(habit);
        }
    }

    private void setInitialDates() {
        startDatePicker.setValue(new java.sql.Date(DateUtils.getMonthAgoDate().getTime()).toLocalDate());
        endDatePicker.setValue(LocalDate.now());
    }

    private void showReport() {
        if (selectedHabits.size() == 0)
            return;

        reportTable.getChildren().clear();
        List<List<ReportCell>> report = Core.getInstance().habitsReportService().getReport(selectedHabits,
                Utils.getPeriodByDp(startDatePicker, endDatePicker));

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
        pane.setStyle(" -fx-background-color: " + reportCell.getColor() + "; ");
        Label label = new Label(reportCell.getText());
        pane.getChildren().add(label);
        HBox.setMargin(label, new Insets(8));
        StackPane.setAlignment(label, Pos.CENTER);
        reportTable.add(pane, column, row);
    }
}
