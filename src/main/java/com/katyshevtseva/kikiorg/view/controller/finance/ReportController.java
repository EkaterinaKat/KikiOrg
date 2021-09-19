package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

class ReportController implements FxController {
    @FXML
    private VBox accountPane;
    @FXML
    private VBox periodPane;
    @FXML
    private Button showButton;
    @FXML
    private Label replenishmentLabel;
    @FXML
    private GridPane replenishmentTable;
    @FXML
    private PieChart replenishmentChart;
    @FXML
    private Label expenseLabel;
    @FXML
    private GridPane expenseTable;
    @FXML
    private PieChart expenseChart;
    @FXML
    private Label resultLabel;
}
