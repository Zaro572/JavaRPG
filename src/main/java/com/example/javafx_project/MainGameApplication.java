package com.example.javafx_project;

import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.AudioType;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.LocalTimer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import com.almasb.fxgl.physics.CollisionHandler;
import java.util.Map;

/**
 * @author Zaro Kavelar
 * @author Isa Begovic
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
        settings.setTitle("Cool Game!!");
        settings.setVersion("1.0.0");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setMainMenuEnabled(true);
        //settings.setSceneFactory(new MainGameSceneFactory());
    }

    @Override
    protected void initGame() {
        Texture playerTexture = FXGL.getAssetLoader().loadTexture("aryan.png");

        playerTexture.resize(playerSize, playerSize);

        FXGL.getGameScene().setBackgroundRepeat("grass.png");

        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(0, 0)
                .viewWithBBox(playerTexture)
                .with(new CollidableComponent(true))
                .buildAndAttach();

        FXGL.entityBuilder()
                .type(EntityType.COIN)
                .at(500, 200)
                .viewWithBBox(new Circle(15, 15, 15, Color.YELLOW))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        FXGL.play("battle1.mp3");

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    @Override
    protected void initUI() {
        statsText = new Text();
        statsText.setTranslateX(50);
        statsText.setTranslateY(100);
        FXGL.getGameScene().addUINode(statsText);
        statsText.textProperty().bind(FXGL.getip("pixelsMoved").asString());
        statsText.setVisible(false);
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.W, "Move Up", this::moveUp);
        FXGL.onKey(KeyCode.S, "Move Down", this::moveDown);
        FXGL.onKey(KeyCode.A, "Move Left", this::moveLeft);
        FXGL.onKey(KeyCode.D, "Move Right", this::moveRight);

        FXGL.onKey(KeyCode.UP, "Move Up 2", this::moveUp);
        FXGL.onKey(KeyCode.DOWN, "Move Down 2", this::moveDown);
        FXGL.onKey(KeyCode.LEFT, "Move Left 2", this::moveLeft);
        FXGL.onKey(KeyCode.RIGHT, "Move Right 2", this::moveRight);

        FXGL.onKey(KeyCode.Q, "Toggle Stats", this::toggleStats);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {

            }
        });
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
        statsText.setVisible(!statsText.isVisible());
    }

    public static void main(String[] args) {
        MainGameApplication.launch(args);
    }

    public enum EntityType {
        PLAYER, COIN
    }



}
