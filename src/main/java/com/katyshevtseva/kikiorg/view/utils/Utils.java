package com.katyshevtseva.kikiorg.view.utils;

import com.katyshevtseva.kikiorg.core.date.Period;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static com.katyshevtseva.kikiorg.view.utils.ViewConstants.IMAGE_LOCATION;

public class Utils {

    private Utils() {
    }

    public static Period getPeriodByDp(DatePicker startDp, DatePicker endDp) {
        return new Period(Date.valueOf(startDp.getValue()), Date.valueOf(endDp.getValue()));
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
        return " -fx-font-weight: bold; ";
    }

    public static String getGreenBackground() {
        return " -fx-background-color:#4FFF4C ";
    }

    public static String getOrangeBackground() {
        return " -fx-background-color:#FFA24C ";
    }

    public static String getBlueBackground() {
        return " -fx-background-color:#4C9FFF ";
    }

    public static String getPurpleTextStyle() {
        return " -fx-text-fill: #800080; ";
    }

    public static String getGreenTextStyle() {
        return " -fx-text-fill: #008000; ";
    }

    public static String getGrayTextStyle() {
        return " -fx-text-fill: #808080; ";
    }

    public static void associateButtonWithControls(Button button, Control... controls) {
        associateButtonWithControls(button, Arrays.asList(controls));
    }

    public static void setImageOnButton(String imageName, Button button, int imageSize) {
        Image image = new Image(IMAGE_LOCATION + imageName);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(imageSize);
        imageView.setFitWidth(imageSize);
        button.graphicProperty().setValue(imageView);
    }

    public static void associateButtonWithControls(Button button, List<Control> controls) {
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
            } else
                throw new RuntimeException("Элемент неизвестного типа");
        }
    }

    private static void setButtonAccessibility(Button button, List<Control> controls) {
        boolean disableButton = false;
        for (Control control : controls) {
            if (control instanceof TextField && ((TextField) control).getText().trim().equals("")) {
                disableButton = true;
            } else if (control instanceof TextArea && ((TextArea) control).getText().trim().equals("")) {
                disableButton = true;
            } else if (control instanceof ComboBox && ((ComboBox) control).getValue() == null) {
                disableButton = true;
            } else if (control instanceof DatePicker && ((DatePicker) control).getValue() == null) {
                disableButton = true;
            }
            button.setDisable(disableButton);
        }
    }
}