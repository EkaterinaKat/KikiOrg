package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

class LedgerController implements FxController {
    private final FinanceOperationService service = Core.getInstance().financeOperationService();
    @FXML
    private Pane replenishmentPane;
    @FXML
    private Pane expensePane;
    @FXML
    private Pane transferPane;
    @FXML
    private Pane checkPane;
    @FXML
    private Pane historyTablePane;

    @FXML
    private void initialize() {
        CheckController checkController = new CheckController();
        checkPane.getChildren().add(OrganizerWindowCreator.getInstance().getCheckNode(checkController));

        HistoryTableController historyTableController =
                new HistoryTableController(service.getTodayOperations(), checkController::updateTable);
        historyTablePane.getChildren().add(OrganizerWindowCreator.getInstance().getHistoryTableNode(historyTableController));

        replenishmentPane.getChildren().add(OrganizerWindowCreator.getInstance().getReplenishmentNode(
                new ReplenishmentController(() -> {
                    checkController.updateTable();
                    historyTableController.setTableContent(service.getTodayOperations());
                })));

        expensePane.getChildren().add(OrganizerWindowCreator.getInstance().getExpensesNode(
                new ExpensesController(() -> {
                    checkController.updateTable();
                    historyTableController.setTableContent(service.getTodayOperations());
                })));

        transferPane.getChildren().add(OrganizerWindowCreator.getInstance().getTransferNode(
                new TransferController(() -> {
                    checkController.updateTable();
                    historyTableController.setTableContent(service.getTodayOperations());
                })));
    }
}
