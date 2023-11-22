package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceOperationService;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.*;

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
        checkPane.getChildren().add(WindowBuilder.getNode(FIN_CHECK, checkController));

        historyTableController =
                new HistoryTableController(service::getLastWeekOperations, checkController::updateTable);
        historyTablePane.getChildren().add(WindowBuilder.getNode(FIN_HISTORY_TABLE, historyTableController));

        replenishmentController = new ReplenishmentController(null, () -> {
            checkController.updateTable();
            historyTableController.updateTableContent();
        });
        replenishmentPane.getChildren().add(WindowBuilder.getNode(REPLENISHMENT, replenishmentController));

        expenseController = new ExpenseController(null, () -> {
            checkController.updateTable();
            historyTableController.updateTableContent();
        });
        expensePane.getChildren().add(WindowBuilder.getNode(EXPENSE, expenseController));

        transferController = new TransferController(null, () -> {
            checkController.updateTable();
            historyTableController.updateTableContent();
        });
        transferPane.getChildren().add(WindowBuilder.getNode(TRANSFER, transferController));

        scatterPane.getChildren().add(WindowBuilder.getNode(SCATTER_CHECK, new ScatterCheckController()));
        huddlePane.getChildren().add(WindowBuilder.getNode(HUDDLE_CHECK, new HuddleCheckController()));
    }

    @Override
    public void update() {
        historyTableController.updateTableContent();
        checkController.updateTable();
        expenseController.adjustComboBoxes();
        replenishmentController.adjustComboBoxes();
        transferController.adjustComboBoxes();
    }
}
