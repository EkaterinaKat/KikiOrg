package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.date.Period;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Description;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.fx.FxUtils.*;

class HistoryController implements FxController {
    private static final int BLOCK_WIDTH = 760;
    @FXML
    private ComboBox<Habit> habitComboBox;
    @FXML
    private Button showButton;
    @FXML
    private VBox historyPane;

    @FXML
    private void initialize() {
        associateButtonWithControls(showButton, habitComboBox);
        setComboBoxItems(habitComboBox, Core.getInstance().habitsService().getAllHabits());
        showButton.setOnAction(event -> show());
    }

    private void show() {
        historyPane.getChildren().clear();
        historyPane.getChildren().add(getPaneWithHeight(20));
        List<Description> descriptions = Core.getInstance().habitsService().getAllHabitDescriptions(habitComboBox.getValue());
        for (Description description : descriptions) {
            historyPane.getChildren().add(descToBlock(habitComboBox.getValue(), description));
            historyPane.getChildren().add(FxUtils.getPaneWithHeight(20));
        }
    }

    private Pane descToBlock(Habit habit, Description description) {
        VBox vBox = new VBox();

        Label datesLabel;
        if (description.equals(habit.getCurrentDescription())) {
            datesLabel = new Label(String.format("%s-present time", READABLE_DATE_FORMAT.format(description.getBeginningDate().getValue())));
        } else {
            datesLabel = new Label(DateUtils.getStringRepresentationOfPeriod(
                    new Period(description.getBeginningDate().getValue(), description.getEndDate().getValue())));
        }

        Label textLabel = new Label(description.getText());
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(BLOCK_WIDTH - 40);

        vBox.getChildren().addAll(getPaneWithHeight(15), datesLabel, getPaneWithHeight(15), textLabel, getPaneWithHeight(15));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(getPaneWithWidth(15), vBox);
        hBox.setStyle(Styler.getBlackBorderStyle());
        hBox.setPrefWidth(BLOCK_WIDTH);
        return hBox;
    }
}
