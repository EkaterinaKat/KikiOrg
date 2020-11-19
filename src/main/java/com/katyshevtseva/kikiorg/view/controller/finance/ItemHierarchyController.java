package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.exeption.SchemaException;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.AddButton;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.Entry;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.SchemaLine;
import com.katyshevtseva.kikiorg.view.controller.dialog.InfoDialogController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class ItemHierarchyController implements FxController {
    private final ItemSchemaService schemaService = Core.getInstance().itemSchemaService();
    private final ItemHierarchyService hierarchyService = Core.getInstance().itemHierarchyService();
    @FXML
    private GridPane schemaBox;

    @FXML
    private void initialize() {
        fillSchema();
    }

    private void fillSchema() {
        schemaBox.getChildren().clear();
        List<SchemaLine> schema = schemaService.getSchema();
        for (int i = 0; i < schema.size(); i++) {
            addLineToSchemaBox(schema.get(i), i);
        }
    }

    private void addLineToSchemaBox(SchemaLine line, int rowIndex) {
        if (line instanceof AddButton) {
            AddButton addButton = (AddButton) line;

            ComboBox<ItemHierarchyNode> comboBox = new ComboBox<>();
            comboBox.setVisible(false);
            comboBox.setItems(FXCollections.observableArrayList(hierarchyService.getTopLevelNodes()));
            comboBox.valueProperty().addListener(observable -> {
                try {
                    addButton.add(comboBox.getValue());
                    fillSchema();
                } catch (SchemaException e) {
                    OrganizerWindowCreator.getInstance().openInfoDialog(new InfoDialogController(e.getMessage()));
                    e.printStackTrace();
                }
            });

            Label label = new Label("<+>");
            label.setStyle(" -fx-font-weight: bold; ");
            label.setOnMouseClicked(event -> comboBox.setVisible(true));

            HBox hBox = new HBox();
            hBox.getChildren().addAll(label, comboBox);
            schemaBox.add(hBox, addButton.getLevel(), rowIndex);

        } else if (line instanceof Entry) {
            Entry entry = (Entry) line;

            Label entryLabel = new Label(entry.getText());
            entryLabel.setStyle(" -fx-text-fill: " + entry.getColor() + "; ");

            Label deleteLabel = new Label("<->");
            deleteLabel.setOnMouseClicked(event -> {
                entry.deleteFromShema();
                fillSchema();
            });

            schemaBox.add(entryLabel, entry.getLevel(), rowIndex);
            schemaBox.add(deleteLabel, entry.getLevel() + 1, rowIndex);
        }
    }
}
