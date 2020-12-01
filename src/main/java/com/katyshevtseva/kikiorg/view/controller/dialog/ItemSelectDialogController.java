package com.katyshevtseva.kikiorg.view.controller.dialog;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.EmptyLine;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.Entry;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.SchemaLine;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.Item;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class ItemSelectDialogController implements FxController {
    @FXML
    private VBox container;
    private ItemSelectionHandler itemSelectionHandler;

    public ItemSelectDialogController(ItemSelectionHandler itemSelectionHandler) {
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
                    label.setStyle(Utils.getPurpleTextStyle() + Utils.getBoldTextStyle());
                    label.setOnMouseClicked(event -> {
                        itemSelectionHandler.execute(entry.getItem());
                        Utils.closeWindowThatContains(container);
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

    @FunctionalInterface
    public interface ItemSelectionHandler {
        void execute(Item selectedItem);
    }
}
