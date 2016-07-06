package ru.znay.znay.tt.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.entity.Enemy;
import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.gfx.*;
import ru.znay.znay.tt.level.block.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 25.06.2016.
 */
public class Level {
    public final int w;
    public final int h;
    public Block[] blocks;
    public Block solidWall = new SolidBlock();
    public List<Entity> entities = new ArrayList<Entity>();

    public int floorSprite = 1 * 8;
    public int ceilSprite = -1;

    public int xSpawn;
    public int ySpawn;

    public Level(Pixmap pixmap) {
        this.w = pixmap.getWidth();
        this.h = pixmap.getHeight();
        this.blocks = new Block[w * h];
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                int col = (pixmap.getPixel(x, z) >> 8) & 0xFFFFFF;
                Block b = getBlock(col);
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

    public void decorateBlock(int x, int y, int col, Block b) {
        b.decorate(this, x, y);
        if (col == 0x0000FF) {
            xSpawn = x;
            ySpawn = y;
        }
        if (col == 0xFF0000) {
            addEntity(new Enemy(x * 16, 0, y * 16));
        }
    }

    public Block getBlock(int col) {
        if (col == 0x00FF00) return new GrassBlock();
        if (col == 0xFFFFFF) return new WallBlock();
        if (col == 0x008C00) return new TreeBlock();
        if (col == 0xFF7921) return new TorchBlock();
        return new Block();
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.init(this);
        e.updatePos();
    }

    private Vector3 v = new Vector3();
    private Vector3 dim = new Vector3(16, 16, 16);

    public void renderBlocks(Camera camera, PlaneBatch pb) {
        float xCam = camera.position.x - camera.direction.x * 0.3f;
        float yCam = camera.position.z - camera.direction.z * 0.3f;

        int r = 6;

        int xCenter = (int) (Math.floor(xCam / 16.0));
        int zCenter = (int) (Math.floor(yCam / 16.0));

        for (int zb = zCenter - r; zb <= zCenter + r; zb++) {
            for (int xb = xCenter - r; xb <= xCenter + r; xb++) {
                Block c = getBlock(xb, zb);
                Block e = getBlock(xb + 1, zb);
                Block s = getBlock(xb, zb + 1);

                v.set(xb * 16, 0, zb * 16);
                if (!camera.frustum.boundsInFrustum(v, dim)) continue;

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
        }

        pb.renderAndReset(camera, new Matrix4());
    }

    public void renderSprites(Camera camera, SpriteBatch3D spriteBatch3D) {
        float xCam = camera.position.x - camera.direction.x * 0.3f;
        float yCam = camera.position.z - camera.direction.z * 0.3f;

        int r = 6;

        int xCenter = (int) (Math.floor(xCam / 16.0));
        int zCenter = (int) (Math.floor(yCam / 16.0));

        for (int zb = zCenter - r; zb <= zCenter + r; zb++) {
            for (int xb = xCenter - r; xb <= xCenter + r; xb++) {

                Block b = blocks[xb + zb * w];
                for (Entity e : b.entities) {
                    for (Sprite3D s : e.sprites) {
                        s.addSprite(e.x, e.y, e.z, camera, spriteBatch3D);
                    }
                }
                for (Sprite3D s : b.sprites) {
                    s.addSprite(xb * 16, 0, zb * 16, camera, spriteBatch3D);
                }
            }
        }
        spriteBatch3D.renderAndReset(new Matrix4());
    }

    public Block getBlock(int xt, int zt) {
        if (xt < 0 || zt < 0 || xt >= w || zt >= h) {
            return solidWall;
        }
        return blocks[xt + zt * w];
    }

    public void tick() {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
            e.updatePos();
            if (e.removed) {
                entities.remove(i--);
            }
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                blocks[x + y * w].tick();
            }
        }
    }
}
