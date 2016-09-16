package ru.znay.znay.tt.particle;

/**
 * Created by admin on 09.09.2016.
 */
public class FogParticle extends Particle {
    public FogParticle(float x, float y, float z) {
        super(x, y, z);
        xa *= 0.005;
        ya *= 0.005;
        za *= 0.005;
        gravity = -0.005f;
        this.timeLife = this.maxTimeLife = random.nextInt(10) + 60;
        sprite.set(1 * 16 + (8 * (random.nextInt(3) + 1)), 7 * 16, 8, 8);
    }

    public void tick() {
        super.tick();

    }


}
