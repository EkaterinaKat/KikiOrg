package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcCheckBoxGroup;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.AccountGroupService;
import com.katyshevtseva.kikiorg.core.sections.finance.FinanceService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Account;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.AccountGroup;
import com.katyshevtseva.kikiorg.core.sections.finance.report.Cfd;
import com.katyshevtseva.kikiorg.core.sections.finance.report.DynamicsReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.DynamicsReportService.DrElement;
import com.katyshevtseva.kikiorg.core.sections.finance.report.FinanceReportService;
import com.katyshevtseva.kikiorg.core.sections.finance.report.SpodReport;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.katyshevtseva.fx.FxUtils.getPaneWithHeight;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.REPORT_PANE;

public class DynamicReportController implements WindowBuilder.FxController {
    private final AccountGroupService accountGroupService = Core.getInstance().accountGroupService;
    private final DynamicsReportService dynamicsReportService = Core.getInstance().dynamicsReportService;
    private final FinanceReportService reportService = Core.getInstance().financeReportService;
    private final FinanceService financeService = Core.getInstance().financeService;
    private ToggleGroup toggleGroup;
    private Map<RadioButton, AccountGroup> radioButtonAccountGroupMap;
    private final ReportPaneController pieChartPaneController = new ReportPaneController();
    @FXML
    private VBox accountPane;
    @FXML
    private Button showButton;
    @FXML
    private VBox lineChartPane;
    @FXML
    private VBox pieChartPane;

    @FXML
    private void initialize() {
        tuneAccountPane();
        showButton.setOnAction(event -> showReport());
        pieChartPane.getChildren().add(WindowBuilder.getNode(REPORT_PANE, pieChartPaneController));
    }

    private void showReport() {
        AccountGroup group = radioButtonAccountGroupMap.get((RadioButton) toggleGroup.getSelectedToggle());
        if (group == null) {
            return;
        }

        try {
            showChart(dynamicsReportService.getReport(group));
        } catch (Exception e) {
            new StandardDialogBuilder().openInfoDialog(e.getMessage());
        }
    }

    private void showChart(List<DrElement> elements) {
        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Number> outgoSeries = new XYChart.Series<>();
        outgoSeries.setName("Outgo");
        for (DrElement element : elements) {
            XYChart.Data<String, Number> data1 = new XYChart.Data<>(element.getPeriod().getTitle(), element.getIncome());
            data1.setNode(getNode(element.getIncome() + "", Cfd.INCOME, element));
            incomeSeries.getData().add(data1);

            XYChart.Data<String, Number> data2 = new XYChart.Data<>(element.getPeriod().getTitle(), element.getOutcome());
            data2.setNode(getNode(element.getOutcome() + "", Cfd.OUTGO, element));
            outgoSeries.getData().add(data2);
        }
        lineChart.getData().add(incomeSeries);
        lineChart.getData().add(outgoSeries);

        FxUtils.setSize(lineChart, new Size(500, 900));
        lineChartPane.getChildren().clear();
        lineChartPane.getChildren().add(lineChart);
    }

    public Node getNode(String s, Cfd cfd, DrElement element) {
        Label label = new Label(s);
        label.setStyle(Styler.getTextSizeStyle(8));
        label.setOnMouseClicked(event -> {
            SpodReport spodReport = cfd == Cfd.INCOME ?
                    reportService.getIncomeReport(element.getAccountGroup().getAccounts(), element.getPeriod().getPeriod()) :
                    reportService.getOutgoReport(element.getAccountGroup().getAccounts(), element.getPeriod().getPeriod());
            pieChartPaneController.showReport(spodReport);
        });
        return label;
    }

    // дублирование
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
}
