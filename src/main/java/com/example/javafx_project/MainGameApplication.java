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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import com.almasb.fxgl.physics.CollisionHandler;
import java.util.Map;
import java.util.Random;

/**
 * @author Zaro Kavelar
 * @author Isa Begovic
 * @version 19.04.2024
 */
public class MainGameApplication extends GameApplication {
    private final int playerSize = 32;
    private final int step = 4;

    private Random random;

    private Entity player;

    private Text statsText;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle(" JavaRPG ");
        settings.setVersion("1.0.0");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setMainMenuEnabled(true);
        //settings.setSceneFactory(new MainGameSceneFactory());
    }

    @Override
    protected void initGame() {
        random = new Random();

        FXGL.getGameScene().setBackgroundRepeat("grass.png");

        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(0, 0)
                .viewWithBBox(getTexture("aryan.png"))
                .with(new CollidableComponent(true))
                .buildAndAttach();

        generateCoin();

        FXGL.getAudioPlayer().loopMusic(FXGL.getAssetLoader().loadMusic("field1.mp3"));

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("coins", 0);
    }

    @Override
    protected void initUI() {
        statsText = new Text();
        statsText.setTranslateX(20);
        statsText.setTranslateY(40);
        statsText.setFont(Font.loadFont("https://cdn.redj.me/fonts/SHOWG.ttf", 36));
        statsText.setFill(Color.DARKORANGE);
        FXGL.getGameScene().addUINode(statsText);
        statsText.textProperty().bind(FXGL.getip("coins").asString());
        //statsText.setVisible(false);
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

        //FXGL.onKey(KeyCode.Q, "Toggle Stats", this::toggleStats);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                FXGL.play("coin.wav");
                FXGL.set("coins", FXGL.geti("coins") + 1);
                coin.removeFromWorld();
                generateCoin();
            }
        });
    }

    private Texture getTexture(String name) {
        return getTexture(name, playerSize, playerSize);
    }

    private Texture getTexture(String name, int w, int h) {
        Texture texture = FXGL.getAssetLoader().loadTexture(name);
        texture.resize(w, h);
        return texture;
    }

    private void generateCoin() {
        int posx = random.nextInt(FXGL.getGameScene().getAppWidth() - 32) + 16;
        int posy = random.nextInt(FXGL.getGameScene().getAppHeight() - 32) + 16;

        FXGL.entityBuilder()
            .type(EntityType.COIN)
            .at(posx, posy)
            .viewWithBBox(getTexture("coin.png"))
            .with(new CollidableComponent(true))
            .buildAndAttach();
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
        PLAYER,
        COIN
    }



}
