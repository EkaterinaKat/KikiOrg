package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.AbstractSwitchController;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.HierarchyController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcComboBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.general.TwoArgKnob;
import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainFinanceController extends AbstractSwitchController implements FxController {
    @FXML
    private Pane mainPane;
    @FXML
    private Button historyButton;
    @FXML
    private Button itemHierarchyButton;
    @FXML
    private Button reportButton;
    @FXML
    private Button adminButton;
    @FXML
    private Button ledgerButton;
    @FXML
    private Button xxxButton;

    private final HistoryController historyController = new HistoryController();
    private final FullReportController fullReportController = new FullReportController();
    private final AdminController adminController = new AdminController();
    private final LedgerController ledgerController = new LedgerController();
    private final XxxController xxxController = new XxxController();

    private Node historyNode;
    private Node adminNode;
    private Node ledgerNode;
    private Node xxxNode;

    private ComponentBuilder.Component<HierarchyController> hierarchyComponent;

    @FXML
    private void initialize() {
        pane = mainPane;
        buttons.addAll(Arrays.asList(ledgerButton, historyButton, itemHierarchyButton, adminButton, xxxButton));
        ledgerButtonListener();
        historyButton.setOnAction(event -> historyButtonListener());
        itemHierarchyButton.setOnAction(event -> itemHierarchyButtonListener());
        reportButton.setOnAction(event -> reportButtonListener());
        adminButton.setOnAction(event -> adminButtonListener());
        ledgerButton.setOnAction(event -> ledgerButtonListener());
        xxxButton.setOnAction(event -> xxxButtonListener());
    }

    private void xxxButtonListener() {
        activateMode(xxxButton, xxxNode,
                OrganizerWindowCreator.getInstance()::getXxxNode, xxxController);
    }

    private void ledgerButtonListener() {
        activateMode(ledgerButton, ledgerNode,
                OrganizerWindowCreator.getInstance()::getLedgerNode, ledgerController);
    }

    private void adminButtonListener() {
        activateMode(adminButton, adminNode,
                OrganizerWindowCreator.getInstance()::getFinanceAdminNode, adminController);
    }

    private void itemHierarchyButtonListener() {
        if (hierarchyComponent == null) {
            hierarchyComponent = new ComponentBuilder().setSize(new Size(800, 1090))
                    .getHierarchyComponent(Core.getInstance().itemHierarchyService(), true, true, itemLabelAdjuster);
        }
        activateMode(itemHierarchyButton, hierarchyComponent);
    }

    private void reportButtonListener() {
        OrganizerWindowCreator.getInstance().openFinanceReportDialog(fullReportController);
    }

    private void historyButtonListener() {
        activateMode(historyButton, historyNode,
                OrganizerWindowCreator.getInstance()::getHistoryNode, historyController);
    }

    private final TwoArgKnob<HierarchyNode, Label> itemLabelAdjuster = (hierarchyNode, label) -> {
        if (hierarchyNode instanceof Item) {
            label.setContextMenu(getContextMenu((Item) hierarchyNode));
        }
    };

    private ContextMenu getContextMenu(Item item) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem editItem = new MenuItem("Edit");
        editItem.setOnAction(event -> {
            DcTextField titleField = new DcTextField(true, item.getTitle());
            DcTextArea descField = new DcTextArea(false, item.getDescription());
            DialogConstructor.constructDialog(() -> {
                Core.getInstance().financeService().alterItem(item, titleField.getValue(), descField.getValue());
                hierarchyComponent.getController().fillSchema();
            }, titleField, descField);
        });

        MenuItem mergeItem = new MenuItem("Merge");
        mergeItem.setOnAction(event -> {
            DcComboBox<Item> itemComboBox = new DcComboBox<>(true, null, Core.getInstance().financeService().getAllItems());
            DialogConstructor.constructDialog(() -> {
                merge(item, itemComboBox.getValue());
                hierarchyComponent.getController().fillSchema();
            }, itemComboBox);
        });

        contextMenu.getItems().add(editItem);
        contextMenu.getItems().add(mergeItem);
        return contextMenu;
    }

    private void merge(Item itemToMerge, Item destItem) {
        try {
            Core.getInstance().itemMergeService().merge(itemToMerge, destItem);
            new StandardDialogBuilder().openInfoDialog("Success");
        } catch (Exception e) {
            new StandardDialogBuilder().setSize(1000, 1000).openInfoDialog(e.getMessage());
        }
    }
}
