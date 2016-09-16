package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.entity.Player;
import ru.znay.znay.tt.entity.item.Item;
import ru.znay.znay.tt.level.Level;

/**
 * Created by admin on 29.06.2016.
 */
public class WallBlock extends SolidBlock {
    public int decorate = 0;

    public WallBlock(Level level, int xt, int zt) {
        super(level, xt, zt);
        sprite = decorate + Math.abs(level.level) * 16;
    }

    @Override
    public void interactWith(Player player, Item item, float xx, int yy, float zz) {
        decorate = (decorate + 1) % 16;
        sprite = Math.abs(level.level) * 16 + decorate;
    }
}
