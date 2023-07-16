package com.katyshevtseva.kikiorg.view.controller.dtt;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
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
    private final DatelessTask task;
    private final int blockWidth;
    private final NoArgsKnob contentUpdateKnob;
    @FXML
    private Label titleLabel;
    @FXML
    private Label datesLabel;
    @FXML
    private HBox root;

    @FXML
    private void initialize() {
        tuneLabel(titleLabel, 18, task.getTitle());
        tuneLabel(datesLabel, 12, task.getDatesInfo());

        if (!task.getFake()) {
            root.setOnContextMenuRequested(event -> showContextMenu(event, root));
        }
    }

    private void showContextMenu(ContextMenuEvent event, Node node) {
        ContextMenu contextMenu = new ContextMenu();

        if (task.getCompletionDate() == null) {
            MenuItem doneItem = new MenuItem("Done");
            doneItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Done?", b -> {
                if (b) {
                    Core.getInstance().dttTaskService().done(task);
                    contentUpdateKnob.execute();
                }
            }));
            contextMenu.getItems().add(doneItem);
        }

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> {
            DcTextArea titleField = new DcTextArea(true, task.getTitle());
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().dttTaskService().edit(task, titleField.getValue());
                contentUpdateKnob.execute();
            }, titleField);
        });
        contextMenu.getItems().add(editItem);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().dttTaskService().delete(task);
                contentUpdateKnob.execute();
            }
        }));
        contextMenu.getItems().add(deleteItem);

        contextMenu.show(node, event.getScreenX(), event.getScreenY());
    }

    private void tuneLabel(Label label, int textSize, String text) {
        label.setMaxWidth(blockWidth - 40);
        label.setWrapText(true);
        label.setText(text);
        label.setStyle(Styler.getTextSizeStyle(textSize));
    }
}
