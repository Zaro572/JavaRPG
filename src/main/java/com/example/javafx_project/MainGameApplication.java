package com.example.javafx_project;

import eu.hansolo.toolboxfx.geom.Poi;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import org.jetbrains.annotations.NotNull;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.AudioType;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
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
import java.util.Map;
import java.util.Random;

/**
 * @author Zaro Kavelar, Isa Begovic
 * @version 2024.06.12
 */
public class MainGameApplication extends GameApplication {
    private final int playerSize = 24;
    private final int itemSize = 16;
    private final int step = 4;

    private Random random;

    private Entity player;

    private Font showgFont;

    private Text statsText;
    private Text hpText;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle(" JavaRPG ");
        settings.setVersion("1.0.0");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        //settings.setDeveloperMenuEnabled(true);
        //settings.setSceneFactory(new MainGameSceneFactory());
    }

    @Override
    protected void initGame() {
        random = new Random();
        showgFont = Font.loadFont("file:///C:/Windows/Fonts/SHOWG.ttf", 36);
        //showgFont = Font.loadFont("https://cdn.redj.me/fonts/SHOWG.ttf", 36);

        FXGL.getGameScene().setBackgroundRepeat("grass.png");

        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(0, 0)
                .viewWithBBox(getTexture("blank.png"))
                .with(new CollidableComponent(true))
                .with(new AnimationComponent("player.png"))
                .buildAndAttach();

        generateCoin();

        FXGL.getAudioPlayer().loopMusic(FXGL.getAssetLoader().loadMusic("field1.mp3"));

    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("coins", 0);
        vars.put("hp", 100);
        vars.put("speedBoostDuration", 0);
    }

    @Override
    protected void initUI() {
        statsText = new Text();
        statsText.setTranslateX(20);
        statsText.setTranslateY(40);
        statsText.setFont(showgFont);
        statsText.setFill(Color.DARKORANGE);
        FXGL.getGameScene().addUINode(statsText);
        statsText.textProperty().bind(FXGL.getip("coins").asString());

        hpText = new Text();
        hpText.setTranslateX(20);
        hpText.setTranslateY(80);
        hpText.setFont(showgFont);
        hpText.setFill(Color.DARKRED);
        FXGL.getGameScene().addUINode(hpText);
        hpText.textProperty().bind(FXGL.getip("hp").asString());
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
                FXGL.inc("coins", 1);
                int coinCounter = FXGL.geti("coins");
                coin.removeFromWorld();
                generateCoin();
                if (getChance(30)) generateBooster();
                if (getChance(30)) generateHealth();
                if (coinCounter % 10 == 0) generateEnemy();
                if (coinCounter % 50 == 0) generateCoin();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.SPEEDBOOST) {
            @Override
            protected void onCollisionBegin(Entity player, Entity boost) {
                FXGL.play("boost.wav");
                FXGL.set("speedBoostDuration", FXGL.geti("speedBoostDuration") + 500);
                boost.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.HEALTHBOOST) {
            @Override
            protected void onCollisionBegin(Entity player, Entity boost) {
                FXGL.play("heal.wav");
                FXGL.inc("hp", 10);
                boost.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.ZOMBIE) {
            @Override
            protected void onCollision(Entity player, Entity enemy) {
                FXGL.play("damage.wav");
                FXGL.set("hp", FXGL.geti("hp") - 1);
                checkHP();
            }
        });
    }

    private void checkHP() {
        if (FXGL.geti("hp") <= 0) {
            FXGL.getDialogService().showMessageBox("You died! Your final score was: " + FXGL.geti("coins"), () ->{
                FXGL.getAudioPlayer().stopAllSoundsAndMusic();
                FXGL.getGameController().gotoMainMenu();
            });
        }
    }

    private Texture getTexture(String name) {
        return getTexture(name, playerSize, playerSize);
    }

    private Texture getTexture(String name, int w, int h) {
        Texture texture = FXGL.getAssetLoader().loadTexture(name);
        texture.resize(w, h);
        return texture;
    }

    private int getRandomValue(int lowerBound, int upperBound) {
        return random.nextInt((upperBound - lowerBound) + 1) + lowerBound;
    }

    private boolean getChance(int chance) {
        return getRandomValue(0, chance) == 0;
    }

    private Point2D getRandomMapLocation(int constraint) {
        int posx = getRandomValue(constraint, FXGL.getGameScene().getAppWidth() - constraint);
        int posy = getRandomValue(constraint, FXGL.getGameScene().getAppHeight() - constraint);
        return new Point2D(posx, posy);
    }

    private void generateCoin() {
        Texture coinTexture = getTexture("coin.png");
        AnimatedTexture coinAnimatedTexture = new AnimatedTexture(new AnimationChannel(coinTexture.getImage(), Duration.seconds(1), 4));
        coinAnimatedTexture.loop();

        FXGL.entityBuilder()
            .type(EntityType.COIN)
            .at(getRandomMapLocation(itemSize))
            .viewWithBBox(coinAnimatedTexture)
            .with(new CollidableComponent(true))
            .buildAndAttach();
    }

    private void generateBooster() {
        Texture boosterTexture = getTexture("speedboost.png");
        AnimatedTexture boosterAnimatedTexture = new AnimatedTexture(new AnimationChannel(boosterTexture.getImage(), Duration.seconds(1), 4));
        boosterAnimatedTexture.loop();

        FXGL.entityBuilder()
                .type(EntityType.SPEEDBOOST)
                .at(getRandomMapLocation(itemSize))
                .viewWithBBox(boosterAnimatedTexture)
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    private void generateHealth() {
        Texture boosterTexture = getTexture("health.png");
        AnimatedTexture boosterAnimatedTexture = new AnimatedTexture(new AnimationChannel(boosterTexture.getImage(), Duration.seconds(1), 4));
        boosterAnimatedTexture.loop();

        FXGL.entityBuilder()
                .type(EntityType.HEALTHBOOST)
                .at(getRandomMapLocation(itemSize))
                .viewWithBBox(boosterAnimatedTexture)
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    private void generateEnemy() {
        FXGL.entityBuilder()
            .type(EntityType.ZOMBIE)
            .at(getRandomMapLocation(playerSize))
            .viewWithBBox(getTexture("blank.png"))
            .with(new CollidableComponent(true))
            .with(new AnimationComponent("zombie.png"))
            .with(new EnemyComponent(this, player))
            .buildAndAttach();
    }

    private int getStepSize() {
        if (FXGL.geti("speedBoostDuration") > 0) {
            FXGL.set("speedBoostDuration", FXGL.geti("speedBoostDuration") - 1);
            return step * 2;
        } else {
            return step;
        }
    }



    private void moveUp() {
        if (player.getY() > 0) {
            System.out.println("UP");
            player.translateY(getStepSize() * -1);
            player.getComponent(AnimationComponent.class).walk(Direction.UP);
        }
    }

    private void moveDown() {
        if (player.getY() + playerSize < FXGL.getSettings().getHeight()) {
            System.out.println("DOWN");
            player.translateY(getStepSize());
            player.getComponent(AnimationComponent.class).walk(Direction.DOWN);
        }
    }

    private void moveLeft() {
        if (player.getX() > 0) {
            System.out.println("LEFT");
            player.translateX(getStepSize() * -1);
            player.getComponent(AnimationComponent.class).walk(Direction.LEFT);
        }
    }

    private void moveRight() {
        if (player.getX() + playerSize < FXGL.getSettings().getWidth()) {
            System.out.println("RIGHT");
            player.translateX(getStepSize());
            player.getComponent(AnimationComponent.class).walk(Direction.RIGHT);
        }
    }

    public boolean checkForEntity(Point2D position, EntityType type, Entity self) {
        for (Entity e : FXGL.getGameWorld().getEntitiesByType(type)) {
            double distance = e.getPosition().distance(position);
            if ((distance < 20 || getChance(50)) && e != self) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        MainGameApplication.launch(args);
    }
}
