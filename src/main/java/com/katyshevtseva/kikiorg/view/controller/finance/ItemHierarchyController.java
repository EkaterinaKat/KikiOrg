package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.exeption.SchemaException;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.AddButton;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.Entry;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.SchemaLine;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import com.katyshevtseva.kikiorg.view.utils.OrgUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;

import static com.katyshevtseva.fx.FxUtils.associateButtonWithControls;
import static com.katyshevtseva.fx.FxUtils.setComboBoxItems;

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
    private TableView<ItemGroup> table;
    @FXML
    private TableColumn<ItemGroup, String> titleColumn;
    @FXML
    private TableColumn<ItemGroup, Void> deleteColumn;

    @FXML
    private void initialize() {
        adjustColumns();
        fillTable();
        fillSchema();
        associateButtonWithControls(addButton, nameTextField);
        addButton.setOnAction(event -> {
            hierarchyService.addGroup(nameTextField.getText());
            nameTextField.clear();
            fillSchema();
            fillTable();
        });
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
            setComboBoxItems(comboBox, hierarchyService.getTopLevelNodes());
            comboBox.valueProperty().addListener(observable -> {
                try {
                    addButton.add(comboBox.getValue());
                    fillSchema();
                } catch (SchemaException e) {
                    OrgUtils.getDialogBuilder().openInfoDialog(e.getMessage());
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

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        FxUtils.adjustButtonColumn(deleteColumn, "[x]",
                itemGroup ->
                        OrgUtils.getDialogBuilder().openQuestionDialog("Delete?", b -> {
                            if (b) {
                                Core.getInstance().itemHierarchyService().destroyTreeAndDeleteGroup(itemGroup);
                                fillTable();
                                fillSchema();
                            }
                        }),
                button ->  button.setMaxHeight(10));
    }

    private void fillTable() {
        ObservableList<ItemGroup> itemGroups = FXCollections.observableArrayList();
        itemGroups.addAll(Core.getInstance().itemHierarchyService().getAllItemGroups());
        table.setItems(itemGroups);
    }
}
