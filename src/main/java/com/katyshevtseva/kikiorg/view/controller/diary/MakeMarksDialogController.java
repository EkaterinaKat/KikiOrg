package com.katyshevtseva.kikiorg.view.controller.diary;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndMark;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndValue;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class MakeMarksDialogController implements FxController {
    private final NoArgsKnob onSaveKnob;
    private final List<Line> lines = new ArrayList<>();
    @FXML
    private GridPane indicatorPane;
    @FXML
    private Button saveButton;
    @FXML
    private DatePicker datePicker;


    public MakeMarksDialogController(NoArgsKnob onSaveKnob) {
        this.onSaveKnob = onSaveKnob;
    }

    @FXML
    private void initialize() {
        datePicker.setOnAction(event -> fillPane());

        saveButton.setOnAction(event -> {
            save();
            onSaveKnob.execute();
            FxUtils.closeWindowThatContains(saveButton);
        });
    }

    private void fillPane() {
        indicatorPane.getChildren().clear();
        List<Indicator> indicators = Core.getInstance().diaryService().getIndicators();
        for (int i = 0; i < indicators.size(); i++) {
            Indicator indicator = indicators.get(i);
            Label label = new Label(indicator.getTitle());
            label.setTooltip(new Tooltip(indicator.getDescription()));
            indicatorPane.add(label, 1, i + 1);

            ComboBox<IndValue> valueComboBox = new ComboBox<>();
            FxUtils.setWidth(valueComboBox, 150);
            FxUtils.setComboBoxItems(valueComboBox, indicator.getSortedValues());
            indicatorPane.add(valueComboBox, 2, i + 1);

            TextArea commentArea = new TextArea();
            FxUtils.setSize(commentArea, new Size(100, 300));
            commentArea.setWrapText(true);
            indicatorPane.add(commentArea, 3, i + 1);

            IndMark mark = Core.getInstance().diaryService().getMark(indicator, FxUtils.getDate(datePicker)).orElse(null);
            if (mark != null) {
                valueComboBox.setValue(mark.getValue());
                commentArea.setText(mark.getComment());
            }

            lines.add(new Line(indicator, valueComboBox, commentArea));
        }
    }

    private void save() {
        for (Line line : lines) {
            Core.getInstance().diaryService().saveMark(
                    line.getIndicator(),
                    FxUtils.getDate(datePicker),
                    line.getValueComboBox().getValue(),
                    line.textArea.getText());
        }
    }

    @AllArgsConstructor
    @Data
    private static class Line {
        Indicator indicator;
        ComboBox<IndValue> valueComboBox;
        TextArea textArea;
    }
}
