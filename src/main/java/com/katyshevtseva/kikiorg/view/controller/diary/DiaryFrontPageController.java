package com.katyshevtseva.kikiorg.view.controller.diary;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.ReportUtils;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.diary.DairyTableService.MarkToEdit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.util.Date;
import java.util.List;

import static com.katyshevtseva.date.DateUtils.shiftDate;
import static com.katyshevtseva.fx.FxUtils.getPeriod;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.MAKE_MARKS_DIALOG;

public class DiaryFrontPageController implements SectionController {
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
                WindowBuilder.openDialog(MAKE_MARKS_DIALOG, new MakeMarksDialogController(this::updateSectionContent)));
    }

    @Override
    public void update() {
        updateSectionContent();
    }

    private void updateSectionContent() {
        List<List<ReportCell>> report = Core.getInstance().dairyReportService().getReport(getPeriod(startDatePicker, endDatePicker));
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
        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> WindowBuilder.openDialog(MAKE_MARKS_DIALOG,
                new MakeMarksDialogController(this::updateSectionContent, (MarkToEdit) cell.getItem())));

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(editItem);
        return contextMenu;
    }
}
