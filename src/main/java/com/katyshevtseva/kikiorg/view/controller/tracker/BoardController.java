package com.katyshevtseva.kikiorg.view.controller.tracker;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

class BoardController implements FxController {
    @FXML
    private Button addTaskButton;

    @FXML
    private void initialize() {
        updateBoard();
        addTaskButton.setOnAction(event ->
                OrganizerWindowCreator.getInstance().openTaskDialog(new TaskDialogController(this::updateBoard)));
    }

    private void updateBoard() {
        System.out.println("update board");
    }
}
