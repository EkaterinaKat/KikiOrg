package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.switchcontroller.AbstractSwitchController;
import com.katyshevtseva.fx.switchcontroller.Section;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.DYNAMIC_FIN_REPORT;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgDialogInfo.SP_FIN_REPORT;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.*;

public class MainFinanceController extends AbstractSwitchController implements SectionController {
    private final SinglePeriodReportController singlePeriodReportController = new SinglePeriodReportController();
    private final DynamicReportController dynamicReportController = new DynamicReportController();
    @FXML
    private Pane mainPane;
    @FXML
    private HBox buttonBox;

    @FXML
    private void initialize() {
        init(getSections(), mainPane, this::placeButton);

        Button spReportButton = new Button("Sp Report");
        spReportButton.setOnAction(event -> WindowBuilder.openDialog(SP_FIN_REPORT, singlePeriodReportController));
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), spReportButton);

        Button dynamicReportButton = new Button("Dynamic Report");
        dynamicReportButton.setOnAction(event -> WindowBuilder.openDialog(DYNAMIC_FIN_REPORT, dynamicReportController));
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), dynamicReportButton);
    }

    private List<Section> getSections() {
        return Arrays.asList(
                new Section("Ledger", new LedgerController(),
                        controller -> WindowBuilder.getNode(LEDGER, controller)),
                new Section("Admin", new AdminController(),
                        controller -> WindowBuilder.getNode(FIN_ADMIN, controller)),
                new Section("History", new HistoryController(),
                        controller -> WindowBuilder.getNode(FIN_HISTORY, controller)),
                new Section("Item Hierarchy", new FinanceHierarchyController(),
                        controller -> WindowBuilder.getNode(FIN_HIERARCHY, controller)),
                new Section("XXX", new XxxController(), controller -> WindowBuilder.getNode(XXX, controller)));
    }

    private void placeButton(Button button) {
        buttonBox.getChildren().addAll(FxUtils.getPaneWithWidth(30), button);
    }
}
