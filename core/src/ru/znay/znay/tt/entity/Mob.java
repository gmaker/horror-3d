package ru.znay.znay.tt.entity;

import com.badlogic.gdx.graphics.Camera;
import ru.znay.znay.tt.ai.Order;
import ru.znay.znay.tt.gfx.SpriteBatch3D;
import ru.znay.znay.tt.particle.SpawnParticle;

/**
 * Created by admin on 06.07.2016.
 */
public class Mob extends Entity {
    public float rotSpeed = 0.02f;
    public float walkSpeed = 0.23f;
    public Order order = Order.idle;
    public int health;
    public int maxHealth;
    public int spawnTime = 2 * 60;

    public Mob(float x, float y, float z) {
        super(x, y, z);
        health = maxHealth = 10;
        health -= 3;
    }

    public final void setOrder(Order order) {
        this.order = order;
        this.order.init(this);
    }

    public void tick() {
        if (spawnTime > 0) {
            spawnTime--;
            if (random.nextDouble() < 0.3) {
                level.addParticle(new SpawnParticle(x, -7, z, 16, 2 * 60 + 30));
            }
        }
        if (order != null) {
            order.tick();
            if (order.isCompleted()) {
                setOrder(getNextOrder());
            }
        }
    }

    protected Order getNextOrder() {
        return Order.idle;
    }


    public void render(Camera camera, SpriteBatch3D sb) {
        if (spawnTime > 40) return;
        alpha = 1.0f - spawnTime / 40.0f;
        super.render(camera, sb);
    }
}
