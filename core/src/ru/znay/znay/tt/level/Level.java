package ru.znay.znay.tt.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
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

    public int floorSprite = 4;
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
                decorateBlock(x, z, col);
            }
        }
    }

    public void decorateBlock(int x, int y, int col) {
        if (col == 0x0000FF) {
            xSpawn = x;
            ySpawn = y;
        }
    }

    public Block getBlock(int col) {
        if (col == 0x00FF00) return new GrassBlock();
        if (col == 0xFFFFFF) return new WallBlock();
        if (col == 0x008C00) return new TreeBlock();
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
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                Block b = blocks[x + z * w];
                v.set(x * 16, 0, z * 16);
                if (!camera.frustum.boundsInFrustum(v, dim)) continue;
                pb.setColor(b.r, b.g, b.b, b.a);
                if (b.solidRender) {
                    int sx = (b.sprite % 4) * 16;
                    int sy = (b.sprite / 4) * 16;
                    pb.addPlane(x * 16, 0, z * 16, 16, ModelData.rawDataFrontOut, sx, sy, 16, 16);
                    pb.addPlane(x * 16, 0, z * 16, 16, ModelData.rawDataBackOut, sx, sy, 16, 16);
                    pb.addPlane(x * 16, 0, z * 16, 16, ModelData.rawDataRightOut, sx, sy, 16, 16);
                    pb.addPlane(x * 16, 0, z * 16, 16, ModelData.rawDataLeftOut, sx, sy, 16, 16);

                   // pb.addPlane(x * 16, 0, z * 16, 16, ModelData.rawDataTopOut, sx, sy, 16, 16);
                } else {

                    if (b.floorSprite != -1) {
                        int sx = (b.floorSprite % 4) * 16;
                        int sy = (b.floorSprite / 4) * 16;
                        pb.addPlane(x * 16, 0, z * 16, 16, ModelData.rawDataBottomIn, sx, sy, 16, 16);
                    }

                    if (b.ceilSprite != -1) {
                        int sx = (b.ceilSprite % 4) * 16;
                        int sy = (b.ceilSprite / 4) * 16;
                        pb.addPlane(x * 16, 0, z * 16, 16, ModelData.rawDataTopIn, sx, sy, 16, 16);
                    }
                }
            }
        }
        pb.renderAndReset(camera, new Matrix4());
    }

    public void renderSprites(Camera camera, SpriteBatch3D spriteBatch3D) {
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {

                Block b = blocks[x + z * w];
                for (Entity e : b.entities) {
                    for (Sprite3D s : e.sprites) {
                        s.addSprite(e.x, e.y, e.z, camera, spriteBatch3D);
                    }
                }
                for (Sprite3D s : b.sprites) {
                    s.addSprite(x * 16, 0, z * 16, camera, spriteBatch3D);
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
}
