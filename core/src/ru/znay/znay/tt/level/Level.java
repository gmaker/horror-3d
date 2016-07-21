package ru.znay.znay.tt.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.GoldMiner;
import ru.znay.znay.tt.entity.Enemy;
import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.entity.Player;
import ru.znay.znay.tt.entity.ore.Ore;
import ru.znay.znay.tt.gfx.*;
import ru.znay.znay.tt.gfx.light.Light;
import ru.znay.znay.tt.level.block.*;
import ru.znay.znay.tt.particle.Particle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 25.06.2016.
 */
public class Level {
    public final int w;
    public final int h;
    public final GoldMiner game;
    public Block[] blocks;
    public Block solidWall = new WaterBlock(null, -1, -1);
    public List<Entity> entities = new ArrayList<Entity>();
    public List<Light> lights = new ArrayList<Light>();
    public List<Particle> particles = new ArrayList<Particle>();


    private static Vector3[] colors = {
            new Vector3(1.0f, 1.0f, 1.0f),
            new Vector3(1f, 0.9f, 0.9f),
            new Vector3(1f, 0.5f, 0.5f),
            new Vector3(1f, 0.2f, 0.2f),
    };

    private static int[] floorSprite = {
            1 * 8,
            1 * 8 + 3
    };
    private static int[] ceilSprite = {
            -1,
            2 * 8
    };
    private static Level[] levels = new Level[floorSprite.length];


    public int tickTime = 0;
    public int xSpawn;
    public int ySpawn;
    public final int level;

    private Level(GoldMiner game, int w, int h, int level) {
        this.game = game;
        this.w = w;
        this.h = h;
        this.level = level;
        this.blocks = new Block[w * h];
    }

    public static Level loadLevel(GoldMiner game, int level) {
        if (Level.levels[Math.abs(level)] != null) {
            return Level.levels[Math.abs(level)];
        }
        Pixmap pixmap = Art.i.getPixmap("levels/" + level + ".png");
        int w = pixmap.getWidth();
        int h = pixmap.getHeight();
        Level result = new Level(game, w, h, level);

        int floorSprite = Level.floorSprite[Math.abs(level)];
        int ceilSprite = Level.ceilSprite[Math.abs(level)];

        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int col = (pixmap.getPixel(x, z) >> 8) & 0xFFFFFF;
                Block b = result.createBlock(x, z, col);
                if (b.floorSprite == -1) b.floorSprite = floorSprite;
                if (b.ceilSprite == -1) b.ceilSprite = ceilSprite;
                result.blocks[x + z * w] = b;
            }
        }

        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int col = (pixmap.getPixel(x, z) >> 8) & 0xFFFFFF;
                result.decorateBlock(x, z, col, result.getBlock(x, z));
            }
        }
        result.loadDecoration();
        Level.levels[Math.abs(level)] = result;
        return result;
    }

    public void removeEntityImmediately(Player player) {
        entities.remove(player);
        getBlock(player.xTileO, player.zTileO).removeEntity(player);
    }

    public void saveDecoration() {
        try {
            byte[] data = new byte[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int i = x + y * w;
                    int sprite = -1;
                    if (blocks[i] instanceof WallBlock) {
                        sprite = blocks[i].sprite;
                    }
                    data[i] = (byte) sprite;
                }
            }
            FileHandle dataFolder = Gdx.files.internal("data");
            FileHandle dataFile = Gdx.files.absolute(dataFolder.file().getAbsolutePath() + File.separator + "decoration_" + level + ".dat");
            dataFile.writeBytes(data, 0, data.length, false);
        } catch (Throwable e) {
            System.err.println(e);
        }
    }

    public void loadDecoration() {
        try {
            FileHandle dataFile = Gdx.files.internal("data/decoration_" + level + ".dat");
            byte[] data = dataFile.readBytes();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int i = x + y * w;
                    if (data[i] >= 0 && blocks[i] instanceof WallBlock) {
                        blocks[i].sprite = (int) data[i];
                    }
                }
            }
        } catch (Throwable e) {
            System.err.println(e);
        }

    }

    public void decorateBlock(int x, int z, int col, Block b) {
        b.decorate(this, x, z);
        if (col == 0x0000FF) {
            xSpawn = x;
            ySpawn = z;
        }
        if (col == 0xFF0000) addEntity(new Enemy(x * 16, 0, z * 16));
        if (col == 0x808080) addEntity(new Ore(x * 16, 0, z * 16));
    }

    public Block createBlock(int x, int z, int col) {
        if (col == 0x00FF00) return new GrassBlock(this, x, z);
        if (col == 0xFFFFFF) return new WallBlock(this, x, z);
        if (col == 0x008C00) return new TreeBlock(this, x, z);
        if (col == 0xFF7921) return new TorchBlock(this, x, z);
        if (col == 0x821C1C) return new FireBlock(this, x, z);
        if ((col & 0xFF00FF) == 0xFF00FF && ((col >> 8) & 0xFF) < 0x80) return new StairBlock(this, x, z, false, (col >> 8) & 0xff);
        if ((col & 0xFF00FF) == 0xFF00FF && ((col >> 8) & 0xFF) > 0x80) return new StairBlock(this, x, z, true, 128 - ((col >> 8) & 0xff));
        if (col == 0x00A08D) return new WaterBlock(this, x, z);
        return new Block(this, x, z);
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
        Vector3 levelColor = colors[Math.abs(level)];
        if (c.solidRender) {
            pb.setColor(c.r * levelColor.x, c.g * levelColor.y, c.b * levelColor.z, c.a);
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
                pb.setColor(e.r * levelColor.x, e.g * levelColor.y, e.b * levelColor.z, e.a);
                int sx = (e.sprite % 8) * 16;
                int sy = (e.sprite / 8) * 16;
                pb.addPlane(xb * 16, 0, zb * 16, 16, ModelData.rawDataRightIn, sx, sy, 16, 16);
            }
            if (s.solidRender) {
                pb.setColor(s.r * levelColor.x, s.g * levelColor.y, s.b * levelColor.z, s.a);
                int sx = (s.sprite % 8) * 16;
                int sy = (s.sprite / 8) * 16;
                pb.addPlane(xb * 16, 0, zb * 16, 16, ModelData.rawDataFrontIn, sx, sy, 16, 16);
            }

            pb.setColor(c.r * levelColor.x, c.g * levelColor.y, c.b * levelColor.z, c.a);
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

    public void switchLevel(int nextLevel) {
        game.switchLevel(level, nextLevel);
    }

    public void findSpawn(int level) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Block b = blocks[x + y * w];
                if (b instanceof StairBlock && ((StairBlock) b).nextLevel == level) {
                    xSpawn = x;
                    ySpawn = y;
                }
            }
        }
    }
}
