package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.*;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

class LedgerController implements SectionController {
    private final FinanceOperationService service = Core.getInstance().financeOperationService();

    private HistoryTableController historyTableController;
    private CheckController checkController;
    private ExpenseController expenseController;
    private ReplenishmentController replenishmentController;
    private TransferController transferController;

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
    private Pane scatterPane;
    @FXML
    private Pane huddlePane;

    @FXML
    private void initialize() {
        checkController = new CheckController();
        checkPane.getChildren().add(windowCreator().getNode(FIN_CHECK, checkController));

        historyTableController =
                new HistoryTableController(service.getLastWeekOperations(), checkController::updateTable);
        historyTablePane.getChildren().add(windowCreator().getNode(FIN_HISTORY_TABLE, historyTableController));

        replenishmentController = new ReplenishmentController(() -> {
            checkController.updateTable();
            historyTableController.setTableContent(service.getLastWeekOperations());
        });
        replenishmentPane.getChildren().add(windowCreator().getNode(REPLENISHMENT, replenishmentController));

        expenseController = new ExpenseController(() -> {
            checkController.updateTable();
            historyTableController.setTableContent(service.getLastWeekOperations());
        });
        expensePane.getChildren().add(windowCreator().getNode(EXPENSE, expenseController));

        transferController = new TransferController(() -> {
            checkController.updateTable();
            historyTableController.setTableContent(service.getLastWeekOperations());
        });
        transferPane.getChildren().add(windowCreator().getNode(TRANSFER, transferController));

        scatterPane.getChildren().add(windowCreator().getNode(SCATTER_CHECK, new ScatterCheckController()));
        huddlePane.getChildren().add(windowCreator().getNode(HUDDLE_CHECK, new HuddleCheckController()));
    }

    @Override
    public void update() {
        historyTableController.setTableContent(service.getLastWeekOperations());
        checkController.updateTable();
        expenseController.adjustComboBoxes();
        replenishmentController.adjustComboBoxes();
        transferController.adjustComboBoxes();
    }
}
