package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.view.controller.dialog.HabitEditDialogController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

class AdminController implements FxController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button newHabitButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Button editButton;
    private List<Label> listPoints;

    @FXML
    private void initialize() {
        fillHabitTable();
        newHabitButton.setOnAction(event -> createHabit());
    }

    private void fillHabitTable() {
        gridPane.getChildren().clear();
        List<Habit> habits = Core.getInstance().habitsService().getAllHabits();
        listPoints = new ArrayList<>();
        int rowIndex = 0;
        for (Habit habit : habits) {
            Label label = new Label(habit.getTitle());
            if (habit.isActive())
                label.setStyle(Utils.getGreenTextStyle());
            else
                label.setStyle(Utils.getGrayTextStyle());
            Label point = new Label();
            listPoints.add(point);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                habitClickHandler(habit, point);
            });
            gridPane.add(point, 0, rowIndex);
            gridPane.add(label, 1, rowIndex);
            rowIndex++;
        }
    }

    private void habitClickHandler(Habit habit, Label point) {
        showHabit(habit);
        listPoints.forEach(label1 -> label1.setText(""));
        point.setText("*");
        editButton.setOnAction(event1 -> OrganizerWindowCreator.getInstance().openHabitEditDialog(
                new HabitEditDialogController(habit, savedHabit -> {
                    fillHabitTable();
                    showHabit(savedHabit);
                })));
    }

    private void createHabit() {
        OrganizerWindowCreator.getInstance().openHabitEditDialog(
                new HabitEditDialogController(null, savedHabit -> {
                    fillHabitTable();
                    showHabit(savedHabit);
                }));
    }

    private void showHabit(Habit habit) {
        titleLabel.setText(habit.getTitleWithActiveInfoAndEnumElements());
        descLabel.setText(habit.getDescription());
        typeLabel.setText("type: " + habit.getType() + "; group: " + habit.getHabitGroup() + ".");
    }
}
