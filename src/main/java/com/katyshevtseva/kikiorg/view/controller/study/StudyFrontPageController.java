package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.fx.ReportUtils;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcComboBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.study.StudyTableService;
import com.katyshevtseva.kikiorg.core.sections.study.StudyTableService.CircsToEdit;
import com.katyshevtseva.kikiorg.core.sections.study.StudyTableService.MarkToEdit;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Circs;
import com.katyshevtseva.kikiorg.core.sections.study.enums.CircsType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.shiftDate;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.MAKE_SUBJ_MARKS_DIALOG;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.SMALL_MAKE_SUBJ_MARKS_DIALOG;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.STUDY_PLAN_LIST;

public class StudyFrontPageController implements SectionController {
    private final StudyTableService tableService = Core.getInstance().studyTableService;
    private PlanListController listController;
    private StartEndDateNode dateNode;
    @FXML
    private GridPane tablePane;
    @FXML
    private Button makeMarksButton;
    @FXML
    private Pane planPane;
    @FXML
    private Pane datePane;

    @FXML
    private void initialize() {
        dateNode = new StartEndDateNode(
                this::updateTableContent,
                shiftDate(new Date(), DateUtils.TimeUnit.DAY, -20),
                shiftDate(new Date(), DateUtils.TimeUnit.DAY, 10));
        datePane.getChildren().add(dateNode.getNode());

        makeMarksButton.setOnAction(event ->
                WindowBuilder.openDialog(MAKE_SUBJ_MARKS_DIALOG, new MakeMarksDialogController(this::updateAllContent)));

        listController = new PlanListController(null, this::updatePlanPane, 420);
        planPane.getChildren().add(WindowBuilder.getNode(STUDY_PLAN_LIST, listController));
    }

    @Override
    public void update() {
        updateAllContent();
    }

    private void updateAllContent() {
        updateTableContent();
        updatePlanPane();
    }

    private void updateTableContent() {
        List<List<ReportCell>> report = tableService.getReport(dateNode.getPeriod());
        addContextMenu(report);
        ReportUtils.showReport(report, tablePane, true);
    }

    private void updatePlanPane() {
        listController.show(Core.getInstance().planService.getActivePlans());
    }

    private void addContextMenu(List<List<ReportCell>> report) {
        for (List<ReportCell> list : report) {
            for (ReportCell cell : list) {
                if (cell.getItem() != null) {
                    cell.setContextMenu(getContextMenu(cell));
                }
            }
        }
    }

    private ContextMenu getContextMenu(ReportCell cell) {
        if (cell.getItem() instanceof MarkToEdit) {
            return getContextMenu((MarkToEdit) cell.getItem());
        }
        if (cell.getItem() instanceof CircsToEdit) {
            return getContextMenu((CircsToEdit) cell.getItem());
        }
        throw new RuntimeException();
    }

    private ContextMenu getContextMenu(CircsToEdit circsToEdit) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> openCircsEditDialog(circsToEdit.getCircs(), circsToEdit.getDate()));
        contextMenu.getItems().add(editItem);

        if (circsToEdit.getCircs() != null) {
            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(event -> {
                Core.getInstance().circsService.delete(circsToEdit.getDate());
                updateAllContent();
            });
            contextMenu.getItems().add(deleteItem);
        }

        return contextMenu;
    }

    private void openCircsEditDialog(Circs circs, Date date) {
        boolean newCircs = circs == null;
        DcComboBox<CircsType> comboBox = new DcComboBox<>(true, newCircs ? null : circs.getType(),
                Arrays.asList(CircsType.values()));
        DcTextArea commentField = new DcTextArea(false, newCircs ? "" : circs.getComment());

        DialogConstructor.constructDialog(() -> {
            Core.getInstance().circsService.save(comboBox.getValue(), date, commentField.getValue());
            updateAllContent();
        }, comboBox, commentField);
    }


    private ContextMenu getContextMenu(MarkToEdit mark) {
        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> WindowBuilder.openDialog(SMALL_MAKE_SUBJ_MARKS_DIALOG,
                new MakeMarksDialogController(this::updateAllContent, mark)));

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            Core.getInstance().studyService.delete(mark);
            updateAllContent();
        });

        MenuItem planItem = new MenuItem("Plan");
        planItem.setOnAction(event -> {
            Core.getInstance().pmService.makeMark(mark.getSubject(), mark.getDate());
            updateAllContent();
        });

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(editItem, deleteItem, planItem);
        return contextMenu;
    }
}
