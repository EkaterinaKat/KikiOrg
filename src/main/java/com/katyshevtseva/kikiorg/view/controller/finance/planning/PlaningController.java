package com.katyshevtseva.kikiorg.view.controller.finance.planning;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.ReportUtils;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcNumField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.ExpenseGroupingType;
import com.katyshevtseva.kikiorg.core.sections.finance.PlanningService;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.AccountGroup;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import static com.katyshevtseva.kikiorg.core.sections.finance.ExpenseGroupingType.WITHOUT_GROUPING;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowUtil.OrgNodeInfo.POTENTIAL_EXPENSE;

public class PlaningController implements WindowBuilder.FxController {
    private final PlanningService planningService = Core.getInstance().planningService();
    @FXML
    private ComboBox<AccountGroup> groupAccountComboBox;
    @FXML
    private Label infoLabel;
    @FXML
    private Pane expensePane;
    @FXML
    private ComboBox<ExpenseGroupingType> groupingTypeComboBox;
    @FXML
    private GridPane tablePane;

    @FXML
    private void initialize() {
        //POTENTIAL_EXPENSE pane
        PotentialExpenseController expenseController = new PotentialExpenseController(this::update);
        expensePane.getChildren().add(WindowBuilder.getNode(POTENTIAL_EXPENSE, expenseController));

        //groupAccountComboBox
        FxUtils.setComboBoxItems(groupAccountComboBox, Core.getInstance().accountGroupService().getAll());
        groupAccountComboBox.setValue(planningService.getSelectedAccountGroupOrNull());
        groupAccountComboBox.setOnAction(event -> {
            planningService.setSelectedAccountGroup(groupAccountComboBox.getValue());
            update();
        });

        //groupingTypeComboBox
        FxUtils.setComboBoxItems(groupingTypeComboBox, ExpenseGroupingType.values(), WITHOUT_GROUPING);
        groupingTypeComboBox.setOnAction(event -> update());

        //infoLabel
        infoLabel.setContextMenu(getLimitContextMenu());

        update();
    }

    public void update() {
        infoLabel.setText(planningService.getPlanningInfo());
        ReportUtils.showReport(PlanningUtil.getReport(groupingTypeComboBox.getValue()), tablePane, true);
    }

    private ContextMenu getLimitContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem item = new MenuItem("Change limit");
        item.setOnAction(event1 -> {
            DcNumField numField = new DcNumField(true, planningService.getUpperLimit().longValue());
            DialogConstructor.constructDialog(() -> {
                planningService.setUpperLimit(numField.getValue().intValue());
                update();
            }, numField);
        });
        contextMenu.getItems().add(item);

        return contextMenu;
    }
}
