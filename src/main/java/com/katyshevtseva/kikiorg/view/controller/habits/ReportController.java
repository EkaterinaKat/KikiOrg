package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.date.DateUtils.TimeUnit;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.shiftDate;
import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.HABIT_REPORT_TABLE;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

class ReportController implements SectionController {
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
    private Button showButton;
    @FXML
    private Pane tablePane;
    private ReportTableController tableController;
    private List<Habit> selectedHabits;

    @FXML
    private void initialize() {
        tableController = new ReportTableController();
        tablePane.getChildren().add(windowCreator().getNode(HABIT_REPORT_TABLE, tableController));

        selectedHabits = new ArrayList<>();
        showButton.setOnAction(event -> showReport());
        associateButtonWithControls(showButton, startDatePicker, endDatePicker);
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
        startDatePicker.setValue(new java.sql.Date(shiftDate(
                new Date(), TimeUnit.MONTH, -1).getTime()).toLocalDate());
        endDatePicker.setValue(LocalDate.now());
    }

    private void showReport() {
        if (selectedHabits.size() == 0)
            return;

        tableController.showReport(Core.getInstance().habitsReportService()
                .getReport(selectedHabits, OrgUtils.getPeriodByDp(startDatePicker, endDatePicker)));
    }
}
