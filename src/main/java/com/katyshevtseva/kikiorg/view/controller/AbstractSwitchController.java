package com.katyshevtseva.kikiorg.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSwitchController {
    protected List<Button> buttons = new ArrayList<>();
    protected Pane pane;

    private void activateButton(Button buttonToActivate) {
        for (Button button : buttons) {
            button.setDisable(false);
        }
        buttonToActivate.setDisable(true);
    }

    protected void activateMode(Button button, Node node, NodeSupplier nodeSupplier, FxController fxController) {
        activateButton(button);
        if (node == null)
            node = nodeSupplier.getNode(fxController);
        pane.getChildren().clear();
        pane.getChildren().add(node);
    }


    @FunctionalInterface
    public interface NodeSupplier {
        Node getNode(FxController fxController);
    }

}