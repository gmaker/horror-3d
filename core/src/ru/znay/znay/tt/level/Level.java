package ru.znay.znay.tt.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.entity.Enemy;
import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.gfx.*;
import ru.znay.znay.tt.gfx.light.Light;
import ru.znay.znay.tt.level.block.*;
import ru.znay.znay.tt.particle.Particle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 25.06.2016.
 */
public class Level {
    public final int w;
    public final int h;
    public Block[] blocks;
    public Block solidWall = new SolidBlock(-1, -1);
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Light> lights = new ArrayList<Light>();
    public List<Particle> particles = new ArrayList<Particle>();

    public int floorSprite = 1 * 8;
    public int ceilSprite = -1;

    public int tickTime = 0;
    public int xSpawn;
    public int ySpawn;

    public Level(Pixmap pixmap) {
        this.w = pixmap.getWidth();
        this.h = pixmap.getHeight();
        this.blocks = new Block[w * h];
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int col = (pixmap.getPixel(x, z) >> 8) & 0xFFFFFF;
                Block b = getBlock(x, z, col);
                if (b.floorSprite == -1) b.floorSprite = floorSprite;
                if (b.ceilSprite == -1) b.ceilSprite = ceilSprite;
                blocks[x + z * w] = b;
            }
        }

        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int col = (pixmap.getPixel(x, z) >> 8) & 0xFFFFFF;
                decorateBlock(x, z, col, getBlock(x, z));
            }
        }
    }

    public void decorateBlock(int x, int z, int col, Block b) {
        b.decorate(this, x, z);
        if (col == 0x0000FF) {
            xSpawn = x;
            ySpawn = z;
        }
        if (col == 0xFF0000) {
            addEntity(new Enemy(x * 16, 0, z * 16));
        }
    }

    public Block getBlock(int x, int z, int col) {
        if (col == 0x00FF00) return new GrassBlock(x, z);
        if (col == 0xFFFFFF) return new WallBlock(x, z);
        if (col == 0x008C00) return new TreeBlock(x, z);
        if (col == 0xFF7921) return new TorchBlock(x, z);
        if (col == 0x821C1C) return new FireBlock(x, z);
        return new Block(x, z);
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.init(this);
        e.updatePos();
    }

    public void addParticle(Particle p) {
        particles.add(p);
        p.init(this);
        p.updatePos();
    }

    private Vector3 v = new Vector3();
    private Vector3 dim = new Vector3(16, 16, 16);

    public List<Light> buildAllToRender(Camera camera, PlaneBatch pb, SpriteBatch3D sb, int radius) {
        lights.clear();
        float xCam = camera.position.x - camera.direction.x * 0.3f;
        float yCam = camera.position.z - camera.direction.z * 0.3f;

        int xCenter = (int) (Math.floor(xCam / 16.0));
        int zCenter = (int) (Math.floor(yCam / 16.0));

        for (int zb = zCenter - radius; zb <= zCenter + radius; zb++) {
            for (int xb = xCenter - radius; xb <= xCenter + radius; xb++) {
                Block c = getBlock(xb, zb);
                Block e = getBlock(xb + 1, zb);
                Block s = getBlock(xb, zb + 1);

                if (c instanceof TorchBlock) {
                    lights.add(((TorchBlock) c).pointLight);
                }

                if (c instanceof FireBlock) {
                    lights.add(((FireBlock) c).pointLight);
                }

                for (Entity entity : c.entities) {
                    for (Sprite3D sprite : entity.sprites) {
                        sprite.addSprite(entity.x, entity.y, entity.z, camera, sb);
                    }
                }
                for (Sprite3D sprite : c.sprites) {
                    sprite.addSprite(xb * 16, 0, zb * 16, camera, sb);
                }

                for (Particle p : c.particles) {
                    p.sprite.addSprite(p.x, p.y, p.z, camera, sb);
                }

                //v.set(xb * 16, 0, zb * 16);
                //if (!camera.frustum.boundsInFrustum(v, dim)) continue;
                addBlockToRender(xb, zb, c, e, s, pb);

            }
        }
        return lights;
    }

    private void addBlockToRender(int xb, int zb, Block c, Block e, Block s, PlaneBatch pb) {
        if (c.solidRender) {
            pb.setColor(c.r, c.g, c.b, c.a);
            int sx = (c.sprite % 8) * 16;
            int sy = (c.sprite / 8) * 16;
            if (!e.solidRender) {
                pb.addPlane(xb * 16, 0, zb * 16, 16, ModelData.rawDataRightOut, sx, sy, 16, 16);
            }
            if (!s.solidRender) {
                pb.addPlane(xb * 16, 0, zb * 16, 16, ModelData.rawDataFrontOut, sx, sy, 16, 16);
            }
        } else {
            if (e.solidRender) {
                pb.setColor(e.r, e.g, e.b, e.a);
                int sx = (e.sprite % 8) * 16;
                int sy = (e.sprite / 8) * 16;
                pb.addPlane(xb * 16, 0, zb * 16, 16, ModelData.rawDataRightIn, sx, sy, 16, 16);
            }
            if (s.solidRender) {
                pb.setColor(s.r, s.g, s.b, s.a);
                int sx = (s.sprite % 8) * 16;
                int sy = (s.sprite / 8) * 16;
                pb.addPlane(xb * 16, 0, zb * 16, 16, ModelData.rawDataFrontIn, sx, sy, 16, 16);
            }

            pb.setColor(c.r, c.g, c.b, c.a);
            if (c.floorSprite != -1) {
                int sx = (c.floorSprite % 8) * 16;
                int sy = (c.floorSprite / 8) * 16;
                pb.addPlane(xb * 16, 0, zb * 16, 16, ModelData.rawDataBottomIn, sx, sy, 16, 16);
            }

            if (c.ceilSprite != -1) {
                int sx = (c.ceilSprite % 8) * 16;
                int sy = (c.ceilSprite / 8) * 16;
                pb.addPlane(xb * 16, 0, zb * 16, 16, ModelData.rawDataTopIn, sx, sy, 16, 16);
            }
        }
    }

    public Block getBlock(int xt, int zt) {
        if (xt < 0 || zt < 0 || xt >= w || zt >= h) {
            return solidWall;
        }
        return blocks[xt + zt * w];
    }

    public void tick() {
        tickTime++;
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
            e.updatePos();
            if (e.removed) {
                entities.remove(i--);
            }
        }

        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.tick();
            p.updatePos();
            if (p.removed) {
                particles.remove(i--);
            }
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                blocks[x + y * w].tick(this);
            }
        }
    }
}
