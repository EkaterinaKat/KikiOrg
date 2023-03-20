package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.HierarchyController;
import com.katyshevtseva.fx.dialog.StandardDialogBuilder;
import com.katyshevtseva.fx.dialogconstructor.DcComboBox;
import com.katyshevtseva.fx.dialogconstructor.DcTextArea;
import com.katyshevtseva.fx.dialogconstructor.DcTextField;
import com.katyshevtseva.fx.dialogconstructor.DialogConstructor;
import com.katyshevtseva.fx.switchcontroller.SectionController;
import com.katyshevtseva.general.TwoArgKnob;
import com.katyshevtseva.hierarchy.HierarchyNode;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

public class FinanceHierarchyController implements SectionController {
    private ComponentBuilder.Component<HierarchyController> hierarchyComponent;
    @FXML
    private Pane pane;

    @FXML
    private void initialize() {
        hierarchyComponent = new ComponentBuilder().setSize(new Size(800, 1090))
                .getHierarchyComponent(Core.getInstance().itemHierarchyService(), true, true, itemLabelAdjuster);

        pane.getChildren().add(hierarchyComponent.getNode());
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
