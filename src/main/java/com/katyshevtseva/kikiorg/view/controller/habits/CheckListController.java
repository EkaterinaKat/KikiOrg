package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.AnalysisService;
import com.katyshevtseva.kikiorg.core.sections.habits.AnalysisService.AnalysisResult;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitMarkService;
import com.katyshevtseva.kikiorg.core.sections.habits.HabitsService;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.fx.FxUtils.*;
import static com.katyshevtseva.fx.Styler.ThingToColor.TEXT;
import static com.katyshevtseva.fx.Styler.getColorfullStyle;
import static com.katyshevtseva.fx.Styler.getTextSizeStyle;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.HABIT_REPORT_TABLE;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

class CheckListController implements SectionController {
    private final HabitsService habitsService = Core.getInstance().habitsService();
    private final HabitMarkService habitMarkService = Core.getInstance().habitMarkService();
    private final AnalysisService analysisService = Core.getInstance().analysisService();
    @FXML
    private DatePicker datePicker;
    @FXML
    private VBox habitsPane;
    @FXML
    private Pane tablePane;
    @FXML
    private VBox statisticsBox;
    private ReportTableController tableController;

    @FXML
    private void initialize() {
        tableController = new ReportTableController();
        tablePane.getChildren().add(windowCreator().getNode(HABIT_REPORT_TABLE, tableController));
        updateSectionContent();
        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(event -> fillHabitsTable());
        fillHabitsTable();
    }

    private void fillHabitsTable() {
        List<Habit> habits = habitsService.getActiveHabits();
        habitsPane.getChildren().clear();

        for (Habit habit : habits) {
            boolean markExists = habitMarkService.getMarkOrNull(habit, getDate(datePicker)) != null;

            HBox buttons = new HBox();

            Button doneButton = new Button("Done");
            doneButton.setDisable(markExists);
            doneButton.setOnAction(event -> {
                saveAndHandleException(habit, getDate(datePicker), true);
                fillHabitsTable();
                updateSectionContent();
            });

            Button notDoneButton = new Button("Not done");
            notDoneButton.setDisable(!markExists);
            notDoneButton.setOnAction(event -> {
                saveAndHandleException(habit, getDate(datePicker), false);
                fillHabitsTable();
                updateSectionContent();
            });

            buttons.getChildren().addAll(doneButton, getPaneWithWidth(7), notDoneButton);
            habitsPane.getChildren().addAll(new Label(habit.getTitle()), buttons, getPaneWithHeight(20));
        }
    }

    private void saveAndHandleException(Habit habit, Date date, boolean markValue) {
        try {
            habitMarkService.saveMarkOrRewriteIfExists(habit, date, markValue);
        } catch (Exception e) {
            new StandardDialogBuilder().setSize(200, 400).openInfoDialog(e.getMessage());
        }
    }

    private void updateSectionContent() {
        tableController.showReport(Core.getInstance().habitsReportService().getQuickReport());

        statisticsBox.getChildren().clear();
        for (AnalysisResult analysisResult : analysisService.analyzeStabilityAndAssignNewStatusIfNeeded(habitsService.getActiveHabits())) {
            Label label = new Label(analysisResult.getFullText());
            if (analysisResult.getHabit().getStabilityStatus() != null) {
                label.setStyle(getColorfullStyle(TEXT, analysisResult.getHabit().getStabilityStatus().getColor()));
            }
            label.setStyle(label.getStyle() + getTextSizeStyle(15));
            statisticsBox.getChildren().add(label);
        }
    }
}
