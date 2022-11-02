package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.PaginationPaneController;
import com.katyshevtseva.fx.dialogconstructor.DcComboBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.BoardSortService.SortType;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Project;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
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
    private Button showAllButton;
    @FXML
    private ComboBox<Project> projectComboBox;

    @FXML
    private void initialize() {
        setComboBoxItems(statusComboBox, TaskStatus.values(), TaskStatus.TODO);
        setComboBoxItems(sortComboBox, SortType.values(), SortType.CREATION_DATE);
        tuneProjectComboBox();
        tunePagination();

        DcTextField titleField = new DcTextField(true, "");
        DcComboBox<Project> projectDcComboBox =
                new DcComboBox<>(true, null, Core.getInstance().trackerService().getAllProjects());
        DcTextArea descField = new DcTextArea(true, "");
        addTaskButton.setOnAction(event ->
                DialogConstructor.constructDialog(() -> {
                    Core.getInstance().trackerService()
                            .createTask(titleField.getValue(), descField.getValue(), projectDcComboBox.getValue());
                    paginationPaneController.loadPage();
                }, titleField, projectDcComboBox, descField));

        statusComboBox.setOnAction(event -> {
            tuneProjectComboBox();
            paginationPaneController.loadPage();
        });
        sortComboBox.setOnAction(event -> paginationPaneController.loadPage());
        projectComboBox.setOnAction(event -> paginationPaneController.loadPage());
        showAllButton.setOnAction(event -> {
            projectComboBox.setValue(null);
            paginationPaneController.loadPage();
        });
    }

    private void tuneProjectComboBox() {
        projectComboBox.setValue(null);
        setComboBoxItems(projectComboBox,
                Core.getInstance().trackerService().getProjectsWithTasksInStatus(statusComboBox.getValue()));
    }

    private void tunePagination() {
        Component<PaginationPaneController<Task>> component =
                new ComponentBuilder().getPaginationComponent(this::getTaskPage, this::setContent);
        paginationPaneController = component.getController();
        paginationPane.getChildren().add(component.getNode());
    }

    private Page<Task> getTaskPage(int pageNum) {
        return Core.getInstance().boardSortService().getTaskPage(
                projectComboBox.getValue(), statusComboBox.getValue(), sortComboBox.getValue(), pageNum);
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
