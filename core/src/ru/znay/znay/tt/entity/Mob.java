package ru.znay.znay.tt.entity;

import ru.znay.znay.tt.ai.Order;

/**
 * Created by admin on 06.07.2016.
 */
public class Mob extends Entity {
    public float rotSpeed = 0.02f;
    public float walkSpeed = 0.23f;
    public Order order = Order.idle;
    public int health;
    public int maxHealth;

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
}
