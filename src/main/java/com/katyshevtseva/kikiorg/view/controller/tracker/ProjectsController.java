package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.tracker.Project;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

class ProjectsController implements FxController {
    @FXML
    private Button addProjectButton;
    @FXML
    private TableView<Project> table;
    @FXML
    private TableColumn<Project, String> titleColumn;
    @FXML
    private TableColumn<Project, String> descColumn;
    @FXML
    private TableColumn<Project, String> codeColumn;
    @FXML
    private TableColumn<Project, Void> editColumn;

    @FXML
    private void initialize() {
        adjustColumns();
        fillTable();
        addProjectButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openProjectEditDialog(new ProjectDialogController(this::fillTable, null)));
    }

    private void fillTable() {
        ObservableList<Project> projects = FXCollections.observableArrayList();
        projects.addAll(Core.getInstance().trackerService().getAllProjects());
        table.setItems(projects);
    }

    private void adjustColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        descColumn.setCellFactory(tc -> {
            TableCell<Project, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        FxUtils.adjustButtonColumn(editColumn, "Edit", (project) ->
                OrganizerWindowCreator.getInstance().openProjectEditDialog(new ProjectDialogController(this::fillTable, project)));
    }
}
