package com.katyshevtseva.kikiorg.view.controller.structure;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.Page;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.structure.entity.CourseOfAction;
import com.katyshevtseva.kikiorg.core.sections.structure.enums.CourseOfActionStatus;
import com.katyshevtseva.kikiorg.view.controller.pagination.PaginationPaneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;
import static com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator.getInstance;

public class CoursesController implements FxController {
    private PaginationPaneController<CourseOfAction> paginationPaneController;
    @FXML
    private Button addCourseButton;
    @FXML
    private ComboBox<CourseOfActionStatus> statusComboBox;
    @FXML
    private VBox coursesPane;
    @FXML
    private VBox paginationPane;

    @FXML
    private void initialize() {
        setComboBoxItems(statusComboBox, CourseOfActionStatus.values(), CourseOfActionStatus.ACTIVE);
        tunePagination();
        addCourseButton.setOnAction(event ->
                getInstance().openCourseEditDialog(new CourseEditController(null, (course) -> paginationPaneController.loadPage())));
        statusComboBox.setOnAction(event -> paginationPaneController.loadPage());
    }

    private void tunePagination() {
        paginationPaneController = new PaginationPaneController<>(this::getCourseOfActionPage, this::setContent);
        paginationPane.getChildren().add(getInstance().getPaginationPaneNode(paginationPaneController));
    }

    private Page<CourseOfAction> getCourseOfActionPage(int pageNum) {
        return Core.getInstance().courseOfActionService().getCoursesByStatus(statusComboBox.getValue(), pageNum);
    }

    private void setContent(List<CourseOfAction> courseOfActionList) {
        coursesPane.getChildren().clear();

        for (CourseOfAction course : courseOfActionList) {
            coursesPane.getChildren().add(FxUtils.getPaneWithHeight(20));
            coursesPane.getChildren().add(getCourseLabel(course));
        }
        coursesPane.getChildren().add(FxUtils.getPaneWithHeight(20));
    }

    private Label getCourseLabel(CourseOfAction courseOfAction) {
        Label label = new Label(courseOfAction.getTitle());
        label.setStyle(Styler.getColorfullStyle(Styler.ThingToColor.TEXT, courseOfAction.getSphere().getColor()));
        label.setOnMouseClicked(event -> getInstance().openCourseViewDialog(new CourseViewController(courseOfAction, () -> paginationPaneController.loadPage())));
        return label;
    }
}
