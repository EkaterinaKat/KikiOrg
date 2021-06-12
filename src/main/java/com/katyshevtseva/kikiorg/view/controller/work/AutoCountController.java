package com.katyshevtseva.kikiorg.view.controller.work;

import com.katyshevtseva.general.NoArgsKnob;
import com.katyshevtseva.kikiorg.core.Core;
import com.katyshevtseva.kikiorg.core.sections.work.WorkArea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Date;

import static com.katyshevtseva.date.DateCorrector.getProperDate;
import static com.katyshevtseva.date.DateUtils.*;
import static com.katyshevtseva.fx.FxUtils.*;

public class AutoCountController {
    private AutoCountStage autoCountStage = AutoCountStage.NOT_STARTED;
    private Date startDate;
    private Date finishDate;
    private NoArgsKnob tableUpdateKnob;
    private int finalMinutesNum = 0;
    @FXML
    private Label dateLabel;
    @FXML
    private ComboBox<WorkArea> areaComboBox;
    @FXML
    private Label autoCountInfoLabel;
    @FXML
    private TextField minutesSubtractionTextField;
    @FXML
    private Button autoCountButton;

    @FXML
    private void initialize() {
        disableNonNumericChars(minutesSubtractionTextField);
        dateLabel.setText("Current date: " + READABLE_DATE_FORMAT.format(getProperDate()));
        autoCountButton.setOnAction(event -> autoCountButtonListener());
        associateButtonWithControls(autoCountButton, areaComboBox);
        setComboBoxItems(areaComboBox, WorkArea.values());
    }

    private enum AutoCountStage {
        NOT_STARTED, STARTED, MINUTES_SUBTRACTION, FINISHED
    }

    void setTableUpdateKnob(NoArgsKnob knob) {
        this.tableUpdateKnob = knob;
    }

    private void autoCountButtonListener() {
        switch (autoCountStage) {
            case NOT_STARTED:
                autoCountStage = AutoCountStage.STARTED;
                startDate = new Date();
                autoCountInfoLabel.setText("Started at " + READABLE_TIME_FORMAT.format(startDate));
                autoCountButton.setText("Finish");
                areaComboBox.setDisable(true);
                break;
            case STARTED:
                autoCountStage = AutoCountStage.MINUTES_SUBTRACTION;
                finishDate = new Date();
                autoCountInfoLabel.setText(autoCountInfoLabel.getText() +
                        "\nFinished at " + READABLE_TIME_FORMAT.format(finishDate) +
                        "\nTotal: " + getNumberOfMinutes(startDate, finishDate));
                autoCountButton.setText("Subtract minutes");
                minutesSubtractionTextField.setDisable(false);
                break;
            case MINUTES_SUBTRACTION:
                autoCountStage = AutoCountStage.FINISHED;
                finalMinutesNum = getNumberOfMinutes(startDate, finishDate) - Integer.parseInt(minutesSubtractionTextField.getText());
                autoCountInfoLabel.setText(autoCountInfoLabel.getText() +
                        String.format(" - %s = %d", minutesSubtractionTextField.getText(), finalMinutesNum));
                autoCountButton.setText("Save");
                minutesSubtractionTextField.setDisable(true);
                minutesSubtractionTextField.setText("0");
                break;
            case FINISHED:
                autoCountStage = AutoCountStage.NOT_STARTED;
                Core.getInstance().workService().saveOrSumWithExistingWorkLog(areaComboBox.getValue(), finalMinutesNum, getProperDate());
                tableUpdateKnob.execute();
                autoCountInfoLabel.setText("");
                autoCountButton.setText("Start");
                areaComboBox.setDisable(false);
        }
    }
}
