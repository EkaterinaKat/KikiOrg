package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.BlockGridController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.lang.Nullable;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.FxUtils.getPaneWithWidth;
import static com.katyshevtseva.fx.Styler.ThingToColor.BACKGROUND;
import static com.katyshevtseva.fx.Styler.getColorfullStyle;
import static com.katyshevtseva.fx.Styler.setHoverStyle;

public class GoalsController implements SectionController {
    private static final Size GRID_SIZE = new Size(700, 900);
    private static final int BLOCK_WIDTH = 250;
    private BlockGridController<Goal> goalBlockGridController;
    @FXML
    private Pane goalsPane;
    @FXML
    private Button newGoalButton;

    @FXML
    private void initialize() {
        newGoalButton.setOnAction(event -> openGoalEditDialog(null));
        adjustGoalGridController();
        updateContent();
    }

    private void openGoalEditDialog(@Nullable Goal goal) {
        DcTextField titleField = new DcTextField(true, goal == null ? "" : goal.getTitle());
        DcTextArea descField = new DcTextArea(false, goal == null ? "" : goal.getDescription());
        DialogConstructor.constructDialog(() -> {
            Core.getInstance().goalService().save(goal, titleField.getValue(), descField.getValue());
            updateContent();
        }, titleField, descField);
    }

    private void updateContent() {
        goalBlockGridController.setContent(Core.getInstance().goalService().getAll());
    }

    private void adjustGoalGridController() {
        ComponentBuilder.Component<BlockGridController<Goal>> goalGridComponent =
                new ComponentBuilder().setSize(GRID_SIZE).getBlockGridComponent(BLOCK_WIDTH,
                        null, this::getContextMenu, this::getGoalNode);
        goalsPane.getChildren().add(goalGridComponent.getNode());
        goalBlockGridController = goalGridComponent.getController();
    }

    private Node getGoalNode(Goal goal, int width) {
        Label titleLabel = new Label(goal.getTitle());
        FxUtils.setWidth(titleLabel, width);
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.BASELINE_CENTER);
        titleLabel.setStyle(Styler.getTextSizeStyle(20));

        Label descLabel = new Label(goal.getDescription());
        FxUtils.setWidth(descLabel, width);
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.BASELINE_CENTER);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                getPaneWithHeight(10),
                titleLabel,
                getPaneWithHeight(10),
                descLabel,
                getPaneWithHeight(10));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(getPaneWithWidth(10), vBox, getPaneWithWidth(10));
        hBox.setStyle(Styler.getBlackBorderStyle());
        setHoverStyle(hBox, getColorfullStyle(BACKGROUND, "#70FFD2"));
        return hBox;
    }

    private ContextMenu getContextMenu(Goal goal) {
        ContextMenu menu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event1 -> openGoalEditDialog(goal));
        menu.getItems().add(editItem);

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                Core.getInstance().goalService().delete(goal);
                updateContent();
            }
        }));
        menu.getItems().add(deleteItem);

        return menu;
    }
}
