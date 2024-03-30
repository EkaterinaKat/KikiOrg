package com.katyshevtseva.kikiorg.view.controller.diary;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.diary.DairyTableService.MarkToEdit;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndMark;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.IndValue;
import com.katyshevtseva.kikiorg.core.sections.diary.entity.Indicator;
import com.sun.istack.NotNull;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MakeMarksDialogController implements FxController {
    private final List<Line> hiddenLines = new ArrayList<>();
    private final NoArgsKnob onSaveKnob;
    private final MarkToEdit mark;
    private final List<Line> lines = new ArrayList<>();
    private boolean hiddenShown = false;
    @FXML
    private GridPane indicatorPane;
    @FXML
    private Button saveButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private GridPane hiddenIndicatorPane;
    @FXML
    private Button showHiddenButton;


    public MakeMarksDialogController(NoArgsKnob onSaveKnob) {
        this.onSaveKnob = onSaveKnob;
        mark = null;
    }

    public MakeMarksDialogController(NoArgsKnob onSaveKnob, @NotNull MarkToEdit mark) {
        this.onSaveKnob = onSaveKnob;
        this.mark = mark;
    }

    @FXML
    private void initialize() {
        if (mark == null) {
            datePicker.setOnAction(event -> fillPaneWithAllIndicators());
            showHiddenButton.setOnAction(event -> hiddenButtonListener());
        } else {
            FxUtils.setDate(datePicker, mark.getDate());
            datePicker.setDisable(true);
            addIndicatorToPane(mark.getIndicator(), 0, indicatorPane, lines);
        }

        saveButton.setOnAction(event -> {
            save();
            onSaveKnob.execute();
            FxUtils.closeWindowThatContains(saveButton);
        });
    }

    private void hiddenButtonListener() {
        hiddenShown = !hiddenShown;
        if (hiddenShown) {
            showHiddenButton.setText("Hide");

            List<Indicator> indicators = Core.getInstance().diaryService.getActiveHiddenIndicators();
            for (int i = 0; i < indicators.size(); i++) {
                addIndicatorToPane(indicators.get(i), i, hiddenIndicatorPane, hiddenLines);
            }
        } else {
            showHiddenButton.setText("Show hidden");
            hiddenIndicatorPane.getChildren().clear();
            hiddenLines.clear();
        }
    }

    private void fillPaneWithAllIndicators() {
        showHiddenButton.setVisible(true);
        List<Indicator> indicators = Core.getInstance().diaryService.getActiveNotHiddenIndicators();
        for (int i = 0; i < indicators.size(); i++) {
            addIndicatorToPane(indicators.get(i), i, indicatorPane, lines);
        }
    }

    private void addIndicatorToPane(Indicator indicator, int row, GridPane pane, List<Line> lineList) {
        Label label = new Label(indicator.getTitle());
        label.setTooltip(new Tooltip(indicator.getDescription()));
        pane.add(label, 1, row + 1);

        ComboBox<IndValue> valueComboBox = new ComboBox<>();
        FxUtils.setWidth(valueComboBox, 150);
        FxUtils.setComboBoxItems(valueComboBox, indicator.getSortedValues());
        indicator.getDefaultValue().ifPresent(valueComboBox::setValue);
        pane.add(valueComboBox, 2, row + 1);

        TextArea commentArea = new TextArea();
        FxUtils.setSize(commentArea, new Size(100, 300));
        commentArea.setWrapText(true);
        pane.add(commentArea, 3, row + 1);

        IndMark mark = Core.getInstance().diaryService.getMark(indicator, FxUtils.getDate(datePicker)).orElse(null);
        if (mark != null) {
            valueComboBox.setValue(mark.getValue());
            commentArea.setText(mark.getComment());
        }

        lineList.add(new Line(indicator, valueComboBox, commentArea));
    }

    private void save() {
        for (Line line : Stream.concat(lines.stream(), hiddenLines.stream()).collect(Collectors.toList())) {
            Core.getInstance().diaryService.saveMark(
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
