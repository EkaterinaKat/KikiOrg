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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator.getInstance;


public class CourseViewController implements FxController {
    private CourseOfAction courseOfAction;
    private NoArgsKnob onCourseChangeKnob;
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
        hierarchyComponent = new ComponentBuilder().setSize(new Size(800, 1090))
                .getHierarchyComponent(Core.getInstance().structureHierarchyService(courseOfAction), true, false, getNodeLabelAdjuster());
        hierarchyPane.getChildren().clear();
        hierarchyPane.getChildren().add(hierarchyComponent.getNode());
    }

    private TwoArgKnob<HierarchyNode, Label> getNodeLabelAdjuster() {
        return (node, label) -> {
            TargetStatus status;
            if (node instanceof Target) {
                status = ((Target) node).getStatus();
            } else {
                status = ((TargetGroup) node).getStatus();
            }
            label.setStyle(Styler.getColorfullStyle(Styler.ThingToColor.TEXT, status.getColor()));
        };
    }
}
