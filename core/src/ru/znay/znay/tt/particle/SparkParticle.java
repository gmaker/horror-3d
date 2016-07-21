package ru.znay.znay.tt.particle;

/**
 * Created by admin on 18.07.2016.
 */
public class SparkParticle extends Particle {
    public SparkParticle(float x, float y, float z) {
        super(x, y, z);
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
}
