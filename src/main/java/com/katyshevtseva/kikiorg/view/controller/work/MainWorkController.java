package com.katyshevtseva.kikiorg.view.controller.work;

import com.katyshevtseva.date.DateUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.report.ReportCell;
import com.katyshevtseva.kikiorg.core.sections.work.WorkArea;
import com.katyshevtseva.kikiorg.view.utils.ReportUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

import static com.katyshevtseva.date.DateCorrector.getProperDate;
import static com.katyshevtseva.fx.FxUtils.*;

public class MainWorkController implements FxController {
    @FXML
    private HBox autoCount;
    @FXML
    private AutoCountController autoCountController;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<WorkArea> areaComboBox;
    @FXML
    private TextField minutesTextField;
    @FXML
    private Button rewriteButton;
    @FXML
    private Button sumUpButton;
    @FXML
    private GridPane reportTable;

    @FXML
    private void initialize() {
        associateButtonWithControls(rewriteButton, datePicker, areaComboBox, minutesTextField);
        associateButtonWithControls(sumUpButton, datePicker, areaComboBox, minutesTextField);
        disableNonNumericChars(minutesTextField);
        rewriteButton.setOnAction(event -> rewriteButtonListener());
        sumUpButton.setOnAction(event -> sumUpButtonListener());
        datePicker.setValue(new java.sql.Date(getProperDate().getTime()).toLocalDate());
        setComboBoxItems(areaComboBox, WorkArea.values());
        updateTable();
        autoCountController.setTableUpdateKnob(this::updateTable);
    }

    private void rewriteButtonListener() {
        Core.getInstance().workService().saveOrRewriteExistingWorkLog(
                areaComboBox.getValue(), Integer.parseInt(minutesTextField.getText()), java.sql.Date.valueOf(datePicker.getValue()));
        minutesTextField.clear();
        updateTable();
    }

    private void sumUpButtonListener() {
        Core.getInstance().workService().saveOrSumWithExistingWorkLog(
                areaComboBox.getValue(), Integer.parseInt(minutesTextField.getText()), java.sql.Date.valueOf(datePicker.getValue()));
        minutesTextField.clear();
        updateTable();
    }

    private void updateTable() {
        List<List<ReportCell>> report = Core.getInstance().workReportService().getReport(DateUtils.getLastMonthPeriod());
        ReportUtils.showReport(report, reportTable);
    }

    interface Knob {
        void execute();
    }
}
