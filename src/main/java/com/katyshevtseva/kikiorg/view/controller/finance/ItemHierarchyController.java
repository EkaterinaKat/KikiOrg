package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.AddButton;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.Entry;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.SchemaLine;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.List;

public class ItemHierarchyController implements FxController {
    private final ItemSchemaService service = Core.getInstance().itemSchemaService();
    @FXML
    private GridPane schemaBox;

    @FXML
    private void initialize() {
        fillSchema();
    }

    private void fillSchema() {
        schemaBox.getChildren().clear();
        List<SchemaLine> schema = service.getSchema();
        for (int i = 0; i < schema.size(); i++) {
            addLineToSchemaBox(schema.get(i), i);
        }
    }

    private void addLineToSchemaBox(SchemaLine line, int rowIndex) {
        if (line instanceof AddButton) {
            AddButton addButton = (AddButton) line;
            Label label = new Label("<+>");
            label.setStyle(" -fx-font-weight: bold; ");
            label.setOnMouseClicked(event -> {
//                addButton.add();
                fillSchema();
            });
            schemaBox.add(label, addButton.getLevel(), rowIndex);
        } else if (line instanceof Entry) {
            Entry entry = (Entry) line;
            Label label = new Label(entry.getText());
            label.setStyle(" -fx-text-fill: " + entry.getColor() + "; ");
            label.setOnMouseClicked(event -> {
                entry.deleteFromShema();
                fillSchema();
            });
            schemaBox.add(label, entry.getLevel(), rowIndex);
        }
    }
}
