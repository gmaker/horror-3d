package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.level.Level;

/**
 * Created by admin on 28.06.2016.
 */
public class GrassBlock extends Block {
    public GrassBlock(Level level, int xt, int zt) {
        super(level, xt, zt);
        floorSprite = 6 * 8 + random.nextInt(3);
    }
}
