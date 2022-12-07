package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.CoreConstants;
import javafx.scene.Node;

import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.*;


public class OrganizerWindowCreator {
    private static final OrganizerWindowCreator INSTANCE = new OrganizerWindowCreator();

    private OrganizerWindowCreator() {
    }

    public static OrganizerWindowCreator getInstance() {
        return INSTANCE;
    }

    public void openMainWindow(FxController controller) {
        new WindowBuilder(FXML_LOCATION + "main.fxml").
                setController(controller)
                .setSize(WINDOW_SIZE)
                .setTitle(CoreConstants.APP_NAME).
                setOnWindowCloseEventHandler(event -> System.exit(0)).showWindow();
    }

    /* ---------------------------------- Структура ---------------------------------------------- */

    public Node getMainStructureNode(FxController controller) {
        return new WindowBuilder(STRUCTURE_FXML_LOCATION + "main_structure.fxml").setController(controller).getNode();
    }

    public Node getStructureActivitiesNode(FxController controller) {
        return new WindowBuilder(STRUCTURE_FXML_LOCATION + "activities.fxml").setController(controller).getNode();
    }

    public Node getStructureParamstNode(FxController controller) {
        return new WindowBuilder(STRUCTURE_FXML_LOCATION + "params.fxml").setController(controller).getNode();
    }

    /* ---------------------------------- Трекер ---------------------------------------------- */

    public Node getMainTrackerNode(FxController controller) {
        return new WindowBuilder(TRACKER_FXML_LOCATION + "main_tracker.fxml").setController(controller).getNode();
    }

    public Node getBoardNode(FxController controller) {
        return new WindowBuilder(TRACKER_FXML_LOCATION + "board.fxml").setController(controller).getNode();
    }

    public Node getProjectsNode(FxController controller) {
        return new WindowBuilder(TRACKER_FXML_LOCATION + "project.fxml").setController(controller).getNode();
    }

    public void openProjectDialog(FxController controller) {
        new WindowBuilder(TRACKER_FXML_LOCATION + "project_dialog.fxml").
                setController(controller).setSize(500, 480).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public Node getTaskPaneNode(FxController controller) {
        return new WindowBuilder(TRACKER_FXML_LOCATION + "task_pane.fxml").setController(controller).getNode();
    }

    /* ---------------------------------- Гардероб ---------------------------------------------- */

    public Node getMainWardrobeNode(FxController controller) {
        return new WindowBuilder(WARDROBE_FXML_LOCATION + "main_wardrobe.fxml").setController(controller).getNode();
    }

    public Node getOutfitsNode(FxController controller) {
        return new WindowBuilder(WARDROBE_FXML_LOCATION + "outfit.fxml").setController(controller).getNode();
    }

    public Node getPiecesNode(FxController controller) {
        return new WindowBuilder(WARDROBE_FXML_LOCATION + "piece.fxml").setController(controller).getNode();
    }

    public Node getWardrobeStatisticsNode(FxController controller) {
        return new WindowBuilder(WARDROBE_FXML_LOCATION + "statistics.fxml").setController(controller).getNode();
    }

    public void openPieceEditDialog(FxController controller) {
        new WindowBuilder(WARDROBE_FXML_LOCATION + "piece_dialog.fxml").
                setController(controller).setSize(700, 800).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openOutfitDialog(FxController controller) {
        new WindowBuilder(WARDROBE_FXML_LOCATION + "outfit_dialog.fxml").
                setController(controller).setSize(1000, 1200).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public Node getOutfitGridNode(FxController controller) {
        return new WindowBuilder(WARDROBE_FXML_LOCATION + "outfit_grid.fxml").setController(controller).getNode();
    }

    /* ----------------------------------  Привычки ---------------------------------------------- */

    public Node getMainHabitsNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "main_habits.fxml").setController(controller).getNode();
    }

    public Node getHabitAdminNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "admin.fxml").setController(controller).getNode();
    }

    public Node getCheckListNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "check_list.fxml").setController(controller).getNode();
    }

    public Node getHabitsReportNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "report.fxml").setController(controller).getNode();
    }

    public Node getHabitsReportTableNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "report_table.fxml").setController(controller).getNode();
    }

    public Node getHabitsCriterionNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "criterion.fxml").setController(controller).getNode();
    }

    public void openHabitEditDialog(FxController controller) {
        new WindowBuilder(HABITS_FXML_LOCATION + "habit_dialog.fxml").
                setController(controller).setSize(BIG_DIALOG_SIZE)
                .setTitle(CoreConstants.APP_NAME).showWindow();
    }

    /* ----------------------------------  Финансы  ---------------------------------------------- */

    public Node getMainFinanceNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "main_finance.fxml").setController(controller).getNode();
    }

    public Node getSearchNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "search.fxml").setController(controller).getNode();
    }

    public Node getReplenishmentNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "replenishment.fxml").setController(controller).getNode();
    }

    public Node getExpensesNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "expense.fxml").setController(controller).getNode();
    }

    public Node getScatterCheckNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "check/scatter_check.fxml").setController(controller).getNode();
    }

    public Node getHuddleCheckNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "check/huddle_check.fxml").setController(controller).getNode();
    }

    public Node getHistoryTableNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "history_table.fxml").setController(controller).getNode();
    }

    public Node getCheckNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "check/check.fxml").setController(controller).getNode();
    }

    public Node getHistoryNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "history.fxml").setController(controller).getNode();
    }

    public Node getTransferNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "transfer.fxml").setController(controller).getNode();
    }

    public Node getReportPaneNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "report_pane.fxml").setController(controller).getNode();
    }

    public void openFinanceReportDialog(FxController controller) {
        new WindowBuilder(FINANCE_FXML_LOCATION + "full_report.fxml")
                .setController(controller).setSize(WINDOW_SIZE)
                .setTitle("Finance report").showWindow();
    }

    public Node getFinanceAdminNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "admin.fxml").setController(controller).getNode();
    }

    public Node getLedgerNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "ledger.fxml").setController(controller).getNode();
    }
}
