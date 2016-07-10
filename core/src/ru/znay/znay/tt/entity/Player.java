package ru.znay.znay.tt.entity;

import ru.znay.znay.tt.ai.MoveOrder;
import ru.znay.znay.tt.ai.Order;

/**
 * Created by admin on 03.07.2016.
 */
public class Player extends Mob {
    public float slope = 0f;
    public float bob = 0.0f;
    public float bobPhase = 0.0f;

    public Player(float x, float y, float z) {
        super(x, y, z);
        rot = (float) -Math.PI / 2.0f;
    }

    public void tick(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight) {
        if (turnLeft) rotA += rotSpeed;
        if (turnRight) rotA -= rotSpeed;

        float xm = 0;
        float zm = 0;
        if (up) zm--;
        if (down) zm++;
        if (left) xm--;
        if (right) xm++;
        float dd = xm * xm + zm * zm;
        if (dd > 0) {
            dd = (float)Math.sqrt(dd);
            bob += dd;
            bobPhase += dd;
        } else dd = 1;
        xm /= dd;
        zm /= dd;

        xa += (xm * Math.cos(rot) + zm * Math.sin(rot)) * walkSpeed;
        za += (zm * Math.cos(rot) - xm * Math.sin(rot)) * walkSpeed;

        move();

        float friction = 0.6f;
        xa *= friction;
        za *= friction;
        bob *= 0.6;
        rot += rotA;
        rotA *= 0.4;

        y = -0.2f + (float) Math.sin(bobPhase * 0.3) * 0.06f * bob;
    }
}
