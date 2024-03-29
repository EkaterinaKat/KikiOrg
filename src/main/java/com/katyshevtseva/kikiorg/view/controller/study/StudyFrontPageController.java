package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.fx.FxUtils;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.shiftDate;
import static com.katyshevtseva.fx.FxUtils.getPeriod;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.MAKE_SUBJ_MARKS_DIALOG;

public class StudyFrontPageController implements SectionController {
    private final StudyTableService tableService = Core.getInstance().studyTableService();
    @FXML
    private GridPane tablePane;
    @FXML
    private Button makeMarksButton;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    @FXML
    private void initialize() {
        FxUtils.setDate(startDatePicker, shiftDate(new Date(), DateUtils.TimeUnit.DAY, -30));
        FxUtils.setDate(endDatePicker, new Date());
        startDatePicker.setOnAction(event -> updateSectionContent());
        endDatePicker.setOnAction(event -> updateSectionContent());

        makeMarksButton.setOnAction(event ->
                WindowBuilder.openDialog(MAKE_SUBJ_MARKS_DIALOG, new MakeMarksDialogController(this::updateSectionContent)));
    }

    @Override
    public void update() {
        updateSectionContent();
    }

    private void updateSectionContent() {
        List<List<ReportCell>> report = tableService.getReport(getPeriod(startDatePicker, endDatePicker));
        addContextMenu(report);
        ReportUtils.showReport(report, tablePane, false);
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
                Core.getInstance().circsService().delete(circsToEdit.getDate());
                updateSectionContent();
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
            Core.getInstance().circsService().save(comboBox.getValue(), date, commentField.getValue());
            updateSectionContent();
        }, comboBox, commentField);
    }


    private ContextMenu getContextMenu(MarkToEdit mark) {
        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> WindowBuilder.openDialog(MAKE_SUBJ_MARKS_DIALOG,
                new MakeMarksDialogController(this::updateSectionContent, mark)));

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            Core.getInstance().studyService().delete(mark);
            updateSectionContent();
        });

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(editItem, deleteItem);
        return contextMenu;
    }
}
