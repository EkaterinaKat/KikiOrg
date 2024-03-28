package com.katyshevtseva.kikiorg.view.controller.study;

import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.study.StudyChartService.Dot;
import com.katyshevtseva.kikiorg.core.sections.study.entity.Subject;
import com.katyshevtseva.kikiorg.core.sections.study.enums.ChartYValueType;
import com.katyshevtseva.kikiorg.core.sections.study.enums.Span;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;

public class StudyReportController implements SectionController {
    @FXML
    private ComboBox<Subject> subjectComboBox;
    @FXML
    private ComboBox<Span> spanComboBox;
    @FXML
    private Button showButton;
    @FXML
    private VBox chartPane;
    @FXML
    private Label detailLabel;
    @FXML
    private CheckBox adjustYCheckBox;
    @FXML
    private ComboBox<ChartYValueType> yValueTypeComboBox;

    @FXML
    private void initialize() {
        setComboBoxItems(subjectComboBox, Core.getInstance().studyService().getActiveSubjects());
        setComboBoxItems(spanComboBox, Span.values());
        setComboBoxItems(yValueTypeComboBox, ChartYValueType.values(), ChartYValueType.AVERAGE);
        associateButtonWithControls(showButton, subjectComboBox, spanComboBox, yValueTypeComboBox);
        showButton.setOnAction(event -> showChart());
    }

    @Override
    public void update() {
        initialize();
    }

    private void showChart() {
        List<Dot> dots = Core.getInstance().studyChartService()
                .getChart(subjectComboBox.getValue(), spanComboBox.getValue(), yValueTypeComboBox.getValue());
        int yUpperBound = adjustYCheckBox.isSelected() ? getMaxY(dots) : 10;

        NumberAxis numberAxis = new NumberAxis(0, yUpperBound, 1);
        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), numberAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        lineChart.getData().add(series);
        lineChart.setMinWidth(dots.size() * 40);

        for (Dot dot : dots) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(dot.getX(), dot.getY());
            series.getData().add(data);
            data.getNode().setOnMouseClicked(event -> dotClickListener(dot));
        }

        chartPane.getChildren().clear();
        chartPane.getChildren().add(lineChart);
    }

    private int getMaxY(List<Dot> dots) {
        int max = 0;
        for (Dot dot : dots) {
            int value = (int) Math.ceil(dot.getY());
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void dotClickListener(Dot dot) {
        detailLabel.setText(dot.getDetails());
    }
}
