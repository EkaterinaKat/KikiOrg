package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.PieChartUtil;
import com.katyshevtseva.fx.ReportUtils;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReport;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ReportPaneController implements WindowBuilder.FxController {
    @FXML
    private Label label;
    @FXML
    private GridPane table;
    @FXML
    private PieChart pieChart;

    @FXML
    private void initialize() {
        pieChart.setLegendVisible(false);
    }

    void showReport(FinanceReport financeReport) {
        label.setText(financeReport.getTitle());
        PieChartUtil.tunePieChart(pieChart, financeReport.getPieChartData());
        ReportUtils.showReport(financeReport.getTable(), table);
    }
}
