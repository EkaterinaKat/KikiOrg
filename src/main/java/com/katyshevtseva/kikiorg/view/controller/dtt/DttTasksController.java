package com.katyshevtseva.kikiorg.view.controller.dtt;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.PageableBlockListController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcCheckBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.dtt.DttTaskService;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.Sphere;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.DTT_TASK_PANE;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

public class DttTasksController implements SectionController {
    private static final Size TASK_LIST_SIZE = new Size(800, 350);
    private Map<Sphere, Label> spherePointLabelMap;
    private PageableBlockListController<DatelessTask> todoListController;
    private PageableBlockListController<DatelessTask> doneListController;
    private final DttTaskService service = Core.getInstance().dttTaskService();
    private Sphere selectedSphere;
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane todoPane;
    @FXML
    private Pane donePane;
    @FXML
    private Button newTaskButton;
    @FXML
    private Button newSphereButton;

    @FXML
    private void initialize() {
        adjustCreationButtons();
    }

    @Override
    public void update() {
        fillSphereTable();
        adjustTasksLists();
    }

    private void adjustTasksLists() {
        ComponentBuilder.Component<PageableBlockListController<DatelessTask>> todoComponent =
                new ComponentBuilder().setSize(TASK_LIST_SIZE).getPageableBlockListComponent();
        ComponentBuilder.Component<PageableBlockListController<DatelessTask>> doneComponent =
                new ComponentBuilder().setSize(TASK_LIST_SIZE).getPageableBlockListComponent();

        todoPane.getChildren().add(todoComponent.getNode());
        donePane.getChildren().add(doneComponent.getNode());

        todoListController = todoComponent.getController();
        doneListController = doneComponent.getController();
    }

    private void fillSphereTable() {
        gridPane.getChildren().clear();
        List<Sphere> spheres = Core.getInstance().sphereService().getAll();
        spherePointLabelMap = new HashMap<>();
        int rowIndex = 0;
        for (Sphere sphere : spheres) {
            Label label = new Label(sphere.getTitle());
            label.setWrapText(true);
            label.setStyle(Styler.getTextSizeStyle(18));
            label.setContextMenu(getMenu(sphere));
            if (!sphere.isActive()) {
                label.setStyle(Styler.getColorfullStyle(Styler.ThingToColor.TEXT, Styler.StandardColor.GRAY));
            } else {
                Label point = new Label();
                spherePointLabelMap.put(sphere, point);
                label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> selectSphere(sphere));
                gridPane.add(point, 1, rowIndex);
            }
            gridPane.add(label, 2, rowIndex);
            rowIndex++;
        }
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
        newTaskButton.setVisible(sphere != null);

        if (sphere != null) {
            todoListController.show(pageNum -> service.getTodoTasks(sphere, pageNum),
                    ((task, integer) -> taskToNode(task, integer, sphere)));
            doneListController.show(pageNum -> service.getDoneTasks(sphere, pageNum),
                    ((task, integer) -> taskToNode(task, integer, sphere)));
        } else {
            todoListController.clear();
            doneListController.clear();
        }
    }

    private void adjustCreationButtons() {
        newTaskButton.setOnAction(event -> {
            DcTextArea titleField = new DcTextArea(true, "");
            DialogConstructor.constructDialog(() -> {
                service.createTask(selectedSphere, titleField.getValue());
                fillSphereTable();
                selectSphere(selectedSphere);
            }, titleField);
        });

        newSphereButton.setOnAction(event -> showSphereEditDialog(null));
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

    private Node taskToNode(DatelessTask task, int blockWidth, Sphere sphere) {
        return windowCreator().getNode(DTT_TASK_PANE,
                new TaskPaneController(task, blockWidth, () -> selectSphere(sphere)));
    }
}
