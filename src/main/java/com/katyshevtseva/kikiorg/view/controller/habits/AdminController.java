package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.Styler.StandardColor.GRAY;
import static com.katyshevtseva.fx.Styler.StandardColor.GREEN;
import static com.katyshevtseva.fx.Styler.ThingToColor.TEXT;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.HABIT;

class AdminController implements SectionController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button newHabitButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button historyButton;
    private Map<Long, Label> habitIdPointLabelMap;

    @FXML
    private void initialize() {
        fillHabitTable();
        newHabitButton.setOnAction(event -> createHabit());
    }

    private void fillHabitTable() {
        gridPane.getChildren().clear();
        List<Habit> habits = Core.getInstance().habitsService.getAllHabits();
        habitIdPointLabelMap = new HashMap<>();
        int rowIndex = 0;
        for (Habit habit : habits) {
            Label label = new Label(habit.getTitle());
            if (habit.isActive())
                label.setStyle(Styler.getColorfullStyle(TEXT, GREEN));
            else
                label.setStyle(Styler.getColorfullStyle(TEXT, GRAY));
            Label point = new Label();
            habitIdPointLabelMap.put(habit.getId(), point);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                showHabit(habit);
            });
            gridPane.add(point, 0, rowIndex);
            gridPane.add(label, 1, rowIndex);
            rowIndex++;
        }
        if (habits.size() > 0)
            showHabit(habits.get(0));
    }

    private void showHabit(Habit habit) {
        titleLabel.setText(habit.getTitleWithActiveInfo());
        descLabel.setText(habit.getDescription());

        habitIdPointLabelMap.values().forEach(label -> label.setText(""));
        habitIdPointLabelMap.get(habit.getId()).setText("* ");
        editButton.setOnAction(event1 -> WindowBuilder.openDialog(HABIT,
                new HabitDialogController(habit, savedHabit -> {
                    fillHabitTable();
                    showHabit(savedHabit);
                })));
        historyButton.setOnAction(event -> new StandardDialogBuilder().openHistoryDialog(habit));
    }

    private void createHabit() {
        WindowBuilder.openDialog(HABIT,
                new HabitDialogController(null, savedHabit -> {
                    fillHabitTable();
                    showHabit(savedHabit);
                }));
    }
}
