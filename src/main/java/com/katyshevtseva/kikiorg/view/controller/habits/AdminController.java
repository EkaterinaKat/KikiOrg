package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitGroup;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.HabitUnion;
import com.katyshevtseva.kikiorg.view.controller.dialog.HabitEditDialogController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

class AdminController implements FxController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button newHabitButton;
    @FXML
    private Label habitTitleLabel;
    @FXML
    private Label habitTypeLabel;
    @FXML
    private Label habitDescLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button newUnionButton;
    @FXML
    private Pane habitPane;
    @FXML
    private Label unionTitleLabel;
    @FXML
    private Label unionDescLabel;
    @FXML
    private Pane unionPane;

    @FXML
    private void initialize() {
        fillHabitTable();
        newHabitButton.setOnAction(event -> createHabit());
        newUnionButton.setOnAction(event -> createUnion());
    }

    private void fillHabitTable() {
        gridPane.getChildren().clear();
        List<Habit> habits = Core.getInstance().habitsService().getAllHabits();
        List<HabitUnion> unions = Core.getInstance().habitUnionService().getAllHabitUnions();

        int rowIndex = 0;

        for (HabitUnion union : unions) {
            if (union.isActive()) {
                putItemToTable(union.getTitle().toUpperCase(), Utils.getGreenTextStyle(), rowIndex, event -> showUnion(union));
                rowIndex++;
            }
        }

        for (Habit habit : habits) {
            if (habit.isActive() && habit.getHabitUnion() == null) {
                putItemToTable(habit.getTitle(), Utils.getGreenTextStyle(), rowIndex, event -> showHabit(habit));
                rowIndex++;
            }
        }

        for (HabitUnion union : unions) {
            if (!union.isActive()) {
                putItemToTable(union.getTitle().toUpperCase(), Utils.getGrayTextStyle(), rowIndex, event -> showUnion(union));
                rowIndex++;
            }
        }

        for (Habit habit : habits) {
            if (!habit.isActive() && habit.getHabitUnion() == null) {
                putItemToTable(habit.getTitle(), Utils.getGrayTextStyle(), rowIndex, event -> showHabit(habit));
                rowIndex++;
            }
        }
    }

    private void putItemToTable(String text, String style, int rowIndex, EventHandler<MouseEvent> eventHandler) {
        Label label = new Label(text);
        label.setStyle(style);
        Label point = new Label();
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        gridPane.add(point, 0, rowIndex);
        gridPane.add(label, 1, rowIndex);

        if (rowIndex == 0)
            eventHandler.handle(null);
    }

    private void showHabit(Habit habit) {
        switchPane(unionPane, habitPane);

        habitTitleLabel.setText(habit.getTitleWithActiveInfoAndEnumElements());
        habitDescLabel.setText(habit.getDescription());
        habitTypeLabel.setText("type: " + habit.getType() + "; group: " + habit.getHabitGroup() + ".");

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

    private void showUnion(HabitUnion union) {
        switchPane(habitPane, unionPane);

        unionTitleLabel.setText(union.getTitle());
        unionDescLabel.setText("Group: " + union.getHabitGroup());

        editButton.setOnAction(event -> {
            new StandardDialogBuilder().setCssPath(Utils.getCssPath()).setTitle("Edit Union").
                    openTextFieldAndComboBoxDialog(union.getTitle(), Arrays.asList(HabitGroup.values()), union.getHabitGroup(),
                            (text, habitGroup) -> {
                                union.setTitle(text);
                                union.setHabitGroup((HabitGroup) habitGroup);
                                Core.getInstance().habitUnionService().saveEditedUnion(union);
                                fillHabitTable();
                                showUnion(union);
                            });
        });
    }

    private void createUnion() {
        new StandardDialogBuilder().setCssPath(Utils.getCssPath()).setTitle("New Union").
                openTextFieldAndComboBoxDialog("", Arrays.asList(HabitGroup.values()), HabitGroup.O,
                        (text, habitGroup) -> {
                            HabitUnion union = Core.getInstance().habitUnionService().createUnion(text, (HabitGroup) habitGroup);
                            fillHabitTable();
                            showUnion(union);
                        });
    }

    private void switchPane(Pane from, Pane to) {
        from.setVisible(false);
        from.setManaged(false);
        to.setVisible(true);
        to.setManaged(true);
    }
}
