package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.study.StudyTableService.MarkToEdit;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjValue;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
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
    private GridPane subjectPane;
    @FXML
    private Button saveButton;
    @FXML
    private DatePicker datePicker;
    @FXML
    private GridPane hiddenSubjectPane;
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
            datePicker.setOnAction(event -> fillPaneWithAllSubjects());
            showHiddenButton.setOnAction(event -> hiddenButtonListener());
        } else {
            FxUtils.setDate(datePicker, mark.getDate());
            datePicker.setDisable(true);
            addSubjectToPane(mark.getSubject(), 0, subjectPane, lines);
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

            List<Subject> subjects = Core.getInstance().studyService().getActiveHiddenSubjects();
            for (int i = 0; i < subjects.size(); i++) {
                addSubjectToPane(subjects.get(i), i, hiddenSubjectPane, hiddenLines);
            }
        } else {
            showHiddenButton.setText("Show hidden");
            hiddenSubjectPane.getChildren().clear();
            hiddenLines.clear();
        }
    }

    private void fillPaneWithAllSubjects() {
        showHiddenButton.setVisible(true);
        List<Subject> subjects = Core.getInstance().studyService().getActiveNotHiddenSubjects();
        for (int i = 0; i < subjects.size(); i++) {
            addSubjectToPane(subjects.get(i), i, subjectPane, lines);
        }
    }

    private void addSubjectToPane(Subject subject, int row, GridPane pane, List<Line> lineList) {
        Label label = new Label(subject.getTitle());
        label.setTooltip(new Tooltip(subject.getDescription()));
        pane.add(label, 1, row + 1);

        ComboBox<SubjValue> valueComboBox = new ComboBox<>();
        FxUtils.setWidth(valueComboBox, 150);
        FxUtils.setComboBoxItems(valueComboBox, subject.getSortedValues());
        subject.getDefaultValue().ifPresent(valueComboBox::setValue);
        pane.add(valueComboBox, 2, row + 1);

        TextArea commentArea = new TextArea();
        FxUtils.setSize(commentArea, new Size(100, 300));
        commentArea.setWrapText(true);
        pane.add(commentArea, 3, row + 1);

        SubjMark mark = Core.getInstance().studyService().getMark(subject, FxUtils.getDate(datePicker)).orElse(null);
        if (mark != null) {
            valueComboBox.setValue(mark.getValue());
            commentArea.setText(mark.getComment());
        }

        lineList.add(new Line(subject, valueComboBox, commentArea));
    }

    private void save() {
        for (Line line : Stream.concat(lines.stream(), hiddenLines.stream()).collect(Collectors.toList())) {
            Core.getInstance().studyService().saveMark(
                    line.getSubject(),
                    FxUtils.getDate(datePicker),
                    line.getValueComboBox().getValue(),
                    line.textArea.getText());
        }
    }

    @AllArgsConstructor
    @Data
    private static class Line {
        Subject subject;
        ComboBox<SubjValue> valueComboBox;
        TextArea textArea;
    }
}
