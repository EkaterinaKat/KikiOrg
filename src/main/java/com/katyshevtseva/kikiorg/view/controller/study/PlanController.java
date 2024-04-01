package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.dialogconstructor.*;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Plan;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.STUDY_PLAN_LIST;

public class PlanController implements SectionController {
    private PlanListController listController;
    @FXML
    private Button addPlanButton;
    @FXML
    private VBox planBox;

    @FXML
    private void initialize() {
        addPlanButton.setOnAction(event -> openSeriesEditDialog(null));
        listController = new PlanListController(this::openSeriesEditDialog, this::updateContent, 800);
        planBox.getChildren().add(WindowBuilder.getNode(STUDY_PLAN_LIST, listController));
    }

    @Override
    public void update() {
        updateContent();
    }

    private void openSeriesEditDialog(Plan plan) {
        boolean newPlan = plan == null;

        DcComboBox<Subject> subjectCB = new DcComboBox<>(true, newPlan ? null : plan.getSubject(),
                Core.getInstance().studyService.getActiveSubjects());
        DcDatePicker startDp = new DcDatePicker(true, newPlan ? null : plan.getStart().getValue());
        DcDatePicker endDp = new DcDatePicker(true, newPlan ? null : plan.getEnd().getValue());
        DcNumField minDayField = new DcNumField(true, newPlan ? null : plan.getMinDays(), "Min days");
        DcTimeField timeField = new DcTimeField(true, newPlan ? null : plan.getMinMinutesADay());

        DialogConstructor.constructDialog(() -> {
            Core.getInstance().planService.save(plan, subjectCB.getValue(), startDp.getValue(), endDp.getValue(),
                    minDayField.getValue(), timeField.getValue());
            updateContent();
        }, subjectCB, startDp, endDp, minDayField, timeField);
    }

    private void updateContent() {
        listController.show(Core.getInstance().planService.getAllPlans());
    }
}
