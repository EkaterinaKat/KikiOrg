package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialogconstructor.DcComboBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Project;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.fx.FxImageCreationUtil.IconPicture.GREEN_TICK;
import static com.katyshevtseva.fx.FxImageCreationUtil.IconPicture.GREY_CROSS;
import static com.katyshevtseva.fx.FxImageCreationUtil.getIcon;
import static com.katyshevtseva.fx.FxUtils.getPaneWithWidth;
import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;
import static com.katyshevtseva.fx.Styler.getColorfullStyle;
import static com.katyshevtseva.kikiorg.view.utils.OrgUtils.getColorString;

class TaskPaneController implements FxController {
    private final Task task;
    private final NoArgsKnob boardUpdateKnob;
    private final int width;
    @FXML
    private HBox root;
    @FXML
    private ImageView imageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descLabel;
    @FXML
    private HBox buttonsPane;

    public TaskPaneController(Task task, NoArgsKnob boardUpdateKnob, int width) {
        this.task = task;
        this.boardUpdateKnob = boardUpdateKnob;
        this.width = width;
    }

    @FXML
    private void initialize() {
        root.setStyle(getColorfullStyle(BACKGROUND, getColorString(task.getProject().getColor())));
        FxUtils.setWidth(root, width);
        titleLabel.setWrapText(true);
        descLabel.setWrapText(true);

        titleLabel.setText(task.getNumberAndTitleInfo());

        if (task.getTaskStatus() == TaskStatus.TODO) {
            descLabel.setText(task.getDescription() + "\n\nCreation date: " + READABLE_DATE_FORMAT.format(task.getCreationDate().getValue()));
            buttonsPane.getChildren().addAll(
                    getEditButton(),
                    getPaneWithWidth(25),
                    getShelveButton(),
                    getPaneWithWidth(25),
                    getRejectButton(),
                    getPaneWithWidth(25),
                    getDoneButton());
        }

        if (task.getTaskStatus() == TaskStatus.SHELVED) {
            descLabel.setText(task.getDescription() + "\n\nCreation date: " + READABLE_DATE_FORMAT.format(task.getCreationDate().getValue()));
            buttonsPane.getChildren().addAll(
                    getEditButton(),
                    getPaneWithWidth(25),
                    getReturnToWorkButton());
        }

        if (task.getTaskStatus() == TaskStatus.DONE) {
            imageView.setImage(getIcon(GREEN_TICK));
            descLabel.setText(task.getDescription() + "\n\nCreation date: " + READABLE_DATE_FORMAT.format(task.getCreationDate().getValue())
                    + "\nCompletion date: " + READABLE_DATE_FORMAT.format(task.getCompletionDate().getValue()));
            buttonsPane.getChildren().addAll(
                    getEditButton(),
                    getPaneWithWidth(25),
                    getReturnToWorkButton());
        }

        if (task.getTaskStatus() == TaskStatus.REJECTED) {
            imageView.setImage(getIcon(GREY_CROSS));
            descLabel.setText(task.getDescription() + "\n\nCreation date: " + READABLE_DATE_FORMAT.format(task.getCreationDate().getValue()));
            buttonsPane.getChildren().addAll(
                    getEditButton(),
                    getPaneWithWidth(25),
                    getReturnToWorkButton());
        }
    }

    private Button getEditButton() {
        Button button = new Button("Edit");
        DcTextField titleField = new DcTextField(true, task.getTitle());
        DcComboBox<Project> projectDcComboBox =
                new DcComboBox<>(true, task.getProject(), Core.getInstance().trackerService().getAllProjects());
        projectDcComboBox.setDisabled(true);
        DcTextArea descField = new DcTextArea(true, task.getDescription());
        button.setOnAction(event -> DialogConstructor.constructDialog(() -> {
            task.setTitle(titleField.getValue());
            task.setDescription(descField.getValue());
            Core.getInstance().trackerService().saveEditedTask(task);
            boardUpdateKnob.execute();
        }, titleField, projectDcComboBox, descField));
        return button;
    }

    private Button getReturnToWorkButton() {
        Button returnToWorkButton = new Button("Return to work");
        returnToWorkButton.setOnAction(event -> {
            Core.getInstance().trackerService().returnTaskToWork(task);
            boardUpdateKnob.execute();
        });
        return returnToWorkButton;
    }

    private Button getDoneButton() {
        Button doneButton = new Button("Done");
        doneButton.setOnAction(event -> {
            Core.getInstance().trackerService().completeTask(task);
            boardUpdateKnob.execute();
        });
        return doneButton;
    }

    private Button getShelveButton() {
        Button shelveButton = new Button("Shelve");
        shelveButton.setOnAction(event -> {
            Core.getInstance().trackerService().shelveTask(task);
            boardUpdateKnob.execute();
        });
        return shelveButton;
    }

    private Button getRejectButton() {
        Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(event -> {
            Core.getInstance().trackerService().rejectTask(task);
            boardUpdateKnob.execute();
        });
        return rejectButton;
    }
}
