package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;

import static com.katyshevtseva.fx.Styler.StandardColor.GOLD;
import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;
import static com.katyshevtseva.fx.Styler.*;

@RequiredArgsConstructor
public class GoalPaneController implements FxController {
    private final Goal goal;
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
        tuneLabel(titleLabel, 18, goal.getTitle());
        tuneLabel(datesLabel, 12, goal.getDatesInfo());
        root.setOnContextMenuRequested(event -> showContextMenu(event, root));

        if (goal.isHighlighted()) {
            root.setStyle(getColorfullStyle(BACKGROUND, GOLD)
                    + getBorderWidth(3)
                    + getBorderRadius(10));
        }
    }

    private void showContextMenu(ContextMenuEvent event, Node node) {
        ContextMenu contextMenu = new ContextMenu();

        if (goal.getCompletionDate() == null) {
            MenuItem doneItem = new MenuItem("Done");
            doneItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Done?", b -> {
                if (b) {
                    Core.getInstance().structureService().done(goal);
                    contentUpdateKnob.execute();
                }
            }));
            contextMenu.getItems().add(doneItem);
        }

        MenuItem highlightItem = new MenuItem(goal.isHighlighted() ? "Remove highlightion" : "Highlight");
        highlightItem.setOnAction(event1 -> {
            Core.getInstance().structureService().highlight(goal);
            contentUpdateKnob.execute();
        });
        contextMenu.getItems().add(highlightItem);

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> {
            DcTextField titleField = new DcTextField(true, goal.getTitle());
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().edit(goal, titleField.getValue());
                contentUpdateKnob.execute();
            }, titleField);
        });
        contextMenu.getItems().add(editItem);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().structureService().delete(goal);
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
