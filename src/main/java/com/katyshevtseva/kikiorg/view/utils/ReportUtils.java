package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.kikiorg.core.report.ReportCell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ReportUtils {

    public static void showReport(List<List<ReportCell>> report, GridPane table) {
        table.getChildren().clear();

        for (int i = 0; i < report.size(); i++) {
            List<ReportCell> reportLine = report.get(i);
            for (int j = 0; j < reportLine.size(); j++) {
                ReportCell reportCell = reportLine.get(j);
                fillCell(table, reportCell, i, j);
            }
        }
    }

    private static void fillCell(GridPane table, ReportCell reportCell, int row, int column) {
        StackPane cell = new StackPane();
        cell.setStyle(" -fx-background-color: " + reportCell.getColor() + "; ");
        Label label = new Label(reportCell.getText());
        label.setTooltip(new Tooltip(reportCell.getText()));

        if (reportCell.isColumnHead()) {
            VBox vBox = new VBox(label);
            vBox.setRotate(90);
            vBox.setPadding(new Insets(5, 5, 5, 5));
            cell.setPrefHeight(170);
            cell.setPrefWidth(50);
            cell.getChildren().add(new Group(vBox));
        } else {
            cell.setPrefHeight(30);
            cell.setPrefWidth(50);
            cell.getChildren().add(label);
        }

        if (reportCell.getWidth() != null) {
            cell.setPrefWidth(reportCell.getWidth());
        }

        HBox.setMargin(label, new Insets(8));
        StackPane.setAlignment(label, Pos.CENTER);
        table.add(cell, column, row);
    }
}
