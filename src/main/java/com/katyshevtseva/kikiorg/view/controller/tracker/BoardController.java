package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.BoardSortService.SortType;
import com.katyshevtseva.kikiorg.core.sections.tracker.Task;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import com.katyshevtseva.kikiorg.view.controller.pagination.PaginationPaneController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;

class BoardController implements FxController {
    private PaginationPaneController<Task> paginationPaneController;
    @FXML
    private Pane paginationPane;
    @FXML
    private Button addTaskButton;
    @FXML
    private VBox taskPane;
    @FXML
    private ComboBox<TaskStatus> statusComboBox;
    @FXML
    private Label statisticsLabel;
    @FXML
    private ComboBox<SortType> sortComboBox;

    @FXML
    private void initialize() {
        setComboBoxItems(statusComboBox, TaskStatus.values(), TaskStatus.TODO);
        setComboBoxItems(sortComboBox, SortType.values(), SortType.CREATION_DATE);
        tunePagination();
        addTaskButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openTaskDialog(new TaskDialogController(null, () -> paginationPaneController.loadPage())));
        statusComboBox.setOnAction(event -> paginationPaneController.loadPage());
        sortComboBox.setOnAction(event -> paginationPaneController.loadPage());
    }

    private void tunePagination() {
        paginationPaneController = new PaginationPaneController<>(this::getTaskPage, this::setContent);
        paginationPane.getChildren().add(OrganizerWindowCreator.getInstance().getPaginationPaneNode(paginationPaneController));
    }

    private Page<Task> getTaskPage(int pageNum) {
        return Core.getInstance().boardSortService().getTaskPage(statusComboBox.getValue(), sortComboBox.getValue(), pageNum);
    }

    private void setContent(List<Task> tasks) {
        updateStatistics();
        taskPane.getChildren().clear();

        for (Task task : tasks) {
            taskPane.getChildren().add(FxUtils.getPaneWithHeight(20));
            taskPane.getChildren().add(OrganizerWindowCreator.getInstance()
                    .getTaskPaneNode(new TaskPaneController(task, () -> paginationPaneController.loadPage())));
        }
        taskPane.getChildren().add(FxUtils.getPaneWithHeight(20));
    }

    private void updateStatistics() {
        statisticsLabel.setText(Core.getInstance().trackerService().getStatistics());
    }
}
