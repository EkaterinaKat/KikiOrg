package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.FxUtils.getPaneWithWidth;

public class TopicalController implements FxController {
    private static final int NUM_OF_COLUMNS = 3;
    private static final int BLOCK_WIDTH = 300;
    @FXML
    private HBox hBox;

    @FXML
    private void initialize() {
        setContent();
    }

    private void setContent() {
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

        List<Goal> goals = Core.getInstance().structureService().getHighlightedGoals();
        for (int i = 0; i < goals.size(); i++) {
            int numOfBox = i % NUM_OF_COLUMNS;
            vBoxes.get(numOfBox).getChildren().addAll(goalToNode(goals.get(i)), getPaneWithHeight(10));
        }
    }

    private Node goalToNode(Goal goal) {
        return OrganizerWindowCreator.getInstance().getGoalPaneNode(new GoalPaneController(goal, BLOCK_WIDTH, this::setContent));
    }
}
