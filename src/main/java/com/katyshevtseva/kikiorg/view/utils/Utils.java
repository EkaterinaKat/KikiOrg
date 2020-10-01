package com.katyshevtseva.kikiorg.view.utils;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Utils {

    private Utils() {
    }

    public static void closeWindowThatContains(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public static void disableNonNumericChars(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public static void associateTextFieldAndButton(TextField textField, Button button) {
        button.setDisable(true);
        textField.textProperty().addListener((observable, oldValue, newValue) ->
                button.setDisable(textField.getText().trim().equals("")));
    }

}
