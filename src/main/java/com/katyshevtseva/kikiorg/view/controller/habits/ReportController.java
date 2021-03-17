package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.Utils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.date.DateUtils;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.ReportCell;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                OrgUtils.getPeriodByDp(startDatePicker, endDatePicker));

        for (int i = 0; i < report.size(); i++) {
            List<ReportCell> reportLine = report.get(i);
            for (int j = 0; j < reportLine.size(); j++) {
                ReportCell reportCell = reportLine.get(j);
                fillCell(reportCell, i, j);
            }
        }
    }

    private void fillCell(ReportCell reportCell, int row, int column) {
        StackPane cell = new StackPane();
        cell.setStyle(" -fx-background-color: " + reportCell.getColor() + "; ");
        Label label = new Label(reportCell.getText());
        label.setTooltip(new Tooltip(reportCell.getText()));


        if (reportCell.isHabit()) {
            VBox vBox = new VBox(label);
            vBox.setRotate(90);
            vBox.setPadding(new Insets(5, 5, 5, 5));
            cell.setPrefHeight(170);
            cell.setPrefWidth(50);
            cell.getChildren().add(new Group(vBox));
        } else if (reportCell.isDate()) {
            cell.setPrefWidth(100);
            cell.setPrefHeight(30);
            cell.getChildren().add(label);
        } else {
            cell.setPrefHeight(30);
            cell.setPrefWidth(50);
            cell.getChildren().add(label);
        }

        HBox.setMargin(label, new Insets(8));
        StackPane.setAlignment(label, Pos.CENTER);
        reportTable.add(cell, column, row);


    }
}
