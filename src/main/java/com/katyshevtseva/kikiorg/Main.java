package com.katyshevtseva.kikiorg;


import com.katyshevtseva.kikiorg.view.MainController;
import com.katyshevtseva.kikiorg.view.utils.OrganizerWindowCreator;
import com.katyshevtseva.kikiorg.view.utils.WindowBuilder;
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
        WindowBuilder.Controller mainController = new MainController();
        OrganizerWindowCreator.getInstance().openMainWindow(mainController);
    }
}
