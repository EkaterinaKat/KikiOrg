package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ReportPaneController implements WindowBuilder.FxController {
    @FXML
    private Label expenseLabel;
    @FXML
    private GridPane expenseTable;
    @FXML
    private PieChart expenseChart;
}
