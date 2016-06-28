package ru.znay.znay.tt.entity;

import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.level.Level;
import ru.znay.znay.tt.level.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 28.06.2016.
 */
public class Entity {
    public List<Sprite3D> sprites = new ArrayList<Sprite3D>();
    public float x, y, z;
    public Level level;
    public boolean removed = false;
    public int xTileO = -1;
    public int zTileO = -1;

    public Entity(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void init(Level level) {
        this.level = level;
    }

    public void updatePos() {
        int xTile = (int) (x / 16);
        int zTile = (int) (z / 16);

        if (xTile != xTileO || zTile != zTileO) {
            level.getBlock(xTileO, zTileO).removeEntity(this);
            xTileO = xTile;
            zTileO = zTile;
            if (!removed) level.getBlock(xTileO, zTileO).addEntity(this);
        }

    }
}
