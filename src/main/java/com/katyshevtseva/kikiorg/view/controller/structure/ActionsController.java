package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.PageableBlockListController;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.StructureService;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Action;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Activity;
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

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.ACTION_PANE;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

public class ActionsController implements SectionController {
    private static final Size ACTION_LIST_SIZE = new Size(800, 350);
    private Map<Activity, Label> activityPointLabelMap;
    private PageableBlockListController<Action> todoListController;
    private PageableBlockListController<Action> doneListController;
    private final StructureService service = Core.getInstance().structureService();
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane todoPane;
    @FXML
    private Pane donePane;
    @FXML
    private Button newActionButton;

    @FXML
    private void initialize() {
        fillActivityTable();
        adjustActionLists();
    }

    private void adjustActionLists() {
        Component<PageableBlockListController<Action>> todoComponent =
                new ComponentBuilder().setSize(ACTION_LIST_SIZE).getPageableBlockListComponent();
        Component<PageableBlockListController<Action>> doneComponent =
                new ComponentBuilder().setSize(ACTION_LIST_SIZE).getPageableBlockListComponent();

        todoPane.getChildren().add(todoComponent.getNode());
        donePane.getChildren().add(doneComponent.getNode());

        todoListController = todoComponent.getController();
        doneListController = doneComponent.getController();
    }

    private void fillActivityTable() {
        gridPane.getChildren().clear();
        List<Activity> activities = Core.getInstance().structureService().getActivitiesForActionsSection();
        activityPointLabelMap = new HashMap<>();
        int rowIndex = 0;
        for (Activity activity : activities) {
            Label label = new Label(activity.getTitle());
            label.setWrapText(true);
            label.setStyle(Styler.getTextSizeStyle(18));
            Label point = new Label();
            activityPointLabelMap.put(activity, point);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                showActions(activity);
            });
            gridPane.add(point, 1, rowIndex);
            gridPane.add(label, 2, rowIndex);
            rowIndex++;
        }
    }

    private void showActions(Activity activity) {
        setPoint(activity);
        adjustNewActionButton(activity);

        todoListController.show(pageNum -> service.getTodoActions(activity, pageNum),//
                ((action, integer) -> actionToNode(action, integer, activity)));
        doneListController.show(pageNum -> service.getDoneActions(activity, pageNum),
                ((action, integer) -> actionToNode(action, integer, activity)));
    }

    private void adjustNewActionButton(Activity activity) {
        newActionButton.setVisible(true);
        newActionButton.setOnAction(event -> {
            DcTextArea titleField = new DcTextArea(true, "");
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().structureService().createAction(activity, titleField.getValue());
                showActions(activity);
            }, titleField);
        });
    }

    private void setPoint(Activity activity) {
        activityPointLabelMap.values().forEach(label -> label.setText(""));
        activityPointLabelMap.get(activity).setText("* ");
    }

    private Node actionToNode(Action action, int blockWidth, Activity activity) {
        return windowCreator().getNode(ACTION_PANE, new ActionPaneController(action, blockWidth, () -> showActions(activity)));
    }
}
