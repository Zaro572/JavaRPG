package com.example.javafx_project;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.AudioType;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.LocalTimer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Zaro Kavelar
 * @version 19.04.2024
 */
public class MainGameApplication extends GameApplication {
    private final int playerSize = 32;
    private final int step = 4;

    private boolean canMove = true;

    private Entity player;

    private Text statsText;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(600);
        settings.setTitle("Cool Game!!");
        settings.setVersion("1.0.0");


    }

    @Override
    protected void initGame() {
        Texture playerTexture = FXGL.getAssetLoader().loadTexture("aryan.png");

        playerTexture.resize(playerSize, playerSize);

        FXGL.getGameScene().setBackgroundRepeat("grass.png");

        player = FXGL.entityBuilder()
                .at(0, 0)
                .viewWithBBox(playerTexture)
                .buildAndAttach();

        FXGL.play("battle1.mp3");

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("distanceTraveled", 0);
    }

    @Override
    protected void initUI() {
        statsText = new Text();
        statsText.setTranslateX(50);
        statsText.setTranslateY(100);
        FXGL.getGameScene().addUINode(statsText);
        //statsText.textProperty().bind(getGameState().intProperty("pixelsMoved").asString());
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.W, this::moveUp);
        FXGL.onKey(KeyCode.S, this::moveDown);
        FXGL.onKey(KeyCode.A, this::moveLeft);
        FXGL.onKey(KeyCode.D, this::moveRight);

        FXGL.onKey(KeyCode.UP, this::moveUp);
        FXGL.onKey(KeyCode.DOWN, this::moveDown);
        FXGL.onKey(KeyCode.LEFT, this::moveLeft);
        FXGL.onKey(KeyCode.RIGHT, this::moveRight);

        FXGL.onKey(KeyCode.Q, this::toggleStats);
    }

    private void moveUp() {
        if (player.getY() > 0) {
            System.out.println("UP");
            player.translateY(step * -1);

        }
    }

    private void moveDown() {
        if (player.getY() + playerSize < FXGL.getSettings().getHeight()) {
            System.out.println("DOWN");
            player.translateY(step);
        }
    }

    private void moveLeft() {
        if (player.getX() > 0) {
            System.out.println("LEFT");
            player.translateX(step * -1);
        }
    }

    private void moveRight() {
        if (player.getX() + playerSize < FXGL.getSettings().getWidth()) {
            System.out.println("RIGHT");
            player.translateX(step);
        }
    }

    private void toggleStats() {

    }

    public static void main(String[] args) {
        MainGameApplication.launch(args);
    }
}
