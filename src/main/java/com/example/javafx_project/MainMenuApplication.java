package com.example.javafx_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuApplication extends Application {
    private Scene scene;
    private VBox mainContentBox;
    private Button playButton;
    private Button exitButton;

    @Override
    public void start(Stage stage)  {
        draw(stage);
    }

    private void draw(Stage stage) {
        playButton = new Button("PLAY");
        exitButton = new Button("EXIT");
        mainContentBox = new VBox(playButton, exitButton);

        playButton.setPadding(new Insets(10));
        exitButton.setPadding(new Insets(10));
        mainContentBox.setPadding(new Insets(30));

        scene = new Scene(mainContentBox);
        stage.setScene(scene);
        stage.show();
    }
}
