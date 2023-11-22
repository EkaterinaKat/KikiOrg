package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.CoreConstants;
import lombok.Getter;

import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.*;

public class KikiOrgWindowUtil {

    // Не можем использовать WindowBuilder.openDialog так как пока это приложение работает на SpringBoot
    // и нам обязательно нужно прописывать setOnWindowCloseEventHandler(event -> System.exit(0))
    public static void openMainWindow(FxController controller) {
        new WindowBuilder(FXML_LOCATION + "main.fxml").
                setController(controller)
                .setSize(WINDOW_SIZE)
                .setTitle(CoreConstants.APP_NAME).
                setOnWindowCloseEventHandler(event -> System.exit(0)).showWindow();
    }

    public enum OrgNodeInfo implements WindowBuilder.NodeInfo {
        //DIARY
        MAIN_DIARY(FXML_LOCATION, "section_main.fxml"),
        DIARY_ADMIN(DIARY_FXML_LOCATION, "admin.fxml"),
        DIARY_FRONT_PAGE(DIARY_FXML_LOCATION, "front_page.fxml"),
        //TRACKER
        MAIN_TRACKER(FXML_LOCATION, "section_main.fxml"),
        TASKS(TRACKER_FXML_LOCATION, "tasks.fxml"),
        TASK_PANE(TRACKER_FXML_LOCATION, "task_pane.fxml"),
        TRACKER_OLDEST(TRACKER_FXML_LOCATION, "oldest.fxml"),
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
        POTENTIAL_EXPENSE(FIN_FXML_LOCATION, "potential_expense.fxml"),
        SCATTER_CHECK(FIN_FXML_LOCATION, "check/scatter_check.fxml"),
        REPLENISHMENT(FIN_FXML_LOCATION, "replenishment.fxml"),
        HUDDLE_CHECK(FIN_FXML_LOCATION, "check/huddle_check.fxml"),
        MAIN_FIN(FXML_LOCATION, "section_main.fxml"),
        //HABITS
        HABIT_REPORT_TABLE(HABITS_FXML_LOCATION, "report_table.fxml"),
        HABIT_REPORT(HABITS_FXML_LOCATION, "report.fxml"),
        CHECK_LIST(HABITS_FXML_LOCATION, "check_list.fxml"),
        HABIT_ADMIN(HABITS_FXML_LOCATION, "admin.fxml"),
        MAIN_HABIT(FXML_LOCATION, "section_main.fxml");

        private final String location;
        private final String fileName;

        OrgNodeInfo(String location, String fileName) {
            this.location = location;
            this.fileName = fileName;
        }

        public String getFullFileName() {
            return location + fileName;
        }
    }

    public enum OrgDialogInfo implements WindowBuilder.DialogInfo {
        EXPENSE_EDIT(FIN_FXML_LOCATION, "expense.fxml", new Size(400, 1000), "Edit Expense"),
        REPLENISHMENT_EDIT(FIN_FXML_LOCATION, "replenishment.fxml", new Size(400, 1000), "Edit Replenishment"),
        TRANSFER_EDIT(FIN_FXML_LOCATION, "transfer.fxml", new Size(400, 1000), "Edit Transfer"),
        OUTFIT(WARDROBE_FXML_LOCATION, "outfit_dialog.fxml", new Size(1000, 1200), CoreConstants.APP_NAME),
        PIECE(WARDROBE_FXML_LOCATION, "piece_dialog.fxml", new Size(700, 800), CoreConstants.APP_NAME),
        HABIT(HABITS_FXML_LOCATION, "habit_dialog.fxml", new Size(650, 600), CoreConstants.APP_NAME),
        DYNAMIC_FIN_REPORT(FIN_FXML_LOCATION, "dynamic_report.fxml", WINDOW_SIZE, "Finance report"),
        SP_FIN_REPORT(FIN_FXML_LOCATION, "single_period_report.fxml", WINDOW_SIZE, "Finance report"),
        FIN_PLANING(FIN_FXML_LOCATION, "planing.fxml", WINDOW_SIZE, "Finance report"),
        MAKE_MARKS_DIALOG(DIARY_FXML_LOCATION, "make_marks_dialog.fxml", new Size(1000, 800), "Make marks");

        private final String location;
        private final String fileName;
        @Getter
        private final Size size;
        @Getter
        private final String title;

        OrgDialogInfo(String location, String fileName, Size size, String title) {
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
