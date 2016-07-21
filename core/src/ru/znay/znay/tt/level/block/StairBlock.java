package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.entity.Player;
import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.level.Level;

/**
 * Created by admin on 20.07.2016.
 */
public class StairBlock extends Block {
    public boolean wait;
    public int nextLevel;

    public StairBlock(Level level, int xt, int zt, boolean down, int nextLevel) {
        super(level, xt, zt);
        this.nextLevel = level.level + nextLevel;
        if (down) {
            floorSprite = 6 * 8 + 4;
            sprites.add(new Sprite3D(0, 0, 0, 1 * 16, 4 * 16, 16, 16));
        } else {
            ceilSprite = 6 * 8 + 4;
            sprites.add(new Sprite3D(0, 0, 0, 2 * 16, 4 * 16, 16, 16));
        }
    }

    public void removeEntity(Entity e) {
        super.removeEntity(e);
        if (e instanceof Player) {
            wait = false;
        }
    }

    public void addEntity(Entity e) {
        super.addEntity(e);
        if (!wait && e instanceof Player) {
            level.switchLevel(nextLevel);
        }
    }
}
