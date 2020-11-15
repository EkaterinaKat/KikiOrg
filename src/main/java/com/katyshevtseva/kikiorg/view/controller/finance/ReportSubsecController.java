package com.katyshevtseva.kikiorg.view.controller.finance;

import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

class ReportSubsecController implements FxController {
    @FXML
    private Button updateButton;
    @FXML
    private TextArea reportTextArea;

    @FXML
    private void initialize() {
        updateButton.setOnAction(event -> reportTextArea.setText(Core.getInstance().financeReportService().getReport()));
    }
}
