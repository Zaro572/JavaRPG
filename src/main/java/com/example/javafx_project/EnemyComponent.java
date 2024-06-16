package com.example.javafx_project;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.ui.Position;
import javafx.geometry.Point2D;

public class EnemyComponent extends Component {
    private Entity player;
    private MainGameApplication app;

    public EnemyComponent(MainGameApplication app, Entity player) {
        this.app = app;
        this.player = player;
    }

    @Override
    public void onUpdate(double tpf) {
        Point2D enemyPos = entity.getPosition();
        Point2D playerPos = player.getPosition();

        double distance = enemyPos.distance(playerPos);

        if (distance > 0) {
            double dx = (playerPos.getX() - enemyPos.getX()) / distance;
            double dy = (playerPos.getY() - enemyPos.getY()) / distance;
            double speed = 100;
            double x = dx * speed * tpf;
            double y = dy * speed * tpf;
            Point2D pos = entity.getPosition().add(x, y);
            if (!app.checkForEntity(pos, EntityType.ZOMBIE, entity)) {
                entity.setPosition(pos);
            }
        }
    }
}