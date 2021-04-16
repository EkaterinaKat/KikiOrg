package com.katyshevtseva.kikiorg.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.controller.finance.MainFinanceController;
import com.katyshevtseva.kikiorg.view.controller.habits.MainHabitsController;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.MainWardrobeController;
import com.katyshevtseva.kikiorg.view.controller.work.MainWorkController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainController extends AbstractSwitchController implements FxController {
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
    @FXML
    private Button workButton;
    private final MainFinanceController mainFinanceController = new MainFinanceController();
    private final MainHabitsController mainHabitsController = new MainHabitsController();
    private final MainWardrobeController mainWardrobeController = new MainWardrobeController();
    private final MainWorkController mainWorkController = new MainWorkController();
    private Node financeModeNode;
    private Node habitsModeNode;
    private Node wardrobeModeNode;
    private Node workModeNode;

    @FXML
    private void initialize() {
        logoImageView.setImage(new Image("images/logo.png"));
        pane = mainPane;
        buttons.addAll(Arrays.asList(habitsButton, financeButton, wardrobeButton, workButton));
        financeButton.setOnAction(event -> financeButtonListener());
        habitsButton.setOnAction(event -> habitsButtonListener());
        wardrobeButton.setOnAction(event -> wardrobeButtonListener());
        workButton.setOnAction(event -> workButtonListener());
        financeButtonListener();
    }

    private void workButtonListener() {
        activateMode(workButton, workModeNode,
                OrganizerWindowCreator.getInstance()::getMainWorkNode, mainWorkController);
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
