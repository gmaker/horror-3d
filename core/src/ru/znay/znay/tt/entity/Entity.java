package ru.znay.znay.tt.entity;

import com.badlogic.gdx.graphics.Camera;
import ru.znay.znay.tt.entity.item.Item;
import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.gfx.SpriteBatch3D;
import ru.znay.znay.tt.level.Level;
import ru.znay.znay.tt.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 28.06.2016.
 */
public class Entity {
    public static final Random random = new Random();
    public List<Sprite3D> sprites = new ArrayList<Sprite3D>();
    public float x, y, z;
    public float xa = 0, ya = 0, za = 0;
    public float rot = 0.0f;
    public float rotA = 0.0f;
    public Level level;
    public float r = 4;
    public float alpha = 1.0f;
    public boolean removed = false;
    public int xTileO = -1;
    public int zTileO = -1;
    public float movement = 0.0f;

    public Entity(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    protected void move() {
        int xSteps = (int) (Math.abs(xa * 100) + 1);
        for (int i = xSteps; i > 0; i--) {
            float xxa = xa;
            if (isFree(x + xxa * i / xSteps, z)) {
                x += xxa * i / xSteps;
                movement += Math.abs(xxa * i / xSteps);
                break;
            } else {
                xa = 0;
            }
        }

        int zSteps = (int) (Math.abs(za * 100) + 1);
        for (int i = zSteps; i > 0; i--) {
            float zza = za;
            if (isFree(x, z + zza * i / zSteps)) {
                z += zza * i / zSteps;
                movement += Math.abs(zza * i / zSteps);
                break;
            } else {
                za = 0;
            }
        }
    }

    protected boolean isFree(float xx, float yy) {
        int x0 = (int) (Math.floor(xx + 8.0f - r) / 16.0);
        int x1 = (int) (Math.floor(xx + 8.0f + r) / 16.0);
        int y0 = (int) (Math.floor(yy + 8.0f - r) / 16.0);
        int y1 = (int) (Math.floor(yy + 8.0f + r) / 16.0);

        if (level.getBlock(x0, y0).blocks(this)) return false;
        if (level.getBlock(x1, y0).blocks(this)) return false;
        if (level.getBlock(x0, y1).blocks(this)) return false;
        if (level.getBlock(x1, y1).blocks(this)) return false;

        int xc = (int) (Math.floor(xx + 8.0f) / 16.0f);
        int zc = (int) (Math.floor(yy + 8.0f) / 16.0f);
        int rr = 2;
        for (int z = zc - rr; z <= zc + rr; z++) {
            for (int x = xc - rr; x <= xc + rr; x++) {
                List<Entity> es = level.getBlock(x, z).entities;
                for (int i = 0; i < es.size(); i++) {
                    Entity e = es.get(i);
                    if (e == this) continue;

                    if (!e.blocks(this, this.x, this.z, r) && e.blocks(this, xx, yy, r)) {
                        e.collide(this);
                        this.collide(e);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void collide(Entity entity) {

    }

    public boolean blocks(Entity entity, float x2, float z2, float r2) {
        if (x + r <= x2 - r2) return false;
        if (x - r >= x2 + r2) return false;

        if (z + r <= z2 - r2) return false;
        if (z - r >= z2 + r2) return false;

        return true;
    }

    public void tick() {

    }

    public void init(Level level) {
        this.level = level;
    }

    public void updatePos() {
        int xTile = (int) ((x + 8.0f) / 16.0f);
        int zTile = (int) ((z + 8.0f) / 16.0f);

        if (xTile != xTileO || zTile != zTileO) {
            level.getBlock(xTileO, zTileO).removeEntity(this);
            xTileO = xTile;
            zTileO = zTile;
            if (!removed) level.getBlock(xTileO, zTileO).addEntity(this);
        }
    }

    public void remove() {
        level.getBlock(xTileO, zTileO).removeEntity(this);
        removed = true;
    }

    public float distanceToSqr(Entity e) {
        float xd = e.x - x;
        float yd = e.y - y;
        return xd * xd + yd * yd;
    }

    public void interactWith(Player player, Item item, float xx, int yy, float zz) {

    }

    public void render(Camera camera, SpriteBatch3D sb) {
        for (Sprite3D sprite : sprites) {
            sprite.alpha(alpha);
            sprite.addSprite(x, y, z, camera, sb);
        }
    }
}
