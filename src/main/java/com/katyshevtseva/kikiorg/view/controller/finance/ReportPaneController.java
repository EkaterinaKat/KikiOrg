package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.PieChartUtil;
import com.katyshevtseva.fx.ReportUtils;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.kikiorg.core.sections.finance.report.SpodReport;
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
        pieChart.setStyle(Styler.getTextSizeStyle(10));
    }

    void showReport(SpodReport spodReport) {
        label.setText(spodReport.getTitle());
        PieChartUtil.tunePieChart(pieChart, spodReport.getPieChartData());
        //тут в showReport в параметро rotateHeadlines вообще без разницы что передавать true или false
        //так как при формировании отчета вообще не добавляется строк помеченых как заголовок
        ReportUtils.showReport(spodReport.getTable(), table, false);
    }
}
