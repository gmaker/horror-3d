package ru.znay.znay.tt.particle;

/**
 * Created by admin on 11.07.2016.
 */
public class SmokeParticle extends Particle {
    public SmokeParticle(float x, float y, float z) {
        super(x, y, z);
        xa *= 0.05;
        ya *= 0.05;
        za *= 0.05;
        gravity = -0.009f;
        timeLife = maxTimeLife *= 0.5;
        int s = random.nextInt(3);
        sprite.set(s * 8, 7 * 16, 8, 8);
    }

    public void tick() {
        super.tick();
        sprite.color(sprite.r, sprite.g, sprite.b, (timeLife / (float) maxTimeLife));
    }
}
