package com.katyshevtseva.kikiorg.view.controller;

import com.katyshevtseva.fx.FxImageCreationUtil;
import com.katyshevtseva.fx.FxUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.fx.switchcontroller.AbstractSwitchController;
import com.katyshevtseva.fx.switchcontroller.Section;
import com.katyshevtseva.kikiorg.view.controller.finance.MainFinanceController;
import com.katyshevtseva.kikiorg.view.controller.habits.MainHabitsController;
import com.katyshevtseva.kikiorg.view.controller.structure.MainStructureController;
import com.katyshevtseva.kikiorg.view.controller.tracker.MainTrackerController;
import com.katyshevtseva.kikiorg.view.controller.wardrobe.MainWardrobeController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

import static com.katyshevtseva.fx.FxImageCreationUtil.getIcon;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.NodeInfo.*;
import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

public class MainController extends AbstractSwitchController implements FxController {
    @FXML
    private Pane mainPane;
    @FXML
    private ImageView logoImageView;
    @FXML
    private VBox buttonBox;

    @FXML
    private void initialize() {
        logoImageView.setImage(getIcon(FxImageCreationUtil.IconPicture.KIKI_ORG_LOGO));
        init(getSections(), mainPane, this::placeButton);
    }

    private List<Section> getSections() {
        return Arrays.asList(
                new Section("Finance", new MainFinanceController(),
                        controller -> windowCreator().getNode(MAIN_FIN, controller)),
                new Section("Habits", new MainHabitsController(),
                        controller -> windowCreator().getNode(MAIN_HABIT, controller)),
                new Section("Wardrobe", new MainWardrobeController(),
                        controller -> windowCreator().getNode(MAIN_WARDROBE, controller)),
                new Section("Tracker", new MainTrackerController(),
                        controller -> windowCreator().getNode(MAIN_TRACKER, controller)),
                new Section("Structure", new MainStructureController(),
                        controller -> windowCreator().getNode(MAIN_STRUCTURE, controller)));
    }

    private void placeButton(Button button) {
        FxUtils.setWidth(button, 180);
        buttonBox.getChildren().addAll(FxUtils.getPaneWithHeight(40), button);
    }
}
