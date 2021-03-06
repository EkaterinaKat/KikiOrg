package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.exeption.SchemaException;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemHierarchyService.ItemHierarchyNode;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.AddButton;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.Entry;
import com.katyshevtseva.kikiorg.core.sections.finance.ItemSchemaService.SchemaLine;
import com.katyshevtseva.kikiorg.core.sections.finance.entity.ItemGroup;
import com.katyshevtseva.kikiorg.view.controller.dialog.InfoDialogController;
import com.katyshevtseva.kikiorg.view.controller.dialog.QuestionDialogController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

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
        Utils.associateButtonWithControls(addButton, nameTextField);
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

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        adjustButtonColumn();
    }

    private void fillTable() {
        ObservableList<ItemGroup> itemGroups = FXCollections.observableArrayList();
        itemGroups.addAll(Core.getInstance().itemHierarchyService().getItemGroupsForCurrentUser());
        table.setItems(itemGroups);
    }

    private void adjustButtonColumn() {
        deleteColumn.setCellFactory(new Callback<TableColumn<ItemGroup, Void>, TableCell<ItemGroup, Void>>() {
            @Override
            public TableCell<ItemGroup, Void> call(final TableColumn<ItemGroup, Void> param) {
                return new TableCell<ItemGroup, Void>() {

                    private final Button button = new Button("[x]");

                    {
                        button.setMaxHeight(10);
                        button.setOnAction((ActionEvent event) ->
                                OrganizerWindowCreator.getInstance().openQuestionDialog(new QuestionDialogController(
                                        "Delete?",
                                        b -> {
                                            if (b) {
                                                Core.getInstance().itemHierarchyService().destroyTreeAndDeleteGroup(
                                                        getTableView().getItems().get(getIndex()));
                                                fillTable();
                                                fillSchema();
                                            }
                                        })));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        });
    }
}
