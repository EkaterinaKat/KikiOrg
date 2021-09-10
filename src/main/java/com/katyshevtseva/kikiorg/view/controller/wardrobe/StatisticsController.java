package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeStatisticsService;
import com.katyshevtseva.kikiorg.view.utils.ReportUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class StatisticsController implements FxController {
    private final WardrobeStatisticsService service = Core.getInstance().wardrobeStatisticsService();
    @FXML
    private Label label;
    @FXML
    private GridPane gridPane;

    @FXML
    private void initialize() {
        label.setText(service.getPieceStatistics());
        ReportUtils.showReport(service.getOutfitStatistics(), gridPane);
    }
}
