package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator.windowCreator;

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
        checkPane.getChildren().add(windowCreator().getCheckNode(checkController));

        HistoryTableController historyTableController =
                new HistoryTableController(service.getLastWeekOperations(), checkController::updateTable);
        historyTablePane.getChildren().add(windowCreator().getHistoryTableNode(historyTableController));

        replenishmentPane.getChildren().add(windowCreator().getReplenishmentNode(
                new ReplenishmentController(() -> {
                    checkController.updateTable();
                    historyTableController.setTableContent(service.getLastWeekOperations());
                })));

        expensePane.getChildren().add(windowCreator().getExpensesNode(
                new ExpenseController(() -> {
                    checkController.updateTable();
                    historyTableController.setTableContent(service.getLastWeekOperations());
                })));

        transferPane.getChildren().add(windowCreator().getTransferNode(
                new TransferController(() -> {
                    checkController.updateTable();
                    historyTableController.setTableContent(service.getLastWeekOperations());
                })));
    }
}
