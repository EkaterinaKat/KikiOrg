package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.ActivityStatus;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.GridPane;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.getLabel;
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
        tuneComboBoxes();
        activityCreationButton.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, "");
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().createActivity(titleField.getValue());
                fillPane();
            }, titleField);
        });
    }

    private void tuneComboBoxes() {
        FxUtils.setComboBoxItems(statusComboBox, ActivityStatus.values(), ACTIVE);
        statusComboBox.setOnAction(event -> fillPane());

        FxUtils.setComboBoxItems(goalComboBox, Core.getInstance().goalService().getAll());
        goalComboBox.setOnAction(event -> fillPane());

        clearGoalButton.setOnAction(event -> goalComboBox.setValue(null));
    }

    @Override
    public void update() {
        fillPane();
    }

    private void fillPane() {
        gridPane.getChildren().clear();
        List<Activity> activities = Core.getInstance().structureService()
                .getActivities(statusComboBox.getValue(), goalComboBox.getValue());

        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);

            Label titleLabel = getLabel(activity.getTitle(), 250);
            titleLabel.setOnContextMenuRequested(event -> showActivityContextMenu(event, activity, titleLabel));

            gridPane.add(titleLabel, 0, i);
            gridPane.add(getLabel(activity.getDescription(), 250), 1, i);
            gridPane.add(getLabel(activity.getGoal() != null ? activity.getGoal().getTitle() : "---", 250), 2, i);
        }
    }

    private void showActivityContextMenu(ContextMenuEvent event, Activity activity, Node node) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> {
            DcTextField titleField = new DcTextField(true, activity.getTitle());
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().edit(activity, titleField.getValue());
                fillPane();
            }, titleField);
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().structureService().delete(activity);
                fillPane();
            }
        }));

        if (activity.getStatus() == ACTIVE || activity.getStatus() == SUSPENDED) {
            MenuItem finishItem = new MenuItem("Finish");
            finishItem.setOnAction(event1 -> {
                Core.getInstance().structureService().setStatus(activity, FINISHED);
                fillPane();
            });
            contextMenu.getItems().add(finishItem);
        }

        if (activity.getStatus() == FINISHED || activity.getStatus() == SUSPENDED) {
            MenuItem returnToWorkItem = new MenuItem("Return to work");
            returnToWorkItem.setOnAction(event1 -> {
                Core.getInstance().structureService().setStatus(activity, ACTIVE);
                fillPane();
            });
            contextMenu.getItems().add(returnToWorkItem);
        }

        if (activity.getStatus() == FINISHED || activity.getStatus() == ACTIVE) {
            MenuItem suspendItem = new MenuItem("Suspend");
            suspendItem.setOnAction(event1 -> {
                Core.getInstance().structureService().setStatus(activity, SUSPENDED);
                fillPane();
            });
            contextMenu.getItems().add(suspendItem);
        }


        contextMenu.getItems().addAll(editItem, deleteItem);
        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }
}
