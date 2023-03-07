package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.PageableBlockListController;
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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.TASK_PANE;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

class BoardController implements FxController {
    private PageableBlockListController<Task> blockListController;
    @FXML
    private Button addTaskButton;
    @FXML
    private Pane tasksPane;
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
        Component<PageableBlockListController<Task>> component =
                new ComponentBuilder().setSize(new Size(750, 890)).getPageableBlockListComponent();
        tasksPane.getChildren().add(component.getNode());
        blockListController = component.getController();

        setComboBoxItems(statusComboBox, TaskStatus.values(), TaskStatus.TODO);
        setComboBoxItems(sortComboBox, SortType.values(), SortType.CREATION_DATE);
        tuneProjectComboBox();
        updateContent();

        DcTextField titleField = new DcTextField(true, "");
        DcComboBox<Project> projectDcComboBox =
                new DcComboBox<>(true, null, Core.getInstance().trackerService().getAllProjects());
        DcTextArea descField = new DcTextArea(true, "");
        addTaskButton.setOnAction(event ->
                DialogConstructor.constructDialog(() -> {
                    Core.getInstance().trackerService()
                            .createTask(titleField.getValue(), descField.getValue(), projectDcComboBox.getValue());
                    updateContent();
                }, titleField, projectDcComboBox, descField));

        statusComboBox.setOnAction(event -> {
            tuneProjectComboBox();
            updateContent();
        });
        sortComboBox.setOnAction(event -> updateContent());
        projectComboBox.setOnAction(event -> updateContent());
        showAllButton.setOnAction(event -> {
            projectComboBox.setValue(null);
            updateContent();
        });
    }

    private void tuneProjectComboBox() {
        projectComboBox.setValue(null);
        setComboBoxItems(projectComboBox,
                Core.getInstance().trackerService().getProjectsWithTasksInStatus(statusComboBox.getValue()));
    }

    private Page<Task> getTaskPage(int pageNum) {
        return Core.getInstance().boardSortService().getTaskPage(
                projectComboBox.getValue(), statusComboBox.getValue(), sortComboBox.getValue(), pageNum);
    }

    private void updateContent() {
        statisticsLabel.setText(Core.getInstance().trackerService().getStatistics());
        blockListController.show(this::getTaskPage, (task, blockWidth) -> {
            TaskPaneController taskPaneController = new TaskPaneController(task, this::updateContent, blockWidth);
            return windowCreator().getNode(TASK_PANE, taskPaneController);
        });
    }
}
