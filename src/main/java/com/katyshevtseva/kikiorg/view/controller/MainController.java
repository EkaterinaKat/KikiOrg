package com.katyshevtseva.kikiorg.view.controller;

import com.katyshevtseva.kikiorg.view.controller.finance.MainFinanceController;
import com.katyshevtseva.kikiorg.view.controller.habits.MainHabitsController;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.MainWardrobeController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainController extends AbstractSwitchController implements WindowBuilder.FxController {
    @FXML
    private Button habitsButton;
    @FXML
    private Button financeButton;
    @FXML
    private Button wardrobeButton;
    @FXML
    private Pane mainPane;
    @FXML
    private ImageView logoImageView;
    private final MainFinanceController mainFinanceController = new MainFinanceController();
    private final MainHabitsController mainHabitsController = new MainHabitsController();
    private final MainWardrobeController mainWardrobeController = new MainWardrobeController();
    private Node financeModeNode;
    private Node habitsModeNode;
    private Node wardrobeModeNode;

    @FXML
    private void initialize() {
        logoImageView.setImage(new Image("logo.png"));
        pane = mainPane;
        buttons.addAll(Arrays.asList(habitsButton, financeButton, wardrobeButton));
        financeButton.setOnAction(event -> financeButtonListener());
        habitsButton.setOnAction(event -> habitsButtonListener());
        wardrobeButton.setOnAction(event -> wardrobeButtonListener());
        financeButtonListener();
    }

    private void financeButtonListener() {
        activateMode(financeButton, financeModeNode,
                OrganizerWindowCreator.getInstance()::getMainFinanceNode, mainFinanceController);
    }

    private void habitsButtonListener() {
        activateMode(habitsButton, habitsModeNode,
                OrganizerWindowCreator.getInstance()::getMainHabitsNode, mainHabitsController);
    }

    private void wardrobeButtonListener() {
        activateMode(wardrobeButton, wardrobeModeNode,
                OrganizerWindowCreator.getInstance()::getMainWardrobeNode, mainWardrobeController);
    }
}
