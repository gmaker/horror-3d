package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.level.Level;
import ru.znay.znay.tt.particle.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 28.06.2016.
 */
public class Block {
    public static final Random random = new Random();
    public List<Sprite3D> sprites = new ArrayList<Sprite3D>();
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Particle> particles = new ArrayList<Particle>();
    public boolean blockMotion = false;
    public boolean solidRender = false;
    public int sprite = -1;
    public int floorSprite = -1;
    public int ceilSprite = -1;
    public float r = 1.0f;
    public float g = 1.0f;
    public float b = 1.0f;
    public float a = 1.0f;
    public final int xt;
    public final int zt;

    public Block(int xt, int zt) {
        this.xt = xt;
        this.zt = zt;
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public boolean blocks(Entity entity) {
        return false;
    }

    public void tick(Level level) {
        for (int i = 0; i < sprites.size(); i++) {
            Sprite3D sprite = sprites.get(i);
            sprite.tick();
            if (sprite.removed) {
                sprites.remove(i--);
            }
        }
    }

    public void decorate(Level level, int x, int y) {

    }

    public void removeParticle(Particle p) {
        particles.remove(p);
    }

    public void addParticle(Particle p) {
        particles.add(p);
    }
}
