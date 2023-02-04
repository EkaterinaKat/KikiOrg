package com.katyshevtseva.kikiorg;


import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.kikiorg.view.controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.katyshevtseva.kikiorg.view.utils.KikiOrgWindowCreator.windowCreator;

@SpringBootApplication
public class Main extends Application {

    public static void main(String[] args) {
        /* Spring должен запускаться перед Fx */
        SpringApplication.run(Main.class, args);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FxController mainController = new MainController();
        windowCreator().openMainWindow(mainController);
    }
}
