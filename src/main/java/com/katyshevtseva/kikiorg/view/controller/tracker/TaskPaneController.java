package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.sections.tracker.Task;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import static com.katyshevtseva.date.DateUtils.READABLE_DATE_FORMAT;
import static com.katyshevtseva.fx.FxUtils.getPaneWithWidth;
import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;
import static com.katyshevtseva.fx.Styler.getColorfullStyle;
import static com.katyshevtseva.kikiorg.view.utils.OrgUtils.getColorString;

class TaskPaneController implements FxController {
    private Task task;
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

    TaskPaneController(Task task) {
        this.task = task;
    }

    @FXML
    private void initialize() {
        root.setStyle(getColorfullStyle(BACKGROUND, getColorString(task.getProject().getColor())));
        titleLabel.setWrapText(true);
        descLabel.setWrapText(true);

        titleLabel.setText(String.format("%s-%d: %s", task.getProject().getCode(), task.getNumber(), task.getTitle()));
        if (task.getTaskStatus() == TaskStatus.TODO) {
            descLabel.setText(task.getDescription() + "\n\nCreation date: " + READABLE_DATE_FORMAT.format(task.getCreationDate().getValue()));
            buttonsPane.getChildren().addAll(
                    new Button("Edit"),
                    getPaneWithWidth(25),
                    new Button("Reject"),
                    getPaneWithWidth(25),
                    new Button("Done"));
        }
        if (task.getTaskStatus() == TaskStatus.DONE) {
            imageView.setImage(new Image("images/green_tick.png"));
            descLabel.setText(task.getDescription() + "\n\nCreation date: " + READABLE_DATE_FORMAT.format(task.getCreationDate().getValue())
                    + "\nCompletion date: " + READABLE_DATE_FORMAT.format(task.getCompletionDate()));
            buttonsPane.getChildren().addAll(
                    new Button("Edit"),
                    getPaneWithWidth(25),
                    new Button("Return to work"));
        }
        if (task.getTaskStatus() == TaskStatus.REJECTED) {
            imageView.setImage(new Image("images/gray_cross.png"));
            descLabel.setText(task.getDescription() + "\n\nCreation date: " + READABLE_DATE_FORMAT.format(task.getCreationDate().getValue()));
            buttonsPane.getChildren().addAll(
                    new Button("Edit"),
                    getPaneWithWidth(25),
                    new Button("Return to work"));
        }
    }
}
