package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.Styler;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.OneArgKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.EmptyLine;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.Entry;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.SchemaLine;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.closeWindowThatContains;
import static com.katyshevtseva.fx.Styler.StandardColor.PURPLE;
import static com.katyshevtseva.fx.Styler.ThingToColor.TEXT;

class ItemSelectDialogController implements FxController {
    @FXML
    private VBox container;
    private OneArgKnob<Item> itemSelectionHandler;

    ItemSelectDialogController(OneArgKnob<Item> itemSelectionHandler) {
        this.itemSelectionHandler = itemSelectionHandler;
    }

    @FXML
    private void initialize() {
        fillSchema();
    }

    private void fillSchema() {
        List<SchemaLine> schema = Core.getInstance().itemSchemaService().getSchema();
        for (SchemaLine line : schema) {
            Label label = new Label();
            if (line instanceof EmptyLine) {
                label.setText("");
            } else if (line instanceof Entry) {
                Entry entry = (Entry) line;
                label.setText(getIndentByLevel(entry.getLevel()) + entry.getText());
                if (entry.isLeaf()) {
                    label.setStyle(Styler.getColorfullStyle(TEXT, PURPLE) + Styler.getBoldTextStyle());
                    label.setOnMouseClicked(event -> {
                        itemSelectionHandler.execute(entry.getItem());
                        closeWindowThatContains(container);
                    });
                }
            }
            container.getChildren().add(label);
        }
    }

    private String getIndentByLevel(int level) {
        String indent = " ";
        for (int i = 0; i < level; i++) {
            indent += "         ";
        }
        return indent;
    }
}
