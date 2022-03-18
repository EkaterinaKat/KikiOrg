package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.HierarchyController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.general.TwoArgKnob;
import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.Target;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.TargetGroup;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.TargetStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator.getInstance;


public class CourseViewController implements FxController {
    private CourseOfAction courseOfAction;
    private final NoArgsKnob onCourseChangeKnob;
    private ComponentBuilder.Component<HierarchyController> hierarchyComponent;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Pane hierarchyPane;
    @FXML
    private Button editButton;
    @FXML
    private Button addGroupButton;
    @FXML
    private Button addTargetButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button deleteButton;

    public CourseViewController(CourseOfAction courseOfAction, NoArgsKnob onCourseChangeKnob) {
        this.courseOfAction = courseOfAction;
        this.onCourseChangeKnob = onCourseChangeKnob;
    }

    @FXML
    private void initialize() {
        titleLabel.setStyle(Styler.getTextSizeStyle(20));
        fillCourseData();
        fillHierarchyPane();

        editButton.setOnAction(event -> getInstance().openCourseEditDialog(new CourseEditController(courseOfAction, (course) -> {
            courseOfAction = course;
            fillCourseData();
            onCourseChangeKnob.execute();
        })));

        addGroupButton.setOnAction(event -> new StandardDialogBuilder().openTextFieldAndTextAreaDialog((s, s2) -> {
            Core.getInstance().targetGroupService().create(courseOfAction, s, s2);
            hierarchyComponent.getController().fillSchema();
        }));

        addTargetButton.setOnAction(event -> new StandardDialogBuilder().openTextFieldAndTextAreaDialog((s, s2) -> {
            Core.getInstance().targetService().create(courseOfAction, s, s2);
            hierarchyComponent.getController().fillSchema();
        }));

        historyButton.setOnAction(event -> new StandardDialogBuilder().openHistoryDialog(courseOfAction));

        deleteButton.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Delete?", answer -> {
            if (answer) {
                Core.getInstance().courseOfActionService().delete(courseOfAction);
                onCourseChangeKnob.execute();
                FxUtils.closeWindowThatContains(titleLabel);
            }
        }));
    }

    private void fillCourseData() {
        titleLabel.setText(courseOfAction.getTitle());
        descLabel.setText(courseOfAction.getFullDesc());
    }

    private void fillHierarchyPane() {
        courseOfAction = Core.getInstance().courseOfActionService().getCourseWithLastChanges(courseOfAction);
        hierarchyComponent = new ComponentBuilder().setSize(new Size(800, 1090))
                .getHierarchyComponent(Core.getInstance().structureHierarchyService(courseOfAction), true, false, getNodeLabelAdjuster());
        hierarchyPane.getChildren().clear();
        hierarchyPane.getChildren().add(hierarchyComponent.getNode());
    }

    private TwoArgKnob<HierarchyNode, Label> getNodeLabelAdjuster() {
        return (node, label) -> {
            // set color
            TargetStatus status;
            if (node instanceof Target) {
                status = ((Target) node).getStatus();
            } else {
                status = ((TargetGroup) node).getStatus();
            }
            label.setStyle(Styler.getColorfullStyle(Styler.ThingToColor.TEXT, status.getColor()));

            //set context menu
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().add(getEditMenuItem(node));
            contextMenu.getItems().addAll(getStatusSpecificMenuItems(node));
            contextMenu.getItems().add(getDeleteMenuItem(node));
            contextMenu.getItems().add(getHistoryMenuItem(node));

            label.setOnContextMenuRequested(e -> contextMenu.show(label, e.getScreenX(), e.getScreenY()));
        };
    }

    private MenuItem getDeleteMenuItem(HierarchyNode node) {
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Delete?", answer -> {
            if (answer) {
                System.out.println("Delete " + node);
                //todo действие
                fillHierarchyPane();
            }
        }));
        return deleteItem;
    }

    private MenuItem getEditMenuItem(HierarchyNode node) {
        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> new StandardDialogBuilder().openTextFieldAndTextAreaDialog(node.getTitle(), node.getDescription(),
                ((s, s2) -> {
                    //todo действие
                    System.out.println("Edit " + node);
                    fillHierarchyPane();
                })));
        return editItem;
    }

    private MenuItem getHistoryMenuItem(HierarchyNode node) {
        MenuItem editItem = new MenuItem("History");
        if (node instanceof Target) {
            editItem.setOnAction(event -> new StandardDialogBuilder().openHistoryDialog((Target) node));
        } else if (node instanceof TargetGroup) {
            editItem.setOnAction(event -> new StandardDialogBuilder().openHistoryDialog((TargetGroup) node));
        }
        return editItem;
    }

    private List<MenuItem> getStatusSpecificMenuItems(HierarchyNode node) {
        switch (Core.getInstance().structureHierarchyNodeService().getNodeStatus(node)) {
            case NEW:
                return Arrays.asList(getStartMenuItem(node), getRejectMenuItem(node));
            case STARTED:
                return Arrays.asList(getDoneMenuItem(node), getRejectMenuItem(node));
            case DONE:
            case REJECTED:
                return Collections.singletonList(getStartMenuItem(node));
        }
        throw new RuntimeException();
    }

    private MenuItem getStartMenuItem(HierarchyNode node) {
        MenuItem startItem = new MenuItem("Start");
        startItem.setOnAction(event -> {
            //todo
            System.out.println("Start " + node);
            fillHierarchyPane();
        });
        return startItem;
    }

    private MenuItem getRejectMenuItem(HierarchyNode node) {
        MenuItem rejectItem = new MenuItem("Reject");
        rejectItem.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Reject?", answer -> {
            if (answer) {
                //todo действие
                System.out.println("Reject " + node);
                fillHierarchyPane();
            }
        }));
        return rejectItem;
    }

    private MenuItem getDoneMenuItem(HierarchyNode node) {
        MenuItem doneItem = new MenuItem("Done");
        doneItem.setOnAction(event -> new StandardDialogBuilder().openQuestionDialog("Done?", answer -> {
            if (answer) {
                //todo действие
                System.out.println("Done " + node);
                fillHierarchyPane();
            }
        }));
        return doneItem;
    }
}
