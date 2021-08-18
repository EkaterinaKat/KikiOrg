package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.BoardSortService.SortType;
import com.katyshevtseva.kikiorg.core.sections.tracker.Task;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;

class BoardController implements FxController {
    @FXML
    private Button addTaskButton;
    @FXML
    private VBox taskPane;
    @FXML
    private ComboBox<TaskType> taskTypeComboBox;
    @FXML
    private Label statisticsLabel;
    @FXML
    private ComboBox<SortType> sortComboBox;

    private enum TaskType {
        CURRENT, ARCHIVE, SHELVED
    }

    @FXML
    private void initialize() {
        setComboBoxItems(taskTypeComboBox, TaskType.values(), TaskType.CURRENT);
        setComboBoxItems(sortComboBox, SortType.values(), SortType.CREATION_DATE);
        updateBoard();
        addTaskButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openTaskDialog(new TaskDialogController(null, this::updateBoard)));
        taskTypeComboBox.setOnAction(event -> updateBoard());
        sortComboBox.setOnAction(event -> updateBoard());
    }

    private void updateBoard() {
        updateStatistics();
        taskPane.getChildren().clear();
        List<Task> tasks = null;
        switch (taskTypeComboBox.getValue()) {
            case CURRENT:
                tasks = Core.getInstance().boardSortService().getTodoTasks(sortComboBox.getValue());
                break;
            case SHELVED:
                tasks = Core.getInstance().boardSortService().getShelvedTasks(sortComboBox.getValue());
                break;
            case ARCHIVE:
                tasks = Core.getInstance().boardSortService().getAllDoneAndRejectedTasks(sortComboBox.getValue());
        }
        for (Task task : tasks) {
            taskPane.getChildren().add(FxUtils.getPaneWithHeight(20));
            taskPane.getChildren().add(OrganizerWindowCreator.getInstance().getTaskPaneNode(new TaskPaneController(task, this::updateBoard)));
        }
        taskPane.getChildren().add(FxUtils.getPaneWithHeight(20));
    }

    private void updateStatistics() {
        statisticsLabel.setText(Core.getInstance().trackerService().getStatistics());
    }
}
