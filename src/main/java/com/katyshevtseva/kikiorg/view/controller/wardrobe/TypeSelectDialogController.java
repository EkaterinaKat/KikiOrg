package com.katyshevtseva.kikiorg.view.controller.wardrobe;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.Size;
import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder;
import com.katyshevtseva.fx.component.ComponentBuilder.Component;
import com.katyshevtseva.fx.component.controller.StaticHierarchyController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.PieceType;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.WardrobeHierarchyUtil;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.entity.Piece;
import com.katyshevtseva.kikiorg.core.sections.wardrobe.enums.PieceSubtype;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.katyshevtseva.fx.Styler.StandardColor.GRAY;
import static com.katyshevtseva.fx.Styler.ThingToColor.TEXT;

public class TypeSelectDialogController implements WindowBuilder.FxController {
    private Component<StaticHierarchyController> hierarchyComponent;
    private final OneArgKnob<PieceType> typeSelectionHandler;
    private final List<Piece> pieces;
    @FXML
    private VBox container;

    // Там где вызывается этот конструктор, всё происходит немного костыльно. Сначала с помощью одного метода сервиса
    // получается, а потом передаётся сюда список всех, подходящих по ситуации предметов, чтобы в методе piecesOfThisTypeExist
    // мы выяснили: делать ли пункт с типом активным. А потом с помощью другого метода, который принимает ту же инфу, что и
    // первый + тип, получаем уже предметы для отображения. Костыльность в том, что запросто может случится так, что эти
    // два метода могут быть разных мнений о том, какие предметы доступны
    TypeSelectDialogController(List<Piece> pieces, OneArgKnob<PieceType> typeSelectionHandler) {
        this.typeSelectionHandler = typeSelectionHandler;
        this.pieces = pieces;
    }

    @FXML
    private void initialize() {
        if (hierarchyComponent == null) {
            hierarchyComponent = new ComponentBuilder()
                    .setSize(new Size(500, 450))
                    .getStaticHierarchyComponent(
                            WardrobeHierarchyUtil.getSchema(), (line, label) -> {
                                if (!piecesOfThisTypeExist((PieceType) line)) {
                                    label.setStyle(Styler.getColorfullStyle(TEXT, GRAY));
                                }
                            },
                            line -> {
                                if (piecesOfThisTypeExist((PieceType) line)) {
                                    typeSelectionHandler.execute((PieceType) line);
                                    FxUtils.closeWindowThatContains(container);
                                }
                            });
        }
        container.getChildren().clear();
        container.getChildren().add(hierarchyComponent.getNode());
    }

    private boolean piecesOfThisTypeExist(PieceType type) {
        List<PieceSubtype> subtypes = type.getSubtypes();
        for (Piece piece : pieces) {
            for (PieceSubtype subtype : subtypes) {
                if (piece.getType().equals(subtype)) {
                    return true;
                }
            }
        }
        return false;
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
