package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeStatisticsService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatisticsController implements SectionController {
    private final WardrobeStatisticsService service = Core.getInstance().wardrobeStatisticsService;
    @FXML
    private Label label;

    @Override
    public void update() {
        label.setText(service.getPieceStatistics());
    }
}
