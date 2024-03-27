package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.general.GeneralUtils;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.study.entity.SubjValue;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.frame;
import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;

public class StudyAdminController implements SectionController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button newSubjectButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descLabel;
    @FXML
    private VBox valuesPane;
    private Map<Long, Label> subjectIdPointLabelMap;

    @FXML
    private void initialize() {
        fillSubjectTable();
        newSubjectButton.setOnAction(event -> openSubjectEditWindow(null));
    }

    private void fillSubjectTable() {
        fillSubjectTable(null);
    }

    private void fillSubjectTable(Subject subjectToShow) {
        boolean indToShowWasShowed = false;
        gridPane.getChildren().clear();
        List<Subject> habits = Core.getInstance().studyService().getAllSubjects();
        subjectIdPointLabelMap = new HashMap<>();
        int rowIndex = 0;
        for (Subject subject : habits) {
            Label label = new Label(subject.getTitleAndArchivedInfo());
            label.setContextMenu(getContextMenu(subject));
            Label point = new Label();
            subjectIdPointLabelMap.put(subject.getId(), point);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                showSubject(subject);
            });
            gridPane.add(point, 0, rowIndex);
            gridPane.add(label, 1, rowIndex);
            rowIndex++;

            if (subject.equals(subjectToShow)) {
                showSubject(subject);
                indToShowWasShowed = true;
            }

            if (subject.getArchived()) {
                label.setStyle(Styler.getColorfullStyle(Styler.ThingToColor.TEXT, Styler.StandardColor.GRAY));
            }
        }
        if (!indToShowWasShowed) {
            showSubject(null);
        }
    }

    private ContextMenu getContextMenu(Subject subject) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> openSubjectEditWindow(subject));
        contextMenu.getItems().add(editItem);

        MenuItem newValueItem = new MenuItem("New value");
        newValueItem.setOnAction(event -> openValueEditDialog(subject, null));
        contextMenu.getItems().add(newValueItem);

        MenuItem archiveItem = new MenuItem("Archive");
        archiveItem.setOnAction(event1 -> {
            Core.getInstance().studyService().archive(subject);
            fillSubjectTable(subject);
        });
        contextMenu.getItems().add(archiveItem);

        MenuItem hideItem = new MenuItem("Hide");
        hideItem.setOnAction(event1 -> {
            Core.getInstance().studyService().hide(subject);
            fillSubjectTable(subject);
        });
        contextMenu.getItems().add(hideItem);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> {
            String question = String.format("Delete %s?", subject.getTitle());
            new StandardDialogBuilder().openQuestionDialog(question, b -> {
                if (b) {
                    Core.getInstance().studyService().delete(subject);
                    fillSubjectTable(subject);
                }
            });
        });
        contextMenu.getItems().add(deleteItem);

        return contextMenu;
    }

    private void showSubject(@Nullable Subject subject) {
        titleLabel.setText(subject != null ? subject.getTitleAndArchivedInfo() : "");
        descLabel.setText(subject != null ? subject.getDescription() : "");

        subjectIdPointLabelMap.values().forEach(label -> label.setText(""));
        if (subject != null) {
            subjectIdPointLabelMap.get(subject.getId()).setText("* ");
        }
        fillValuesPane(subject);
    }

    private void fillValuesPane(@Nullable Subject subject) {
        valuesPane.getChildren().clear();

        if (subject == null) {
            return;
        }

        for (SubjValue value : subject.getSortedValues()) {
            Label label = new Label(value.getTitleAndDesc());
            label.setStyle(Styler.getColorfullStyle(Styler.ThingToColor.TEXT, Styler.StandardColor.BLACK));
            FxUtils.setWidth(label, 400);
            label.setWrapText(true);

            Node node = frame(label, 10);

            valuesPane.getChildren().addAll(node, getPaneWithHeight(10));
            if (!GeneralUtils.isEmpty(value.getColor())) {
                try {
                    Styler.setBackgroundColorAndCorrectTextColor(node, label, value.getColor());
                } catch (Exception e) {
                }
            } else {
                node.setStyle(Styler.getBlackBorderStyle());
            }

            if (value.isDefaultValue()) {
                node.setStyle(node.getStyle() + Styler.getBlackBorderStyle() + Styler.getBorderWidth(5));
            }

            node.setOnContextMenuRequested(event -> showValueContextMenu(event, node, value, subject));
        }
    }

    private void openSubjectEditWindow(Subject subject) {
        DcTextField titleField = new DcTextField(true, subject == null ? "" : subject.getTitle());
        DcTextArea descField = new DcTextArea(false, subject == null ? "" : subject.getDescription());
        DialogConstructor.constructDialog(() -> {
            Core.getInstance().studyService().save(subject, titleField.getValue(), descField.getValue());
            fillSubjectTable(subject);
        }, titleField, descField);
    }

    private void openValueEditDialog(Subject subject, SubjValue value) {
        DcTextField titleField = new DcTextField(true, value == null ? "" : value.getTitle());
        DcTextField colorField = new DcTextField(false, value == null ? "" : value.getColor());
        DcTextArea descField = new DcTextArea(false, value == null ? "" : value.getDescription());
        DialogConstructor.constructDialog(() -> {
            Core.getInstance().studyService().save(subject, value, titleField.getValue(),
                    descField.getValue(), colorField.getValue());
            fillSubjectTable(subject);
        }, titleField, colorField, descField);
    }

    private void showValueContextMenu(ContextMenuEvent event, Node node, SubjValue value, Subject subject) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> openValueEditDialog(subject, value));
        contextMenu.getItems().add(editItem);

        MenuItem makeDefaultItem = new MenuItem("Make default");
        makeDefaultItem.setOnAction(event1 -> {
            Core.getInstance().studyService().makeDefault(value);
            fillSubjectTable(subject);
        });
        contextMenu.getItems().add(makeDefaultItem);

        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }
}
