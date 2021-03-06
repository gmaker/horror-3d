package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.level.Level;

/**
 * Created by admin on 28.06.2016.
 */
public abstract class SolidBlock extends Block {
    public SolidBlock(Level level, int xt, int zt) {
        super(level, xt, zt);
        solidRender = true;
    }

    @Override
    public boolean blocks(Entity entity) {
        return true;
    }
}
