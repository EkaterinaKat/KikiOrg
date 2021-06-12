package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.Task;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.util.List;

class BoardController implements FxController {
    @FXML
    private Button addTaskButton;
    @FXML
    private VBox taskPane;
    @FXML
    private ComboBox<TaskType> taskTypeComboBox;

    private enum TaskType {
        CURRENT, ARCHIVE
    }

    @FXML
    private void initialize() {
        FxUtils.setComboBoxItems(taskTypeComboBox, TaskType.values(), TaskType.CURRENT);
        updateBoard();
        addTaskButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openTaskDialog(new TaskDialogController(this::updateBoard)));
        taskTypeComboBox.setOnAction(event -> updateBoard());
    }

    private void updateBoard() {
        taskPane.getChildren().clear();
        List<Task> tasks = null;
        switch (taskTypeComboBox.getValue()) {
            case CURRENT:
                tasks = Core.getInstance().trackerService().getTodoTasks();
                break;
            case ARCHIVE:
                tasks = Core.getInstance().trackerService().getAllDoneAndRejectedTasks();
        }
        for (Task task : tasks) {
            taskPane.getChildren().add(FxUtils.getPaneWithHeight(20));
            taskPane.getChildren().add(OrganizerWindowCreator.getInstance().getTaskPaneNode(new TaskPaneController(task)));
        }
        taskPane.getChildren().add(FxUtils.getPaneWithHeight(20));
    }
}
