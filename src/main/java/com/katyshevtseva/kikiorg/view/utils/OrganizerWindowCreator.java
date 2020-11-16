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
        new WindowBuilder(FXML_LOCATION + "main.fxml").setIconImagePath(
                "file:D:\\Code\\KikiOrg\\src\\main\\java\\com\\katyshevtseva\\kikiorg\\view\\res\\images\\ico.png").
                setController(controller).setHeight(WINDOW_HEIGHT).
                setWidth(WINDOW_WIDTH).setTitle(CoreConstants.APP_NAME).
                setOnWindowCloseEventHandler(event -> System.exit(0)).showWindow();
    }

    public void openQuestionDialog(FxController controller) {
        new WindowBuilder(FXML_LOCATION + "question_dialog.fxml").setIconImagePath(
                "file:D:\\Code\\KikiOrg\\src\\main\\java\\com\\katyshevtseva\\kikiorg\\view\\res\\images\\ico.png").
                setController(controller).setHeight(QUESTION_DIALOG_HEIGHT).
                setWidth(QUESTION_DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    /* ----------------------------------  Привычки ---------------------------------------------- */

    public Node getHabitsSecNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "habits_sec.fxml").setController(controller).getNode();
    }

    public Node getHabitAdminSubsecNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "admin_subsec.fxml").setController(controller).getNode();
    }

    public Node getCheckListSubsecNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "check_list_subsec.fxml").setController(controller).getNode();
    }

    public Node getHabitsReportSubsecNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "report_subsec.fxml").setController(controller).getNode();
    }

    /* ----------------------------------  Финансы  ---------------------------------------------- */

    public Node getFinanceSecNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "finance_sec.fxml").setController(controller).getNode();
    }

    public Node getReplenishmentSubsecNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "replenishment_subsec.fxml").setController(controller).getNode();
    }

    public Node getAccountsSubsecNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "accounts_subsec.fxml").setController(controller).getNode();
    }

    public Node getExpensesSubsecNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "expenses_subsec.fxml").setController(controller).getNode();
    }

    public Node getCheckSubsecNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "check_subsec.fxml").setController(controller).getNode();
    }

    public Node getUserSubsecNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "user_subsec.fxml").setController(controller).getNode();
    }

    public Node getFinanceReportSubsecNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "history_subsec.fxml").setController(controller).getNode();
    }

    public Node getTransferSubsecNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "transfer_subsec.fxml").setController(controller).getNode();
    }
}
