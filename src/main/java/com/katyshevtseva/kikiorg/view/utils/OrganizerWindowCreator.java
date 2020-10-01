package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.Controller;
import javafx.scene.Node;

import static com.katyshevtseva.kikiorg.view.utils.Constants.*;


public class OrganizerWindowCreator {
    private static final OrganizerWindowCreator INSTANCE = new OrganizerWindowCreator();

    private OrganizerWindowCreator() {
    }

    public static OrganizerWindowCreator getInstance() {
        return INSTANCE;
    }

    public void openMainWindow(Controller controller) {
        new WindowBuilder(FXML_LOCATION + "main.fxml").setController(controller).setHeight(MAIN_WINDOW_HEIGHT).
                setWidth(MAIN_WINDOW_WIDTH).setTitle(MAIN_WINDOW_TITLE).showWindow();
    }

    /* ----------------------------------  Финансы  ---------------------------------------------- */

    public Node getFinanceModeNode(Controller controller) {
        return new WindowBuilder(FXML_LOCATION + "finance_mode.fxml").setController(controller).getNode();
    }

    public Node getReplenishmentSubmodeNode(Controller controller) {
        return new WindowBuilder(FXML_LOCATION + "replenishment_submode.fxml").setController(controller).getNode();
    }

    public Node getAccountsSubmodeNode(Controller controller) {
        return new WindowBuilder(FXML_LOCATION + "accounts_submode.fxml").setController(controller).getNode();
    }

    public Node getExpensesSubmodeNode(Controller controller) {
        return new WindowBuilder(FXML_LOCATION + "expenses_submode.fxml").setController(controller).getNode();
    }
}
