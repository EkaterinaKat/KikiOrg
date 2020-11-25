package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.exeption.SchemaException;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.AddButton;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.Entry;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.SchemaLine;
import com.katyshevtseva.kikiorg.view.controller.dialog.InfoDialogController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.List;

class ItemHierarchyController implements FxController {
    private final ItemSchemaService schemaService = Core.getInstance().itemSchemaService();
    private final ItemHierarchyService hierarchyService = Core.getInstance().itemHierarchyService();
    @FXML
    private GridPane schemaBox;
    @FXML
    private Button addButton;
    @FXML
    private TextField nameTextField;

    @FXML
    private void initialize() {
//        fillSchema();
//        Utils.associateButtonWithControls(addButton, nameTextField);
//        addButton.setOnAction(event -> {
//            hierarchyService.saveGroup(nameTextField.getText());
//            nameTextField.clear();
//            fillSchema();
//        });
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
            comboBox.setItems(FXCollections.observableArrayList(hierarchyService.getTopLevelNodesForCurrentUser()));
            comboBox.valueProperty().addListener(observable -> {
                try {
                    addButton.add(comboBox.getValue());
                    fillSchema();
                } catch (SchemaException e) {
                    OrganizerWindowCreator.getInstance().openInfoDialog(new InfoDialogController(e.getMessage()));
                    comboBox.setVisible(false);
                    e.printStackTrace();
                }
            });

            Label label = new Label("<+>");
            label.setOnMouseClicked(event -> comboBox.setVisible(true));

            HBox hBox = new HBox();
            hBox.getChildren().addAll(label, comboBox);
            schemaBox.add(hBox, addButton.getLevel(), rowIndex);

        } else if (line instanceof Entry) {
            Entry entry = (Entry) line;

            Label entryLabel = new Label(entry.isLeaf() ? entry.getText() : entry.getText().toUpperCase());
            entryLabel.setStyle(" -fx-text-fill: " + entry.getColor() + "; ");

            HBox hBox = new HBox();
            hBox.getChildren().add(entryLabel);

            if (!entry.isTopLevel()) {
                Label deleteLabel = new Label("<->");
                deleteLabel.setOnMouseClicked(event -> {
                    entry.deleteFromSchema();
                    fillSchema();
                });
                hBox.getChildren().add(deleteLabel);
            }

            schemaBox.add(hBox, entry.getLevel(), rowIndex);
        }
    }
}
