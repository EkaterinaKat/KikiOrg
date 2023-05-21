package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.Styler.StandardColor;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcComboBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.ActivityStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.PkdType;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.List;

import static com.katyshevtseva.fx.FxUtils.frame;
import static com.katyshevtseva.fx.FxUtils.getLabel;
import static com.katyshevtseva.fx.Styler.StandardColor.*;
import static com.katyshevtseva.fx.Styler.ThingToColor.*;
import static com.katyshevtseva.kikiorg.core.sections.structure.ActivityStatus.*;

public class ActivitiesController implements SectionController {
    @FXML
    private GridPane gridPane;
    @FXML
    private Button activityCreationButton;
    @FXML
    private Button clearGoalButton;
    @FXML
    private ComboBox<ActivityStatus> statusComboBox;
    @FXML
    private ComboBox<Goal> goalComboBox;

    @FXML
    private void initialize() {
        activityCreationButton.setOnAction(event -> openActivityEditDialog(null));
    }

    private void tuneComboBoxes() {
        FxUtils.setComboBoxItems(statusComboBox, ActivityStatus.values(), ACTIVE);
        statusComboBox.setOnAction(event -> fillPane());

        FxUtils.setComboBoxItems(goalComboBox, Core.getInstance().goalService().getAllSortedByTitle());
        goalComboBox.setOnAction(event -> fillPane());

        clearGoalButton.setOnAction(event -> goalComboBox.setValue(null));
    }

    @Override
    public void update() {
        tuneComboBoxes();
        fillPane();
    }

    private void fillPane() {
        gridPane.getChildren().clear();
        List<Activity> activities = Core.getInstance().activityService()
                .getActivities(statusComboBox.getValue(), goalComboBox.getValue());

        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);

            Node titleNode = getTableCell(activity.getTitle(), 18, WHITE, DARK_BLUE, WHITE);
            titleNode.setOnContextMenuRequested(event -> showActivityContextMenu(event, activity, titleNode));

            gridPane.add(titleNode, 0, i);
            gridPane.add(getTableCell(activity.getDescription()), 1, i);
            gridPane.add(getTableCell(activity.getGoal() != null ? activity.getGoal().getTitle() : "---"), 2, i);
            gridPane.add(getTableCell(activity.getPkdType() != null ? activity.getPkdType().toString() : "---"), 3, i);
        }
    }

    private Node getTableCell(String text) {
        return getTableCell(text, 15, BLACK, WHITE, BLACK);
    }

    private Node getTableCell(String text,
                              int textSize,
                              StandardColor textColor,
                              StandardColor backgroundColor,
                              StandardColor borderColor) {
        Label label = getLabel(text, 300);
        label.setStyle(Styler.getColorfullStyle(TEXT, textColor) +
                Styler.getTextSizeStyle(textSize));
        label.setPadding(new Insets(textSize));

        Node node = frame(label, 7);
        node.setStyle(Styler.getColorfullStyle(BACKGROUND, backgroundColor) +
                Styler.getColorfullStyle(BORDER, borderColor));
        return node;
    }

    private void openActivityEditDialog(Activity activity) {
        DcTextField titleField = new DcTextField(true, activity != null ? activity.getTitle() : "");
        DcTextArea descField = new DcTextArea(false, activity != null ? activity.getDescription() : "");
        DcComboBox<Goal> goalBox = new DcComboBox<>(true, activity != null ? activity.getGoal() : null,
                Core.getInstance().goalService().getAllSortedByTitle());
        DcComboBox<PkdType> pkdBox = new DcComboBox<>(true, activity != null ? activity.getPkdType() : null,
                Arrays.asList(PkdType.values()));
        DialogConstructor.constructDialog(() -> {
            Core.getInstance().activityService().save(activity, titleField.getValue(), descField.getValue(),
                    goalBox.getValue(), pkdBox.getValue());
            fillPane();
        }, titleField, descField, goalBox, pkdBox);
    }

    private void showActivityContextMenu(ContextMenuEvent event, Activity activity, Node node) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> openActivityEditDialog(activity));

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().activityService().delete(activity);
                fillPane();
            }
        }));

        if (activity.getStatus() == ACTIVE || activity.getStatus() == SUSPENDED) {
            MenuItem finishItem = new MenuItem("Finish");
            finishItem.setOnAction(event1 -> {
                Core.getInstance().activityService().setStatus(activity, FINISHED);
                fillPane();
            });
            contextMenu.getItems().add(finishItem);
        }

        if (activity.getStatus() == FINISHED || activity.getStatus() == SUSPENDED) {
            MenuItem returnToWorkItem = new MenuItem("Return to work");
            returnToWorkItem.setOnAction(event1 -> {
                Core.getInstance().activityService().setStatus(activity, ACTIVE);
                fillPane();
            });
            contextMenu.getItems().add(returnToWorkItem);
        }

        if (activity.getStatus() == FINISHED || activity.getStatus() == ACTIVE) {
            MenuItem suspendItem = new MenuItem("Suspend");
            suspendItem.setOnAction(event1 -> {
                Core.getInstance().activityService().setStatus(activity, SUSPENDED);
                fillPane();
            });
            contextMenu.getItems().add(suspendItem);
        }


        contextMenu.getItems().addAll(editItem, deleteItem);
        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }
}
