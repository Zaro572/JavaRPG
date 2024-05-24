package com.example.javafx_project;

import com.almasb.fxgl.app.FXGLPane;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.ui.FXGLButton;
import javafx.geometry.Pos;

import java.awt.*;

public class MainGameMenu extends FXGLMenu {
    public MainGameMenu(MenuType menuType) {
        super(menuType);

        FXGLButton playButton = new FXGLButton("PLAY!");
        FXGLButton exitButton = new FXGLButton("EXIT!");

        playButton.setAlignment(Pos.CENTER);
        exitButton.setAlignment(Pos.CENTER);

        getRoot().getChildren().add(playButton);
        getRoot().getChildren().add(exitButton);
    }
}
