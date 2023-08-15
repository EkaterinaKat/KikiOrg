package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.PageableBlockListController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcCheckBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskService;
import com.katyshevtseva.kikiorg.core.sections.tracker.TaskStatus;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Sphere;
import com.katyshevtseva.kikiorg.core.sections.tracker.entity.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.TASK_PANE;

public class TasksController implements SectionController {
    private static final Size TASK_LIST_SIZE = new Size(800, 800);
    private Map<Sphere, Label> spherePointLabelMap;
    private PageableBlockListController<Task> taskListController;
    private final TaskService service = Core.getInstance().taskService();
    private Sphere selectedSphere;
    @FXML
    private GridPane spherePane;
    @FXML
    private Pane tasksPane;
    @FXML
    private Button newTaskButton;
    @FXML
    private Label statisticsLabel;
    @FXML
    private ComboBox<TaskStatus> statusBox;
    @FXML
    private TextField searchField;

    @FXML
    private void initialize() {
        statusBox.setOnAction(event -> updateTaskList());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> updateTaskList());
        FxUtils.setComboBoxItems(statusBox, TaskStatus.values(), TaskStatus.TODO);
        adjustCreationButtons();
    }

    @Override
    public void update() {
        fillSphereTable();
        adjustTasksLists();
    }

    private void adjustTasksLists() {
        ComponentBuilder.Component<PageableBlockListController<Task>> taskListComponent =
                new ComponentBuilder().setSize(TASK_LIST_SIZE).getPageableBlockListComponent();
        tasksPane.getChildren().add(taskListComponent.getNode());
        taskListController = taskListComponent.getController();
    }

    private void fillSphereTable() {
        spherePane.getChildren().clear();
        List<Sphere> spheres = Core.getInstance().sphereService().getAll();
        spherePointLabelMap = new HashMap<>();
        int rowIndex = 0;
        for (Sphere sphere : spheres) {
            Label label = FxUtils.getLabel(sphere.getTitle(), 18, null);
            label.setContextMenu(getMenu(sphere));
            if (!sphere.isActive()) {
                label.setStyle(Styler.getColorfullStyle(Styler.ThingToColor.TEXT, Styler.StandardColor.GRAY));
            } else {
                Label point = new Label();
                spherePointLabelMap.put(sphere, point);
                label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectSphere(sphere));
                spherePane.add(point, 1, rowIndex);
            }
            spherePane.add(label, 2, rowIndex);
            rowIndex++;
        }
        Label label = FxUtils.getLabel("<+>", 18, null);
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> showSphereEditDialog(null));
        spherePane.add(label, 2, rowIndex);

        statisticsLabel.setText(service.getStatistics());
    }

    private ContextMenu getMenu(Sphere sphere) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> showSphereEditDialog(sphere));
        contextMenu.getItems().add(editItem);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().sphereService().delete(sphere);
                fillSphereTable();
                selectSphere(null);
            }
        }));
        contextMenu.getItems().add(deleteItem);

        return contextMenu;
    }

    private void selectSphere(Sphere sphere) {
        setPoint(sphere);
        selectedSphere = sphere;

        if (sphere != null) {
            updateTaskList();
            newTaskButton.setVisible(true);
        } else {
            newTaskButton.setVisible(false);
            taskListController.clear();
        }
    }

    private void updateTaskList() {
        taskListController.show(pageNum -> service.getTasks(selectedSphere, statusBox.getValue(), pageNum, searchField.getText()),
                ((task, integer) -> taskToNode(task, integer, selectedSphere)));
    }

    private void adjustCreationButtons() {
        newTaskButton.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, "");
            DcTextArea descField = new DcTextArea(false, "");
            DialogConstructor.constructDialog(() -> {
                service.createTask(selectedSphere, titleField.getValue(), descField.getValue());
                fillSphereTable();
                selectSphere(selectedSphere);
            }, titleField, descField);
        });
    }

    private void showSphereEditDialog(Sphere sphere) {
        DcTextField titleField = new DcTextField(true, sphere == null ? "" : sphere.getTitle());
        DcCheckBox checkBox = new DcCheckBox(sphere == null || sphere.isActive(), "active");
        DialogConstructor.constructDialog(() -> {
            Sphere savedSphere = Core.getInstance().sphereService()
                    .save(sphere, titleField.getValue(), checkBox.getValue());
            fillSphereTable();
            selectSphere(savedSphere.isActive() ? sphere : null);
        }, titleField, checkBox);
    }

    private void setPoint(Sphere sphere) {
        spherePointLabelMap.values().forEach(label -> label.setText(""));
        if (sphere != null)
            spherePointLabelMap.get(sphere).setText("* ");
    }

    private Node taskToNode(Task task, int blockWidth, Sphere sphere) {
        return WindowBuilder.getNode(TASK_PANE,
                new TaskPaneController(task, blockWidth, () -> {
                    fillSphereTable();
                    selectSphere(sphere);
                }));
    }
}
