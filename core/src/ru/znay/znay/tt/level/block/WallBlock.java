package ru.znay.znay.tt.level.block;

/**
 * Created by admin on 29.06.2016.
 */
public class WallBlock extends SolidBlock {
    public WallBlock(int xt, int zt) {
        super(xt, zt);
        sprite = 0;
        if (random.nextDouble() > 0.8) {
            if (random.nextDouble() > 0.7) {
                sprite = 1;
            } else {
                sprite = 2;
            }
        }
    }
}
