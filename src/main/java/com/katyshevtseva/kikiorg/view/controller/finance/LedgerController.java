package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

class LedgerController implements FxController {
    @FXML
    private Pane replenishmentPane;
    @FXML
    private Pane expensePane;
    @FXML
    private Pane transferPane;
    @FXML
    private Pane checkPane;

    @FXML
    private void initialize() {
        CheckController checkController = new CheckController();
        checkPane.getChildren().add(OrganizerWindowCreator.getInstance().getCheckNode(checkController));
        replenishmentPane.getChildren().add(OrganizerWindowCreator.getInstance().getReplenishmentNode(
                new ReplenishmentController(checkController::updateTable)));
        expensePane.getChildren().add(OrganizerWindowCreator.getInstance().getExpensesNode(
                new ExpensesController(checkController::updateTable)));
        transferPane.getChildren().add(OrganizerWindowCreator.getInstance().getTransferNode(
                new TransferController(checkController::updateTable)));

    }
}
