package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.FIN_HISTORY_TABLE;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.FIN_SEARCH;


class HistoryController implements SectionController {
    private HistoryTableController tableController;
    @FXML
    private Pane searchPane;
    @FXML
    private Pane tablePane;

    @FXML
    private void initialize() {
        tableController = new HistoryTableController(
                Core.getInstance().financeOperationService()::getOperationsForLastMonth);
        tablePane.getChildren().add(WindowBuilder.getNode(FIN_HISTORY_TABLE, tableController));
        searchPane.getChildren().add(WindowBuilder.getNode(FIN_SEARCH,
                new SearchController(tableController::setTableContent)));
    }

    @Override
    public void update() {
        tableController.updateTableContent();
    }
}
