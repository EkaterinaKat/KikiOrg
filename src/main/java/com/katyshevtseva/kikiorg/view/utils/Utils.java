package com.katyshevtseva.kikiorg.view.utils;

import javafx.scene.Node;
import javafx.scene.control.*;
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

    public static String getBoldTextStyle() {
        return " font-weight: bold; ";
    }

    public static void associateButtonWithControls(Button button, Control... controls) {
        button.setDisable(true);
        for (Control control : controls) {
            if (control instanceof TextField) {
                TextField textField = (TextField) control;
                textField.textProperty().addListener(observable -> setButtonAccessibility(button, controls));
            } else if (control instanceof TextArea) {
                TextArea textArea = (TextArea) control;
                textArea.textProperty().addListener(observable -> setButtonAccessibility(button, controls));
            } else if (control instanceof ComboBox) {
                ComboBox comboBox = (ComboBox) control;
                comboBox.valueProperty().addListener((observable -> setButtonAccessibility(button, controls)));
            } else if (control instanceof DatePicker) {
                DatePicker datePicker = (DatePicker) control;
                datePicker.valueProperty().addListener(observable -> setButtonAccessibility(button, controls));
            }
            else
                throw new RuntimeException("Элемент неизвестного типа");
        }
    }

    private static void setButtonAccessibility(Button button, Control... controls) {
        boolean disableButton = false;
        for (Control control : controls) {
            if (control instanceof TextField && ((TextField) control).getText().trim().equals("")) {
                disableButton = true;
            } else if (control instanceof TextArea && ((TextArea) control).getText().trim().equals("")) {
                disableButton = true;
            } else if (control instanceof ComboBox && ((ComboBox) control).getValue() == null) {
                disableButton = true;
            } else if (control instanceof DatePicker && ((DatePicker)control).getValue() == null){
                disableButton = true;
            }
            button.setDisable(disableButton);
        }
    }
}