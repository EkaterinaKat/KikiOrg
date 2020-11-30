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
        new WindowBuilder(DIALOG_FXML_LOCATION + "question_dialog.fxml").setIconImagePath(
                "file:D:\\Code\\KikiOrg\\src\\main\\java\\com\\katyshevtseva\\kikiorg\\view\\res\\images\\ico.png").
                setController(controller).setHeight(DIALOG_HEIGHT).
                setWidth(DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openInfoDialog(FxController controller) {
        new WindowBuilder(DIALOG_FXML_LOCATION + "info_dialog.fxml").setIconImagePath(
                "file:D:\\Code\\KikiOrg\\src\\main\\java\\com\\katyshevtseva\\kikiorg\\view\\res\\images\\ico.png").
                setController(controller).setHeight(DIALOG_HEIGHT).
                setWidth(DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openTwoFieldsEditDialog(FxController controller) {
        new WindowBuilder(DIALOG_FXML_LOCATION + "two_fields_edit_dialog.fxml").setIconImagePath(
                "file:D:\\Code\\KikiOrg\\src\\main\\java\\com\\katyshevtseva\\kikiorg\\view\\res\\images\\ico.png").
                setController(controller).setHeight(DIALOG_HEIGHT).
                setWidth(DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
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

    /* ----------------------------------  Финансы  ---------------------------------------------- */

    public Node getMainFinanceNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "main_finance.fxml").setController(controller).getNode();
    }

    public Node getReplenishmentNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "replenishment.fxml").setController(controller).getNode();
    }

    public Node getAccountsNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "accounts.fxml").setController(controller).getNode();
    }

    public Node getExpensesNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "expenses.fxml").setController(controller).getNode();
    }

    public Node getCheckNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "check.fxml").setController(controller).getNode();
    }

    public Node getUserNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "user.fxml").setController(controller).getNode();
    }

    public Node getFinanceReportNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "history.fxml").setController(controller).getNode();
    }

    public Node getTransferNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "transfer.fxml").setController(controller).getNode();
    }

    public Node getItemHierarchyNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "item_hierarchy.fxml").setController(controller).getNode();
    }

    public Node getAnalysisNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "analysis.fxml").setController(controller).getNode();
    }
}
