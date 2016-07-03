package ru.znay.znay.tt.entity;

/**
 * Created by admin on 03.07.2016.
 */
public class Player extends Entity {
    public float slope = 0.02f;
    public float bob = 0.0f;
    public float bobPhase = 0.0f;
    public Player(float x, float y, float z) {
        super(x, y, z);
    }

    public void tick(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight) {
        double rotSpeed = 0.02;
        double walkSpeed = 0.23;

        if (turnLeft) rotA += rotSpeed;
        if (turnRight) rotA -= rotSpeed;

        double xm = 0;
        double zm = 0;
        if (up) zm--;
        if (down) zm++;
        if (left) xm--;
        if (right) xm++;
        double dd = xm * xm + zm * zm;
        if (dd > 0) {
            dd  = Math.sqrt(dd);
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

        y = -0.2f + (float)Math.sin(bobPhase * 0.3) * 0.06f * bob;
    }
}
