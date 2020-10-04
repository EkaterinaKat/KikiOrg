package com.katyshevtseva.kikiorg;


import com.katyshevtseva.kikiorg.view.controller.MainController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder.FxController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main extends Application {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FxController mainController = new MainController();
        OrganizerWindowCreator.getInstance().openMainWindow(mainController);
    }
}
