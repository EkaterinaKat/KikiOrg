package com.katyshevtseva.kikiorg.view.controller.dialog;

import com.katyshevtseva.kikiorg.view.utils.Utils;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class QuestionDialogController implements FxController {
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;
    @FXML
    private Label questionLable;
    private final String question;
    private final AnswerHandler answerHandler;

    public QuestionDialogController(String question, AnswerHandler answerHandler) {
        this.question = question;
        this.answerHandler = answerHandler;
    }

    @FXML
    private void initialize() {
        questionLable.setText(question);
        yesButton.setOnAction(event -> handleAnswer(true));
        noButton.setOnAction(event -> handleAnswer(false));
    }

    private void handleAnswer(boolean b) {
        answerHandler.handle(b);
        Utils.closeWindowThatContains(questionLable);
    }

    @FunctionalInterface
    interface AnswerHandler {
        void handle(boolean b);
    }
}
