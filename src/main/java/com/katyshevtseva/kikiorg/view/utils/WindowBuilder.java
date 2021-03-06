package com.katyshevtseva.kikiorg.view.utils;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class WindowBuilder {
    private String fxmlName;
    private String title = "";
    private int width = 500;
    private int height = 500;
    private FxController controller;
    private boolean isModal = false;
    private String iconImagePath;
    private boolean stretchable = false;
    private EventHandler<WindowEvent> eventHandler;

    WindowBuilder(String fxmlName) {
        this.fxmlName = fxmlName;
    }

    WindowBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    WindowBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    WindowBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    WindowBuilder setController(FxController controller) {
        this.controller = controller;
        return this;
    }

    WindowBuilder setModal(boolean modal) {
        isModal = modal;
        return this;
    }

    public WindowBuilder setIconImagePath(String iconImagePath) {
        this.iconImagePath = iconImagePath;
        return this;
    }

    public WindowBuilder setStretchable(boolean stretchable) {
        this.stretchable = stretchable;
        return this;
    }

    WindowBuilder setOnWindowCloseEventHandler(EventHandler<WindowEvent> eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }

    void showWindow() {
        getStage().show();
    }

    Node getNode() {
        return getNodeByFxmlAndController();
    }

    private Stage getStage() {
        Stage stage = new Stage();
        stage.setTitle(title);
        if (!stretchable) {
            stage.setMinHeight(height);
            stage.setMaxHeight(height);
            stage.setMinWidth(width);
            stage.setMaxWidth(width);
        }
        stage.setScene(new Scene(getNodeByFxmlAndController(), width, height));
        if (iconImagePath != null)
            stage.getIcons().add(new Image(iconImagePath));
        if (isModal)
            stage.initModality(Modality.APPLICATION_MODAL);
        if (eventHandler != null)
            stage.setOnCloseRequest(eventHandler);
        return stage;
    }

    private Parent getNodeByFxmlAndController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
        if (controller != null)
            fxmlLoader.setController(controller);
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parent;
    }

    public interface FxController {

    }
}
