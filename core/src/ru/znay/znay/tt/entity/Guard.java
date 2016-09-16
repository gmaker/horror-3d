package ru.znay.znay.tt.entity;

import ru.znay.znay.tt.ai.MoveOrder;
import ru.znay.znay.tt.ai.Order;
import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.particle.SpawnParticle;

/**
 * Created by admin on 03.07.2016.
 */
public class Guard extends Mob {
    private Sprite3D sprite;
    private int tickTime = 0;
    private int[] steps = new int[]{0, 1, 0, 2};

    public Guard(float x, float y, float z) {
        super(x, y, z);
        sprite = new Sprite3D(0, 0, 0, 32, 0, 16, 16, -8, -8);
        sprites.add(sprite);
    }

    public void tick() {
        super.tick();
        move();
        float friction = 0.6f;
        xa *= friction;
        za *= friction;
        tickTime++;
        int frame = steps[(int) movement / 10 % steps.length];
        sprite.set(32 + frame * 16, 0, 16, 16);
    }


    protected Order getNextOrder() {
        Order order = Order.idle;
        if (Math.random() < 0.2) {
            //order = new MoveOrder(x + (float) (Math.random() * 2.0 - 1.0) * 8.0f, z + (float) (Math.random() * 2.0 - 1.0) * 8.0f);
        }
        return order;
    }
}
