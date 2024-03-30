package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.date.Month;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.fx.LabelBuilder;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Mark;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.date.DateUtils.getPeriodOfYearDateBelongsTo;
import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.Styler.StandardColor.GRAY;
import static com.katyshevtseva.fx.Styler.StandardColor.GREEN;
import static com.katyshevtseva.fx.Styler.ThingToColor.TEXT;
import static com.katyshevtseva.kikiorg.view.controller.habits.YearVisualisationUtil.*;

public class HistoryController implements SectionController {
    private Map<Long, Label> habitIdPointLabelMap;
    @FXML
    private GridPane habitListPane;
    @FXML
    private VBox historyPane;

    @FXML
    private void initialize() {
        fillHabitTable();
    }

    private void fillHabitTable() {
        habitListPane.getChildren().clear();
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
                show(habit);
            });
            habitListPane.add(point, 0, rowIndex);
            habitListPane.add(label, 1, rowIndex);
            rowIndex++;
        }
    }

    private void show(Habit habit) {
        habitIdPointLabelMap.values().forEach(label -> label.setText(""));
        habitIdPointLabelMap.get(habit.getId()).setText("* ");

        historyPane.getChildren().clear();
        List<Mark> marks = Core.getInstance().habitMarkService.getMarks(habit);

        for (Map.Entry<String, List<Mark>> yearEntry : splitIntoYears(marks).entrySet()) {
            List<Mark> yearMarks = yearEntry.getValue();
            String yearSummary = getSummary(yearMarks, getPeriodOfYearDateBelongsTo(yearMarks.get(0).getDate()));

            Label yearTitle = new LabelBuilder().textSize(30).text(yearEntry.getKey()).build();
            historyPane.getChildren().addAll(yearTitle, new Label(yearSummary));

            GridPane gridPane = new GridPane();
            gridPane.setHgap(15);

            Map<Month, List<Mark>> monthMarksMap = splitIntoMonths(yearMarks);

            for (Month month : Month.values()) {
                List<Mark> monthMarks = monthMarksMap.getOrDefault(month, new ArrayList<>());
                Period period = getPeriodOfMonth(yearEntry.getKey(), month);
                validateMarksAndPeriod(monthMarks, period);
                int rowIndex = 11 - month.getIndex();

                String monthSummary = getSummary(monthMarks, period);
                gridPane.add(new Label(month.getTitle()), 0, rowIndex);
                gridPane.add(new Label(monthSummary), 1, rowIndex);
                gridPane.add(getMonthVisualisation(monthMarks, period), 2, rowIndex);
            }

            historyPane.getChildren().addAll(gridPane, getPaneWithHeight(15));
        }
    }

}
