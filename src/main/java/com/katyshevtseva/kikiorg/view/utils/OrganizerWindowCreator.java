package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.kikiorg.core.CoreConstants;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.scene.Node;

import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.*;


public class OrganizerWindowCreator {
    private static final OrganizerWindowCreator INSTANCE = new OrganizerWindowCreator();
    private static final String ICON_IMAGE_PATH = "file:D:\\Code\\KikiOrg\\src\\main\\java\\com\\katyshevtseva\\kikiorg\\view\\res\\images\\ico.png";

    private OrganizerWindowCreator() {
    }

    public static OrganizerWindowCreator getInstance() {
        return INSTANCE;
    }

    public void openMainWindow(FxController controller) {
        new WindowBuilder(FXML_LOCATION + "main.fxml").setIconImagePath(ICON_IMAGE_PATH).
                setController(controller).setHeight(WINDOW_HEIGHT).
                setWidth(WINDOW_WIDTH).setTitle(CoreConstants.APP_NAME).
                setOnWindowCloseEventHandler(event -> System.exit(0)).showWindow();
    }

    public void openQuestionDialog(FxController controller) {
        new WindowBuilder(DIALOG_FXML_LOCATION + "question_dialog.fxml").setIconImagePath(ICON_IMAGE_PATH).
                setController(controller).setHeight(DIALOG_HEIGHT).
                setWidth(DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openInfoDialog(FxController controller) {
        new WindowBuilder(DIALOG_FXML_LOCATION + "info_dialog.fxml").setIconImagePath(ICON_IMAGE_PATH).
                setController(controller).setHeight(DIALOG_HEIGHT).
                setWidth(DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openBigInfoDialog(FxController controller) {
        new WindowBuilder(DIALOG_FXML_LOCATION + "info_dialog.fxml").setIconImagePath(ICON_IMAGE_PATH).
                setController(controller).setHeight(BIG_DIALOG_HEIGHT).
                setWidth(BIG_DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openTwoFieldsEditDialog(FxController controller) {
        new WindowBuilder(DIALOG_FXML_LOCATION + "two_fields_edit_dialog.fxml").setIconImagePath(ICON_IMAGE_PATH).
                setController(controller).setHeight(DIALOG_HEIGHT).
                setWidth(DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    /* ---------------------------------- Гардероб ---------------------------------------------- */

    public Node getMainWardrobeNode(FxController controller) {
        return new WindowBuilder(WARDROBE_FXML_LOCATION + "main_wardrobe.fxml").setController(controller).getNode();
    }

    public Node getOutfitsNode(FxController controller) {
        return new WindowBuilder(WARDROBE_FXML_LOCATION + "outfits.fxml").setController(controller).getNode();
    }

    public Node getAddOutfitNode(FxController controller) {
        return new WindowBuilder(WARDROBE_FXML_LOCATION + "add_outfit.fxml").setController(controller).getNode();
    }

    public Node getPiecesNode(FxController controller) {
        return new WindowBuilder(WARDROBE_FXML_LOCATION + "pieces.fxml").setController(controller).getNode();
    }

    public void openPieceEditDialog(FxController controller) {
        new WindowBuilder(WARDROBE_FXML_LOCATION + "piece_edit_dialog.fxml").setIconImagePath(ICON_IMAGE_PATH).
                setController(controller).setHeight(700).setWidth(800).setTitle(CoreConstants.APP_NAME).showWindow();
    }

    public void openImageSelectionDialog(FxController controller) {
        new WindowBuilder(WARDROBE_FXML_LOCATION + "image_selection_dialog.fxml").setIconImagePath(ICON_IMAGE_PATH).
                setController(controller).setHeight(IMAGE_SELECTION_DIALOG_HEIGHT).
                setWidth(IMAGE_SELECTION_DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
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

    public Node getHabitsAnalysisNode(FxController controller) {
        return new WindowBuilder(HABITS_FXML_LOCATION + "analysis.fxml").setController(controller).getNode();
    }

    public void openHabitEditDialog(FxController controller) {
        new WindowBuilder(DIALOG_FXML_LOCATION + "habit_edit_dialog.fxml").setIconImagePath(ICON_IMAGE_PATH).
                setController(controller).setHeight(BIG_DIALOG_HEIGHT).
                setWidth(BIG_DIALOG_WIDTH).setTitle(CoreConstants.APP_NAME).showWindow();
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

    public Node getHistoryNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "history.fxml").setController(controller).getNode();
    }

    public Node getTransferNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "transfer.fxml").setController(controller).getNode();
    }

    public Node getItemHierarchyNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "item_hierarchy.fxml").setController(controller).getNode();
    }

    public Node getFinanceReportNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "report.fxml").setController(controller).getNode();
    }

    public Node getFinanceAnalysisNode(FxController controller) {
        return new WindowBuilder(FINANCE_FXML_LOCATION + "analysis.fxml").setController(controller).getNode();
    }

    public void openItemSelectDialog(FxController controller) {
        new WindowBuilder(DIALOG_FXML_LOCATION + "item_select_dialog.fxml").setIconImagePath(ICON_IMAGE_PATH).
                setController(controller).setHeight(ITEM_SELECT_DIALOG_HEIGHT).
                setWidth(ITEM_SELECT_DIALOG_WIDTH).setTitle("Select item").showWindow();
    }
}
