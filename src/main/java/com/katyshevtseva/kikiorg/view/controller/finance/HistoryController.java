package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;


class HistoryController implements FxController {
    @FXML
    private Pane searchPane;
    @FXML
    private Pane tablePane;

    @FXML
    private void initialize() {
        HistoryTableController tableController = new HistoryTableController(
                Core.getInstance().financeOperationService().getOperationsForLastMonth());
        tablePane.getChildren().add(OrganizerWindowCreator.getInstance().getHistoryTableNode(tableController));
        searchPane.getChildren().add(OrganizerWindowCreator.getInstance().getSearchNode(
                new SearchController(tableController::setTableContent)));
    }
}
