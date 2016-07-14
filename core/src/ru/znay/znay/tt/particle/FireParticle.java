package ru.znay.znay.tt.particle;

/**
 * Created by admin on 12.07.2016.
 */
public class FireParticle extends Particle {
    public FireParticle(float x, float y, float z) {
        super(x, y, z);
        xa *= 0.005;
        ya *= 0.005;
        za *= 0.005;
        gravity = -0.015f;
        this.timeLife = this.maxTimeLife = random.nextInt(10) + 30;
        sprite.set(0, 7 * 16 + 8, 8, 8);
    }

    @Override
    public void tick() {
        super.tick();
        float p = 1.0f - timeLife / (float) maxTimeLife;
        int s = (int) (p * 3 + 0.5);
        sprite.set(s * 8, 7 * 16 + 8, 8, 8);
        sprite.color(sprite.r, sprite.g, sprite.b, 1.0f - p);
    }

    @Override
    public void remove() {
        super.remove();
        if (random.nextDouble() < 0.1) {
            level.addParticle(new SmokeParticle(x, y, z));
        }
    }
}
