package ru.znay.znay.tt.particle;

/**
 * Created by admin on 09.09.2016.
 */
public class SpawnParticle extends Particle {
    private float radius = 2.1f;
    private final float maxRadius;
    private float rot = (float) (random.nextFloat() * Math.PI * 2.0f);
    private float rotA = random.nextFloat() * 0.5f + 0.3f;

    public SpawnParticle(float x, float y, float z, float maxRadius, int spawnTime) {
        super(x, y, z);
        xa *= 0.005;
        ya *= 0.005;
        za *= 0.005;
        gravity = -0.005f;
        this.maxRadius = maxRadius;
        this.timeLife = maxTimeLife = spawnTime;

    }

    public void tick() {
        super.tick();

        rot += rotA;

        float p = timeLife / (float) maxTimeLife;
        float r = radius + (maxRadius - radius) * p;

        x += (float) Math.cos(rot) * r * 0.1f;
        z += (float) Math.sin(rot) * r * 0.1f;

        sprite.alpha(p);
        float sz = (1.0f - p) * 2.0f;
        sprite.set(0, 8 * 16, sz, sz);
    }

}
