package com.katyshevtseva.kikiorg.view.controller.dtt;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.dtt.DttTaskService;
import com.katyshevtseva.kikiorg.core.sections.dtt.TaskStatus;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaskPaneController implements WindowBuilder.FxController {
    private final DttTaskService service = Core.getInstance().dttTaskService();
    private final DatelessTask task;
    private final int blockWidth;
    private final NoArgsKnob contentUpdateKnob;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Label datesLabel;
    @FXML
    private HBox root;

    @FXML
    private void initialize() {
        int labelWidth = blockWidth - 40;
        FxUtils.tuneLabel(titleLabel, task.getTitle(), 18, labelWidth);
        FxUtils.tuneLabel(datesLabel, service.getHistory(task), 12, labelWidth);
        FxUtils.tuneLabel(descLabel, task.getDescription(), 15, labelWidth);
        FxUtils.setWidth(root, blockWidth);
        root.setOnContextMenuRequested(event -> showContextMenu(event, root));
    }

    private void showContextMenu(ContextMenuEvent event, Node node) {
        ContextMenu contextMenu = new ContextMenu();

        for (TaskStatus status : service.getStatusesProperToChangeTo(task.getStatus())) {
            MenuItem item = new MenuItem(status.toString());
            item.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Change status to " + status + "?",
                    b -> {
                        if (b) {
                            service.changeStatus(task, status);
                            contentUpdateKnob.execute();
                        }
                    }));
            contextMenu.getItems().add(item);
        }

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> {
            DcTextField titleField = new DcTextField(true, task.getTitle());
            DcTextArea descField = new DcTextArea(false, task.getDescription());
            DialogConstructor.constructDialog(() -> {
                service.edit(task, titleField.getValue(), descField.getValue());
                contentUpdateKnob.execute();
            }, titleField, descField);
        });
        contextMenu.getItems().add(editItem);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                service.delete(task);
                contentUpdateKnob.execute();
            }
        }));
        contextMenu.getItems().add(deleteItem);

        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }
}
