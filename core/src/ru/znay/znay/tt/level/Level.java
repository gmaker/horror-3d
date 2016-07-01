package ru.znay.znay.tt.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.gfx.Cube;
import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.gfx.SpriteBatch3D;
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

    public Level(int w, int h) {
        this.w = w;
        this.h = h;
        this.blocks = new Block[w * h];
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                blocks[x + z * w] = new WallBlock();
                if (Math.random() < 0.89) {
                    if (Math.random() < 0.9) {
                        blocks[x + z * w] = new GrassBlock();
                    } else {
                        blocks[x + z * w] = new TreeBlock();
                    }
                }
            }
        }
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.init(this);
        e.updatePos();
    }

    public void renderBlocks(Camera camera, Cube cube) {
        for (int z = 0; z < h; z++) {
            for (int x = 0; x < w; x++) {
                Block b = blocks[x + z * w];
                if (b.solidRender) {
                    cube.setColor(b.r, b.g, b.b, b.a);
                    cube.render(camera, new Matrix4().translate(x * 16, 0, z * 16).scl(16.0f), b.sprite);
                }
            }
        }
    }

    public void renderSprites(PerspectiveCamera camera, SpriteBatch3D spriteBatch3D) {
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
