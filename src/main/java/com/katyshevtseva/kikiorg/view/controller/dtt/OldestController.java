package com.katyshevtseva.kikiorg.view.controller.dtt;

import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.dtt.entity.DatelessTask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.FxUtils.getPaneWithWidth;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.DTT_TASK_PANE;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

public class OldestController implements SectionController {
    private static final int NUM_OF_COLUMNS = 3;
    private static final int BLOCK_WIDTH = 300;
    @FXML
    private HBox hBox;

    @Override
    public void update() {
        setContent();
    }

    void setContent() {
        hBox.getChildren().clear();
        List<VBox> vBoxes = new ArrayList<>();
        hBox.getChildren().add(getPaneWithWidth(10));
        for (int i = 0; i < NUM_OF_COLUMNS; i++) {
            VBox vBox = new VBox();
            vBox.getChildren().add(getPaneWithHeight(10));
            vBoxes.add(vBox);
            hBox.getChildren().addAll(vBox, getPaneWithWidth(10));
        }
        hBox.getChildren().add(getPaneWithWidth(10));

        List<DatelessTask> tasks = Core.getInstance().dttTaskService().getOldestTasks();
        for (int i = 0; i < tasks.size(); i++) {
            int numOfBox = i % NUM_OF_COLUMNS;
            vBoxes.get(numOfBox).getChildren().addAll(taskToNode(tasks.get(i)), getPaneWithHeight(10));
        }
    }

    private Node taskToNode(DatelessTask task) {
        return windowCreator().getNode(DTT_TASK_PANE, new TaskPaneController(task, BLOCK_WIDTH, this::setContent));
    }
}