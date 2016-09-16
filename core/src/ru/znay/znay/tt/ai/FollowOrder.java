package ru.znay.znay.tt.ai;

import ru.znay.znay.tt.entity.Mob;

/**
 * Created by admin on 09.09.2016.
 */
public class FollowOrder extends Order {

    public final Mob target;

    public FollowOrder(Mob target) {
        this.target = target;
    }

    public void tick() {
        float xd = target.x - owner.x;
        float zd = target.z - owner.z;

    }

    public boolean isCompleted() {
        return false;
    }
}

