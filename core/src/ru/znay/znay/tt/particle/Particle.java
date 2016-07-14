package ru.znay.znay.tt.particle;

import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.level.Level;

import java.util.Random;

/**
 * Created by admin on 11.07.2016.
 */
public class Particle {
    public static final Random random = new Random();
    public float x;
    public float y;
    public float z;
    public float xa = 0.0f;
    public float ya = 0.0f;
    public float za = 0.0f;
    public boolean removed = false;
    public Sprite3D sprite = new Sprite3D(0, 0, 0, 0, 7 * 16, 8, 8);
    public float gravity = 0.04f;
    public float bounce = 0.4f;
    public Level level;
    public int xTileO = -1;
    public int zTileO = -1;
    public int timeLife;
    public int maxTimeLife;

    public Particle(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        do {
            xa = random.nextFloat() * 2.0f - 1.0f;
            ya = random.nextFloat();
            za = random.nextFloat() * 2.0f - 1.0f;
        } while (xa * xa + ya * ya + za * za > 1.0);

        this.timeLife = this.maxTimeLife = random.nextInt(30) + 40;
    }

    public void init(Level level) {
        this.level = level;
    }

    public void tick() {
        if (timeLife-- <= 0) {
            remove();
            return;
        }
        x += xa;
        y += ya;
        z += za;

        xa *= 0.97;
        ya *= 0.97;
        ya *= 0.97;
        ya -= gravity;
        if (y < -8) {
            y = -8;
            xa *= 0.66;
            ya *= -bounce;
            za *= 0.66;
        }
    }

    public void updatePos() {
        int xTile = (int) (x / 16);
        int zTile = (int) (z / 16);

        if (xTile != xTileO || zTile != zTileO) {
            level.getBlock(xTileO, zTileO).removeParticle(this);
            xTileO = xTile;
            zTileO = zTile;
            if (!removed) level.getBlock(xTileO, zTileO).addParticle(this);
        }
    }

    public void remove() {
        level.getBlock(xTileO, zTileO).removeParticle(this);
        removed = true;
    }
}
