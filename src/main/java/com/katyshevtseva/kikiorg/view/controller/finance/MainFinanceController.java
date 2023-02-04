package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.switchcontroller.AbstractSwitchController;
import com.katyshevtseva.fx.switchcontroller.Section;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.DialogInfo.FIN_REPORT;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.*;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

public class MainFinanceController extends AbstractSwitchController {
    private final FullReportController fullReportController = new FullReportController();
    @FXML
    private Pane mainPane;
    @FXML
    private HBox buttonBox;

    @FXML
    private void initialize() {
        init(getSections(), mainPane, this::placeButton);

        Button reportButton = new Button("Report");
        reportButton.setOnAction(event -> windowCreator().openDialog(FIN_REPORT, fullReportController));
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), reportButton);
    }

    private List<Section> getSections() {
        return Arrays.asList(
                new Section("Ledger", new LedgerController(),
                        controller -> windowCreator().getNode(LEDGER, controller)),
                new Section("Admin", new AdminController(),
                        controller -> windowCreator().getNode(FIN_ADMIN, controller)),
                new Section("History", new HistoryController(),
                        controller -> windowCreator().getNode(FIN_HISTORY, controller)),
                new Section("Item Hierarchy", new FinanceHierarchyController(),
                        controller -> windowCreator().getNode(FIN_HIERARCHY, controller)),
                new Section("XXX", new XxxController(), controller -> windowCreator().getNode(XXX, controller)));
    }

    private void placeButton(Button button) {
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), button);
    }
}
