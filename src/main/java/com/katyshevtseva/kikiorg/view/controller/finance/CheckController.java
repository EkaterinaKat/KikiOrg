package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

class CheckController implements FxController {
    @FXML
    private Pane scatterPane;

    @FXML
    private void initialize() {
        scatterPane.getChildren().add(OrganizerWindowCreator.getInstance().getScatterCheckNode(new ScatterCheckController()));
    }
}
