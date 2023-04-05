package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Action;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ActionPaneController implements FxController {
    private final Action action;
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
        tuneLabel(titleLabel, 18, action.getTitle());
        tuneLabel(datesLabel, 12, action.getDatesInfo());
        root.setOnContextMenuRequested(event -> showContextMenu(event, root));
    }

    private void showContextMenu(ContextMenuEvent event, Node node) {
        ContextMenu contextMenu = new ContextMenu();

        if (action.getCompletionDate() == null) {
            MenuItem doneItem = new MenuItem("Done");
            doneItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Done?", b -> {
                if (b) {
                    Core.getInstance().actionService().done(action);
                    contentUpdateKnob.execute();
                }
            }));
            contextMenu.getItems().add(doneItem);
        }

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> {
            DcTextArea titleField = new DcTextArea(true, action.getTitle());
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().actionService().edit(action, titleField.getValue());
                contentUpdateKnob.execute();
            }, titleField);
        });
        contextMenu.getItems().add(editItem);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().actionService().delete(action);
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
