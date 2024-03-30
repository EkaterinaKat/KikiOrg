package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.study.StudyTableService.MarkToEdit;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjMark;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.sun.istack.NotNull;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class MakeMarksDialogController implements FxController {
    private final NoArgsKnob onSaveKnob;
    private final MarkToEdit mark;
    private final List<Line> lines = new ArrayList<>();
    @FXML
    private GridPane subjectPane;
    @FXML
    private Button saveButton;
    @FXML
    private DatePicker datePicker;

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

    private void fillPaneWithAllSubjects() {
        List<Subject> subjects = Core.getInstance().studyService().getActiveSubjects();
        for (int i = 0; i < subjects.size(); i++) {
            addSubjectToPane(subjects.get(i), i, subjectPane, lines);
        }
    }

    private void addSubjectToPane(Subject subject, int row, GridPane pane, List<Line> lineList) {
        SubjMark mark = Core.getInstance().studyService().getMark(subject, FxUtils.getDate(datePicker)).orElse(null);

        Label label = new Label(subject.getTitle());
        label.setTooltip(new Tooltip(subject.getDescription()));
        pane.add(label, 1, row + 1);

        TimeNode timeNode = new TimeNode();
        pane.add(timeNode.getNode(), 2, row + 1);
        if (mark != null) {
            timeNode.setTotalMin(mark.getMinutes());
        }

        TextArea commentArea = new TextArea();
        FxUtils.setSize(commentArea, new Size(100, 300));
        commentArea.setWrapText(true);
        pane.add(commentArea, 3, row + 1);
        if (mark != null) {
            commentArea.setText(mark.getComment());
        }

        lineList.add(new Line(subject, timeNode, commentArea));
    }

    private void save() {
        for (Line line : lines) {
            Core.getInstance().studyService().saveMark(
                    line.getSubject(),
                    FxUtils.getDate(datePicker),
                    line.timeNode.getTotalMin(),
                    line.textArea.getText());
        }
    }

    @AllArgsConstructor
    @Data
    private static class Line {
        Subject subject;
        TimeNode timeNode;
        TextArea textArea;
    }
}
