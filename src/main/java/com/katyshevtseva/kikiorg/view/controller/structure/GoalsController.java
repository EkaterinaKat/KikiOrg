package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.PageableBlockListController;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.StructureService;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Goal;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.GOAL_PANE;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

public class GoalsController implements FxController {
    private static final Size GOAL_LIST_SIZE = new Size(800, 350);
    private Map<Activity, Label> activityPointLabelMap;
    private PageableBlockListController<Goal> todoListController;
    private PageableBlockListController<Goal> doneListController;
    private final StructureService service = Core.getInstance().structureService();
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane todoPane;
    @FXML
    private Pane donePane;
    @FXML
    private Button newGoalButton;

    @FXML
    private void initialize() {
        fillActivityTable();
        adjustGoalLists();
    }

    private void adjustGoalLists() {
        Component<PageableBlockListController<Goal>> todoComponent =
                new ComponentBuilder().setSize(GOAL_LIST_SIZE).getPageableBlockListComponent();
        Component<PageableBlockListController<Goal>> doneComponent =
                new ComponentBuilder().setSize(GOAL_LIST_SIZE).getPageableBlockListComponent();

        todoPane.getChildren().add(todoComponent.getNode());
        donePane.getChildren().add(doneComponent.getNode());

        todoListController = todoComponent.getController();
        doneListController = doneComponent.getController();
    }

    private void fillActivityTable() {
        gridPane.getChildren().clear();
        List<Activity> activities = Core.getInstance().structureService().getActivitiesForGoalsSection();
        activityPointLabelMap = new HashMap<>();
        int rowIndex = 0;
        for (Activity activity : activities) {
            Label label = new Label(activity.getTitle());
            label.setWrapText(true);
            label.setStyle(Styler.getTextSizeStyle(18));
            Label point = new Label();
            activityPointLabelMap.put(activity, point);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                showGoals(activity);
            });
            gridPane.add(point, 1, rowIndex);
            gridPane.add(label, 2, rowIndex);
            rowIndex++;
        }
    }

    private void showGoals(Activity activity) {
        setPoint(activity);
        adjustNewGoalButton(activity);

        todoListController.show(pageNum -> service.getTodoGoals(activity, pageNum),//
                ((goal, integer) -> goalToNode(goal, integer, activity)));
        doneListController.show(pageNum -> service.getDoneGoals(activity, pageNum),
                ((goal, integer) -> goalToNode(goal, integer, activity)));
    }

    private void adjustNewGoalButton(Activity activity) {
        newGoalButton.setVisible(true);
        newGoalButton.setOnAction(event -> {
            DcTextArea titleField = new DcTextArea(true, "");
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().createGoal(activity, titleField.getValue());
                showGoals(activity);
            }, titleField);
        });
    }

    private void setPoint(Activity activity) {
        activityPointLabelMap.values().forEach(label -> label.setText(""));
        activityPointLabelMap.get(activity).setText("* ");
    }

    private Node goalToNode(Goal goal, int blockWidth, Activity activity) {
        return windowCreator().getNode(GOAL_PANE, new GoalPaneController(goal, blockWidth, () -> showGoals(activity)));
    }
}
