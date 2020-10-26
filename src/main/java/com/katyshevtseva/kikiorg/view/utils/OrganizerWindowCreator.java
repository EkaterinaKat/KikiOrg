package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.kikiorg.core.CoreConstants;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
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
        new WindowBuilder(FXML_LOCATION + "main.fxml").setController(controller).setHeight(WINDOW_HEIGHT).
                setWidth(WINDOW_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    /* ----------------------------------  Привычки ---------------------------------------------- */

    public Node getHabitsModeNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "habits_mode.fxml").setController(controller).getNode();
    }

    public Node getHabitAdminSubmodeNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "admin_submode.fxml").setController(controller).getNode();
    }

    public Node getCheckListSubmodeNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "check_list_submode.fxml").setController(controller).getNode();
    }

    public Node getHabitsReportSubmodeNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "report_submode.fxml").setController(controller).getNode();
    }

    /* ----------------------------------  Финансы  ---------------------------------------------- */

    public Node getFinanceModeNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "finance_mode.fxml").setController(controller).getNode();
    }

    public Node getReplenishmentSubmodeNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "replenishment_submode.fxml").setController(controller).getNode();
    }

    public Node getAccountsSubmodeNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "accounts_submode.fxml").setController(controller).getNode();
    }

    public Node getExpensesSubmodeNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "expenses_submode.fxml").setController(controller).getNode();
    }

    public Node getCheckSubmodeNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "check_submode.fxml").setController(controller).getNode();
    }
}
