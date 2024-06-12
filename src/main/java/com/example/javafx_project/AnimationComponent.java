package com.example.javafx_project;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class AnimationComponent extends Component {
    private final int framesPerRow = 4;
    private final int frameWith = 32;
    private final int frameHeight = 48;
    private final double animationDuration = 1;

    private boolean isWalking = false;
    private boolean keepWalking = true;
    private Direction walkingDirection = Direction.DOWN;

    private AnimatedTexture texture;
    private AnimationChannel animIdle, animWalkDown, animWalkRight, animWalkUp, animWalkLeft;

    public AnimationComponent(String assetName) {
        animIdle = new AnimationChannel(FXGL.image(assetName), framesPerRow, frameWith, frameHeight, Duration.seconds(animationDuration), 0, 3);
        animWalkDown = new AnimationChannel(FXGL.image(assetName), framesPerRow, frameWith, frameHeight, Duration.seconds(animationDuration), 4, 7);
        animWalkRight = new AnimationChannel(FXGL.image(assetName), framesPerRow, frameWith, frameHeight, Duration.seconds(animationDuration), 8, 11);
        animWalkUp = new AnimationChannel(FXGL.image(assetName), framesPerRow, frameWith, frameHeight, Duration.seconds(animationDuration), 12, 15);
        animWalkLeft = new AnimationChannel(FXGL.image(assetName), framesPerRow, frameWith, frameHeight, Duration.seconds(animationDuration), 16, 19);

        texture = new AnimatedTexture(animIdle);

        texture.setOnCycleFinished(new Runnable() {
            @Override
            public void run() {
                keepWalking = true;
            }
        });
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);

    }

    @Override
    public void onUpdate(double tpf) {
        //System.out.println(tpf);

        switch (walkingDirection) {
            case UP:
                playAnimation(animWalkUp);
                break;
            case DOWN:
                playAnimation(animWalkDown);
                break;
            case LEFT:
                playAnimation(animWalkLeft);
                break;
            case RIGHT:
                playAnimation(animWalkRight);
                break;
        }
    }

    private void playAnimation(AnimationChannel channel) {
        if (texture.getAnimationChannel() != channel) {
            keepWalking = true;
        }
        System.out.println(isWalking);
        System.out.println(keepWalking);
        System.out.println();
        if (isWalking && keepWalking) {
            texture.playAnimationChannel(channel);
            isWalking = false;
            keepWalking = false;
        }
    }

    public void walk(Direction direction) {
        isWalking = true;
        walkingDirection = direction;
    }
}