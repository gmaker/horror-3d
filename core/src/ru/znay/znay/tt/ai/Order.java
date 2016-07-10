package ru.znay.znay.tt.ai;

import ru.znay.znay.tt.entity.Mob;

/**
 * Created by admin on 06.07.2016.
 */
public abstract class Order {
    public static Order idle = new IdleOrder();

    public Mob owner;

    public void init(Mob owner) {
        this.owner = owner;
    }

    public abstract void tick();

    public abstract boolean isCompleted();
}
