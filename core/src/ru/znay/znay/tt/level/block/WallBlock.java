package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.entity.Player;
import ru.znay.znay.tt.entity.item.Item;
import ru.znay.znay.tt.level.Level;

/**
 * Created by admin on 29.06.2016.
 */
public class WallBlock extends SolidBlock {
    public WallBlock(Level level, int xt, int zt) {
        super(level, xt, zt);
        sprite = 0;
    }

    @Override
    public void interactWith(Player player, Item item, float xx, int yy, float zz) {
        sprite = (sprite + 1) % 16;
    }
}
