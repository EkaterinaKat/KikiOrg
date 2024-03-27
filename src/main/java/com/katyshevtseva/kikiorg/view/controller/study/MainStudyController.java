package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.switchcontroller.AbstractSwitchController;
import com.katyshevtseva.fx.switchcontroller.Section;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.*;

public class MainStudyController extends AbstractSwitchController implements SectionController {
    @FXML
    private Pane mainPane;
    @FXML
    private HBox buttonBox;

    @FXML
    private void initialize() {
        init(getSections(), mainPane, this::placeButton);
    }

    private List<Section> getSections() {
        return Arrays.asList(
                new Section("Front page", new StudyFrontPageController(),
                        controller -> WindowBuilder.getNode(STUDY_FRONT_PAGE, controller)),
                new Section("Admin", new StudyAdminController(),
                        controller -> WindowBuilder.getNode(STUDY_ADMIN, controller)),
                new Section("Report", new StudyReportController(),
                        controller -> WindowBuilder.getNode(STUDY_REPORT, controller)));
    }

    private void placeButton(Button button) {
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), button);
    }
}
