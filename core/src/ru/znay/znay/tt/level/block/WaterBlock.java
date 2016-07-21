package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.level.Level;

/**
 * Created by admin on 21.07.2016.
 */
public class WaterBlock extends Block {
    public WaterBlock(Level level, int xt, int zt) {
        super(level, xt, zt);
        floorSprite = 5 + 1 * 8;
    }

    @Override
    public boolean blocks(Entity entity) {
        return true;
    }
}
