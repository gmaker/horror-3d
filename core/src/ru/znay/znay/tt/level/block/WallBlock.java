package ru.znay.znay.tt.level.block;

/**
 * Created by admin on 29.06.2016.
 */
public class WallBlock extends SolidBlock {
    public WallBlock() {
        super();
        sprite = 0;
        if (random.nextDouble() < 0.05) sprite = 1;
    }
}
