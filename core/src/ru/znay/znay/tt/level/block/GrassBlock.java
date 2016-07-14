package ru.znay.znay.tt.level.block;

/**
 * Created by admin on 28.06.2016.
 */
public class GrassBlock extends Block {
    public GrassBlock(int xt, int zt) {
        super(xt, zt);
        floorSprite = 1 * 8 + random.nextInt(3);
    }
}
