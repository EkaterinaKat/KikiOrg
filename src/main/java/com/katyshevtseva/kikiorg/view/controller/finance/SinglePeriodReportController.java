package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcCheckBoxGroup;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.AccountGroupService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.AccountGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.ReportPeriodService.ReportPeriod;
import com.katyshevtseva.kikiorg.core.sections.finance.report.SinglePeriodReport;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.fx.FxUtils.getPeriod;
import static com.katyshevtseva.general.GeneralUtils.getColumnByIndexAndColumnNum;
import static com.katyshevtseva.general.GeneralUtils.getRowByIndexAndColumnNum;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.REPORT_PANE;

class SinglePeriodReportController implements FxController {
    private static final int NUM_OF_COLUMNS_IN_PERIOD_GRID = 3;
    private final AccountGroupService accountGroupService = Core.getInstance().accountGroupService;
    private final FinanceReportService reportService = Core.getInstance().financeReportService;
    private final ReportPeriodService periodService = Core.getInstance().reportPeriodService;
    private final FinanceService financeService = Core.getInstance().financeService;
    private ToggleGroup toggleGroup;
    private Map<RadioButton, AccountGroup> radioButtonAccountGroupMap;
    private final ReportPaneController incomePaneController = new ReportPaneController();
    private final ReportPaneController outgoPaneController = new ReportPaneController();
    @FXML
    private VBox accountPane;
    @FXML
    private GridPane periodPane;
    @FXML
    private Button showButton;
    @FXML
    private Pane incomePane;
    @FXML
    private Pane outgoPane;
    @FXML
    private Label resultLabel;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;

    @FXML
    private void initialize() {
        tuneAccountPane();
        tunePeriodPane();
        showButton.setOnAction(event -> showReport());
        FxUtils.associateButtonWithControls(showButton, startDatePicker, endDatePicker);

        incomePane.getChildren().add(WindowBuilder.getNode(REPORT_PANE, incomePaneController));
        outgoPane.getChildren().add(WindowBuilder.getNode(REPORT_PANE, outgoPaneController));
    }

    private void showReport() {
        AccountGroup group = radioButtonAccountGroupMap.get((RadioButton) toggleGroup.getSelectedToggle());
        if (group == null) {
            return;
        }

        SinglePeriodReport report = reportService.getReport(group, getPeriod(startDatePicker, endDatePicker));
        resultLabel.setText("Total: " + report.getTotal());
        incomePaneController.showReport(report.getIncomeReport());
        outgoPaneController.showReport(report.getOutgoReport());
    }

    private void tuneAccountPane() {
        accountPane.getChildren().clear();
        radioButtonAccountGroupMap = new HashMap<>();
        accountPane.getChildren().add(getPaneWithHeight(15));
        toggleGroup = new ToggleGroup();
        for (AccountGroup accountGroup : accountGroupService.getAll()) {
            RadioButton radioButton = new RadioButton(accountGroup.getFullInfo());
            radioButtonAccountGroupMap.put(radioButton, accountGroup);
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setWrapText(true);
            radioButton.setContextMenu(getAccountGroupContextMenu(accountGroup));
            FxUtils.setWidth(radioButton, 250);
            radioButton.setStyle(Styler.getTextSizeStyle(13));
            accountPane.getChildren().add(radioButton);
            accountPane.getChildren().add(getPaneWithHeight(15));
        }
        Button addButton = new Button("+");
        addButton.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, "");
            DcCheckBoxGroup<Account> checkBoxGroup = new DcCheckBoxGroup<>(financeService.getAllAccounts());
            DialogConstructor.constructDialog(() -> {
                try {
                    accountGroupService.create(titleField.getValue(), checkBoxGroup.getSelectedElements());
                    tuneAccountPane();
                } catch (Exception e) {
                    new StandardDialogBuilder().openInfoDialog(e.getMessage());
                }
            }, titleField, checkBoxGroup);
        });
        accountPane.getChildren().add(addButton);
    }

    private ContextMenu getAccountGroupContextMenu(AccountGroup accountGroup) {
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event1 -> new StandardDialogBuilder().openQuestionDialog("Delete?", b -> {
            if (b) {
                accountGroupService.delete(accountGroup);
                tuneAccountPane();
            }
        }));

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(deleteItem);
        return contextMenu;
    }

    private void tunePeriodPane() {
        List<ReportPeriod> periods = periodService.getSeveralPastMonthsPeriods(6);
        periods.add(periodService.getAllTimePeriod());

        periodPane.getChildren().clear();
        for (int i = 0; i < periods.size(); i++) {
            ReportPeriod period = periods.get(i);
            Button button = new Button(period.getTitle());
            button.setOnAction(event -> {
                FxUtils.setDate(startDatePicker, period.getPeriod().start());
                FxUtils.setDate(endDatePicker, period.getPeriod().end());
            });
            periodPane.add(button,
                    getColumnByIndexAndColumnNum(i, NUM_OF_COLUMNS_IN_PERIOD_GRID),
                    getRowByIndexAndColumnNum(i, NUM_OF_COLUMNS_IN_PERIOD_GRID));
        }
    }
}
