package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService.ReportPeriod;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class FullReportController implements FxController {
    private final FinanceReportService reportService = Core.getInstance().financeReportService();
    private final ReportPeriodService periodService = Core.getInstance().reportPeriodService();
    private final FinanceService financeService = Core.getInstance().financeService();
    private Map<CheckBox, Account> checkBoxAccountMap;
    private ToggleGroup toggleGroup;
    private Map<RadioButton, ReportPeriod> radioButtonPeriodMap;
    private final ReportPaneController incomePaneController = new ReportPaneController();
    private final ReportPaneController outgoPaneController = new ReportPaneController();
    @FXML
    private VBox accountPane;
    @FXML
    private VBox periodPane;
    @FXML
    private Button showButton;
    @FXML
    private Pane incomePane;
    @FXML
    private Pane outgoPane;
    @FXML
    private Label resultLabel;

    @FXML
    private void initialize() {
        tuneAccountPane();
        tunePeriodPane();
        showButton.setOnAction(event -> showReport());

        incomePane.getChildren().add(OrganizerWindowCreator.getInstance().getReportPaneNode(incomePaneController));
        outgoPane.getChildren().add(OrganizerWindowCreator.getInstance().getReportPaneNode(outgoPaneController));
    }

    private void showReport() {
        List<Account> accounts = checkBoxAccountMap.keySet().stream().filter(CheckBox::isSelected)
                .map(checkBox -> checkBoxAccountMap.get(checkBox)).collect(Collectors.toList());
        ReportPeriod reportPeriod = radioButtonPeriodMap.get((RadioButton) toggleGroup.getSelectedToggle());

        if (accounts.isEmpty() || reportPeriod == null) {
            return;
        }

        reportService.getReport(accounts, reportPeriod);
    }

    private void tuneAccountPane() {
        accountPane.getChildren().clear();
        checkBoxAccountMap = new HashMap<>();
        accountPane.getChildren().add(FxUtils.getPaneWithHeight(15));
        for (Account account : financeService.getAllAccounts().stream()
                .sorted(Comparator.comparing(Account::getMy).reversed()).collect(Collectors.toList())) {
            CheckBox checkBox = new CheckBox(account.getTitle());
            checkBoxAccountMap.put(checkBox, account);
            checkBox.setSelected(account.getMy());
            accountPane.getChildren().add(checkBox);
            accountPane.getChildren().add(FxUtils.getPaneWithHeight(15));
        }
    }

    private void tunePeriodPane() {
        periodPane.getChildren().clear();
        radioButtonPeriodMap = new HashMap<>();
        periodPane.getChildren().add(FxUtils.getPaneWithHeight(15));
        toggleGroup = new ToggleGroup();
        for (ReportPeriod reportPeriod : periodService.getReportPeriods()) {
            RadioButton radioButton = new RadioButton(reportPeriod.toString());
            radioButtonPeriodMap.put(radioButton, reportPeriod);
            radioButton.setToggleGroup(toggleGroup);
            periodPane.getChildren().add(radioButton);
            periodPane.getChildren().add(FxUtils.getPaneWithHeight(15));
        }
    }
}
