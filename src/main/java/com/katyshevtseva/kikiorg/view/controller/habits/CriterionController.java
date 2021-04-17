package com.katyshevtseva.kikiorg.view.controller.habits;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.Habit;
import com.katyshevtseva.kikiorg.core.sections.habits.entity.StabilityCriterion;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.fx.FxUtils.disableNonNumericChars;

class CriterionController implements FxController {
    @FXML
    private GridPane criterionPane;
    @FXML
    private Button saveButton;
    private List<Row> rows;

    @FXML
    private void initialize() {
        fillGridPane();
        saveButton.setOnAction(event -> saveButtonListener());
    }

    private void fillGridPane() {
        rows = new ArrayList<>();
        int rowIndex = 1;
        for (Habit habit : Core.getInstance().habitsService().getAllHabits()) {
            StabilityCriterion criterion = Core.getInstance().stabilityCriterionService().getCriterionByHabitOrNull(habit);
            TextField habitDoneTextField = getTextField(criterion != null ? "" + criterion.getDaysHabitDone() : "");
            TextField totalTextField = getTextField(criterion != null ? "" + criterion.getDaysTotal() : "");
            rows.add(new Row(habit, habitDoneTextField, totalTextField));
            HBox hBox = new HBox();
            hBox.getChildren().addAll(habitDoneTextField, getSlash(), totalTextField);
            criterionPane.add(new Label(habit.getTitle()), 1, rowIndex);
            criterionPane.add(hBox, 2, rowIndex);
            rowIndex++;
        }
    }

    private Label getSlash() {
        Label label = new Label("/");
        label.setStyle(Styler.getTextSizeStyle(35));
        return label;
    }

    private TextField getTextField(String initText) {
        TextField textField = new TextField();
        textField.setPrefWidth(50);
        disableNonNumericChars(textField);
        textField.setText(initText);
        textField.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(false));
        return textField;
    }

    private void saveButtonListener() {
        for (Row row : rows) {
            Integer total = row.getDaysTotalOrNull();
            Integer habitsDone = row.getDaysHabitDoneOrNull();
            if (total != null && habitsDone != null)
                Core.getInstance().stabilityCriterionService().saveOrRewriteCriterion(
                        row.habit, total, habitsDone);
        }
        saveButton.setDisable(true);
    }

    private class Row {
        private Habit habit;
        private TextField habitDoneTextField;
        private TextField totalTextField;

        Row(Habit habit, TextField habitDoneTextField, TextField totalTextField) {
            this.habit = habit;
            this.habitDoneTextField = habitDoneTextField;
            this.totalTextField = totalTextField;
        }

        Integer getDaysHabitDoneOrNull() {
            if (habitDoneTextField.getText().equals(""))
                return null;
            return Integer.parseInt(habitDoneTextField.getText());
        }

        Integer getDaysTotalOrNull() {
            if (totalTextField.getText().equals(""))
                return null;
            return Integer.parseInt(totalTextField.getText());
        }
    }
}
