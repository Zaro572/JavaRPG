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

    public EnemyComponent(Entity player) {
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

            double speed = 100; // pixels per second
            entity.translate(dx * speed * tpf, dy * speed * tpf);
        }
    }
}