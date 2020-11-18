package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ItemHierarchyController implements FxController {
    @FXML
    private VBox schemaBox;
    @FXML
    private Button saveButton;

    @FXML
    private void initialize() {
        fillSchema();
    }

    private void fillSchema() {

    }
}
