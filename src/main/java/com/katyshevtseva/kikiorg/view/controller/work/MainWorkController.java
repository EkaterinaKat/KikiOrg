package com.katyshevtseva.kikiorg.view.controller.work;

import com.katyshevtseva.date.DateCorrector;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.work.WorkArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.Date;

import static com.katyshevtseva.fx.FxUtils.*;

public class MainWorkController implements FxController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<WorkArea> areaComboBox;
    @FXML
    private TextField minutesTextField;
    @FXML
    private Button okButton;
    @FXML
    private GridPane reportTable;

    @FXML
    private void initialize() {
        associateButtonWithControls(okButton, datePicker, areaComboBox, minutesTextField);
        disableNonNumericChars(minutesTextField);
        okButton.setOnAction(event -> okButtonListener());
        datePicker.setValue(new Date(DateCorrector.getProperDate().getTime()).toLocalDate());
        setComboBoxItems(areaComboBox, WorkArea.values());
        updateTable();
    }

    private void okButtonListener() {
        Core.getInstance().workService().saveOrRewriteWorkLog(
                areaComboBox.getValue(), Integer.parseInt(minutesTextField.getText()), Date.valueOf(datePicker.getValue()));
        minutesTextField.clear();
        updateTable();
    }

    private void updateTable() {
//        reportTable.getChildren().clear();
//        List<List<ReportCell>> report = Core.getInstance().habitsReportService().getReport(selectedHabits,
//                OrgUtils.getPeriodByDp(startDatePicker, endDatePicker));
//
//        for (int i = 0; i < report.size(); i++) {
//            List<ReportCell> reportLine = report.get(i);
//            for (int j = 0; j < reportLine.size(); j++) {
//                ReportCell reportCell = reportLine.get(j);
//                fillCell(reportCell, i, j);
//            }
//        }
    }
}
