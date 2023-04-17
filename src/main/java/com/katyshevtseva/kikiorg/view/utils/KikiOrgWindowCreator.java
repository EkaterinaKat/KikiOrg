package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.CoreConstants;
import javafx.scene.Node;

import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.*;

public class KikiOrgWindowCreator {
    private static final KikiOrgWindowCreator INSTANCE = new KikiOrgWindowCreator();

    private KikiOrgWindowCreator() {
    }

    public static KikiOrgWindowCreator windowCreator() {
        return INSTANCE;
    }

    public void openMainWindow(FxController controller) {
        new WindowBuilder(FXML_LOCATION + "main.fxml").
                setController(controller)
                .setSize(WINDOW_SIZE)
                .setTitle(CoreConstants.APP_NAME).
                setOnWindowCloseEventHandler(event -> System.exit(0)).showWindow();
    }

    public Node getNode(NodeInfo nodeInfo, FxController controller) {
        return new WindowBuilder(nodeInfo.getFullFileName()).setController(controller).getNode();
    }

    public void openDialog(DialogInfo dialogInfo, FxController controller) {
        new WindowBuilder(dialogInfo.getFullFileName())
                .setController(controller).setSize(dialogInfo.size)
                .setTitle(dialogInfo.title).showWindow();
    }

    public enum NodeInfo {
        //DATELESS TASK TRACKER
        MAIN_DTT(FXML_LOCATION, "section_main.fxml"),
        DTT_TASKS(DTT_FXML_LOCATION, "tasks.fxml"),
        DTT_TASK_PANE(DTT_FXML_LOCATION, "task_pane.fxml"),
        //STRUCTURE
        GOALS(STRUCTURE_FXML_LOCATION, "goals.fxml"),
        ACTIVITIES(STRUCTURE_FXML_LOCATION, "activities.fxml"),
        MAIN_STRUCTURE(FXML_LOCATION, "section_main.fxml"),
        //TRACKER
        TASK_PANE(TRACKER_FXML_LOCATION, "task_pane.fxml"),
        PROJECT(TRACKER_FXML_LOCATION, "project.fxml"),
        BOARD(TRACKER_FXML_LOCATION, "board.fxml"),
        MAIN_TRACKER(FXML_LOCATION, "section_main.fxml"),
        //WARDROBE
        OUTFIT(WARDROBE_FXML_LOCATION, "outfit.fxml"),
        OUTFIT_GRID(WARDROBE_FXML_LOCATION, "outfit_grid.fxml"),
        PIECE(WARDROBE_FXML_LOCATION, "piece.fxml"),
        MAIN_WARDROBE(FXML_LOCATION, "section_main.fxml"),
        WARDROBE_STATISTICS(WARDROBE_FXML_LOCATION, "statistics.fxml"),
        //FIN
        XXX(FIN_FXML_LOCATION, "xxx.fxml"),
        LEDGER(FIN_FXML_LOCATION, "ledger.fxml"),
        FIN_ADMIN(FIN_FXML_LOCATION, "admin.fxml"),
        REPORT_PANE(FIN_FXML_LOCATION, "report_pane.fxml"),
        TRANSFER(FIN_FXML_LOCATION, "transfer.fxml"),
        FIN_HISTORY(FIN_FXML_LOCATION, "history.fxml"),
        FIN_HIERARCHY(FIN_FXML_LOCATION, "hierarchy.fxml"),
        FIN_HISTORY_TABLE(FIN_FXML_LOCATION, "history_table.fxml"),
        FIN_CHECK(FIN_FXML_LOCATION, "check/check.fxml"),
        FIN_SEARCH(FIN_FXML_LOCATION, "search.fxml"),
        EXPENSE(FIN_FXML_LOCATION, "expense.fxml"),
        SCATTER_CHECK(FIN_FXML_LOCATION, "check/scatter_check.fxml"),
        REPLENISHMENT(FIN_FXML_LOCATION, "replenishment.fxml"),
        HUDDLE_CHECK(FIN_FXML_LOCATION, "check/huddle_check.fxml"),
        MAIN_FIN(FXML_LOCATION, "section_main.fxml"),
        //HABITS
        CRITERION(HABITS_FXML_LOCATION, "criterion.fxml"),
        HABIT_REPORT_TABLE(HABITS_FXML_LOCATION, "report_table.fxml"),
        HABIT_REPORT(HABITS_FXML_LOCATION, "report.fxml"),
        CHECK_LIST(HABITS_FXML_LOCATION, "check_list.fxml"),
        HABIT_ADMIN(HABITS_FXML_LOCATION, "admin.fxml"),
        MAIN_HABIT(FXML_LOCATION, "section_main.fxml");

        private final String location;
        private final String fileName;

        NodeInfo(String location, String fileName) {
            this.location = location;
            this.fileName = fileName;
        }

        public String getFullFileName() {
            return location + fileName;
        }
    }

    public enum DialogInfo {
        PROJECT(TRACKER_FXML_LOCATION, "project_dialog.fxml", new Size(500, 480), CoreConstants.APP_NAME),
        OUTFIT(WARDROBE_FXML_LOCATION, "outfit_dialog.fxml", new Size(1000, 1200), CoreConstants.APP_NAME),
        PIECE(WARDROBE_FXML_LOCATION, "piece_dialog.fxml", new Size(700, 800), CoreConstants.APP_NAME),
        HABIT(HABITS_FXML_LOCATION, "habit_dialog.fxml", BIG_DIALOG_SIZE, CoreConstants.APP_NAME),
        FIN_REPORT(FIN_FXML_LOCATION, "full_report.fxml", WINDOW_SIZE, "Finance report");

        private final String location;
        private final String fileName;
        private final Size size;
        private final String title;

        DialogInfo(String location, String fileName, Size size, String title) {
            this.location = location;
            this.fileName = fileName;
            this.size = size;
            this.title = title;
        }

        public String getFullFileName() {
            return location + fileName;
        }
    }
}
