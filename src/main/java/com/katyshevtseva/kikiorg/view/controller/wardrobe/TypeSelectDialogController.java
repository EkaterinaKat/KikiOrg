package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.StaticHierarchyController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.ClothesType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeHierarchyUtil;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class TypeSelectDialogController implements WindowBuilder.FxController {
    private Component<StaticHierarchyController> hierarchyComponent;
    private OneArgKnob<ClothesType> typeSelectionHandler;
    @FXML
    private VBox container;

    TypeSelectDialogController(OneArgKnob<ClothesType> typeSelectionHandler) {
        this.typeSelectionHandler = typeSelectionHandler;
    }

    @FXML
    private void initialize() {
        if (hierarchyComponent == null) {
            hierarchyComponent = new ComponentBuilder()
                    .setSize(new Size(500, 450))
                    .getStaticHierarchyComponent(
                            WardrobeHierarchyUtil.getSchema(), null,
                            line -> {
                                typeSelectionHandler.execute((ClothesType) line);
                                FxUtils.closeWindowThatContains(container);
                            });
        }
        container.getChildren().clear();
        container.getChildren().add(hierarchyComponent.getNode());
    }

//    private TwoArgKnob<HierarchyNode, Label> getNodeLabelAdjuster() {
//        return (node, label) -> {
//            if (node.isLeaf()) {
//                label.setOnMouseClicked(event -> {
//                    typeSelectionHandler.execute((Item) node);
//                    FxUtils.closeWindowThatContains(container);
//                });
//            }
//        };
//    }
}
