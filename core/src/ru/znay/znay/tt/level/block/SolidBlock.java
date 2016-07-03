package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.entity.Entity;

/**
 * Created by admin on 28.06.2016.
 */
public class SolidBlock extends Block {
    public SolidBlock() {
        super();
        solidRender = true;
        blockMotion = true;
    }

    @Override
    public boolean blocks(Entity entity) {
        return true;
    }
}
