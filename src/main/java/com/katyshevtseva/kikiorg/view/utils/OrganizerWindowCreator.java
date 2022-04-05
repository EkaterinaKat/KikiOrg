package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.fx.Size;
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
                .setSize(WINDOW_HEIGHT, WINDOW_WIDTH)
                .setTitle(CoreConstants.APP_NAME).
                setOnWindowCloseEventHandler(event -> System.exit(0)).showWindow();
    }

    public Node getPaginationPaneNode(FxController controller) {
        return new WindowBuilder(FXML_LOCATION + "pagination/pagination_pane.fxml").setController(controller).getNode();
    }

    /* ----------------------------------  Структура ---------------------------------------------- */

    public Node getMainStructureNode(FxController controller) {
        return new WindowBuilder(STRUCTURE_FXML_LOCATION + "main_structure.fxml").setController(controller).getNode();
    }

    public Node getStructureBoardNode(FxController controller) {
        return new WindowBuilder(STRUCTURE_FXML_LOCATION + "board.fxml").setController(controller).getNode();
    }

    public Node getCoursesNode(FxController controller) {
        return new WindowBuilder(STRUCTURE_FXML_LOCATION + "courses.fxml").setController(controller).getNode();
    }

    public void openCourseEditDialog(FxController controller) {
        new WindowBuilder(STRUCTURE_FXML_LOCATION + "course_dialog.fxml").setSize(new Size(560, 500))
                .setController(controller).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openCourseViewDialog(FxController controller) {
        new WindowBuilder(STRUCTURE_FXML_LOCATION + "course.fxml")
                .setSize(WINDOW_HEIGHT, WINDOW_WIDTH)
                .setController(controller).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public Node getTargetPaneNode(FxController controller) {
        return new WindowBuilder(STRUCTURE_FXML_LOCATION + "target_pane.fxml").setController(controller).getNode();
    }

    /* ---------------------------------- Работа ---------------------------------------------- */

    public Node getMainWorkNode(FxController controller) {
        return new WindowBuilder(WORK_FXML_LOCATION + "main_work.fxml").setController(controller).getNode();
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
                setController(controller).setHeight(500).setWidth(480).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openTaskDialog(FxController controller) {
        new WindowBuilder(TRACKER_FXML_LOCATION + "task_dialog.fxml").
                setController(controller).setHeight(500).setWidth(480).setTitle(CoreConstants.APP_NAME).showWindow();
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
                setController(controller).setHeight(700).setWidth(800).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openOutfitDialog(FxController controller) {
        new WindowBuilder(WARDROBE_FXML_LOCATION + "outfit_dialog.fxml").
                setController(controller).setHeight(1000).setWidth(1200).setTitle(CoreConstants.APP_NAME).showWindow();
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

    public Node getHabitsAnalysisNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "analysis.fxml").setController(controller).getNode();
    }

    public Node getHabitsCriterionNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "criterion.fxml").setController(controller).getNode();
    }

    public Node getHabitsHistoryNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "history.fxml").setController(controller).getNode();
    }

    public void openHabitEditDialog(FxController controller) {
        new WindowBuilder(HABITS_FXML_LOCATION + "habit_dialog.fxml").
                setController(controller).setHeight(BIG_DIALOG_HEIGHT).
                setWidth(BIG_DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
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
                .setController(controller).setHeight(WINDOW_HEIGHT).
                setWidth(WINDOW_WIDTH).setTitle("Finance report").showWindow();
    }

    public Node getFinanceAdminNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "admin.fxml").setController(controller).getNode();
    }

    public Node getLedgerNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "ledger.fxml").setController(controller).getNode();
    }

    public void openItemSelectDialog(FxController controller) {
        new WindowBuilder(FINANCE_FXML_LOCATION + "item_select_dialog.fxml").
                setController(controller).setHeight(ITEM_SELECT_DIALOG_HEIGHT).
                setWidth(ITEM_SELECT_DIALOG_WIDTH).setTitle("Select item").showWindow();
    }
}
