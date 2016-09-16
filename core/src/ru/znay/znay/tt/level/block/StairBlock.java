package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.entity.Player;
import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.level.Level;
import ru.znay.znay.tt.particle.FogParticle;

/**
 * Created by admin on 20.07.2016.
 */
public class StairBlock extends Block {
    public boolean wait;
    public int nextLevel;
    public final boolean down;

    public StairBlock(Level level, int xt, int zt, boolean down, int nextLevel) {
        super(level, xt, zt);
        this.nextLevel = level.level + nextLevel;
        this.down = down;
        if (down) {
            floorSprite = 6 * 16 + 4;
            sprites.add(new Sprite3D(0, 0, 0, 1 * 16, 4 * 16, 16, 16));
        } else {
            ceilSprite = 6 * 16 + 4;
            sprites.add(new Sprite3D(0, 0, 0, 2 * 16, 4 * 16, 16, 16));
        }
    }

    @Override
    public void decorate(Level level, int x, int y) {

    }

    public void tick(Level level) {
        super.tick(level);
        if (!down) return;
        if (random.nextDouble() < 0.06) {
            float xo = (random.nextFloat() * 2.0f - 1.0f) * 4.0f;
            float zo = (random.nextFloat() * 2.0f - 1.0f) * 4.0f;
            level.addParticle(new FogParticle(this.xt * 16 + xo, -2, this.zt * 16 + zo));
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
