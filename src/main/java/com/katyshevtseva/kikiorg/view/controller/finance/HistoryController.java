package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator.windowCreator;


class HistoryController implements FxController {
    @FXML
    private Pane searchPane;
    @FXML
    private Pane tablePane;

    @FXML
    private void initialize() {
        HistoryTableController tableController = new HistoryTableController(
                Core.getInstance().financeOperationService().getOperationsForLastMonth());
        tablePane.getChildren().add(windowCreator().getHistoryTableNode(tableController));
        searchPane.getChildren().add(windowCreator().getSearchNode(
                new SearchController(tableController::setTableContent)));
    }
}
