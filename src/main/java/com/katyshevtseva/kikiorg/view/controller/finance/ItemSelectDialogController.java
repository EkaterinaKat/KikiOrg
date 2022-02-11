package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.controller.HierarchyController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

class ItemSelectDialogController implements FxController {
    private ComponentBuilder.Component<HierarchyController> hierarchyComponent;
    private OneArgKnob<Item> itemSelectionHandler;
    @FXML
    private VBox container;

    ItemSelectDialogController(OneArgKnob<Item> itemSelectionHandler) {
        this.itemSelectionHandler = itemSelectionHandler;
    }

    @FXML
    private void initialize() {
        if (hierarchyComponent == null) {
            hierarchyComponent = new ComponentBuilder()
                    .setSize(new Size(750, 680))
                    .getHierarchyComponent(Core.getInstance().newItemHierarchyService(), false,
                            leaf -> {
                                itemSelectionHandler.execute((Item) leaf);
                                FxUtils.closeWindowThatContains(container);
                            });
        }
        container.getChildren().clear();
        container.getChildren().add(hierarchyComponent.getNode());
    }
}
