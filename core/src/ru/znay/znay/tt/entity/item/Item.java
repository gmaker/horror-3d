package ru.znay.znay.tt.entity.item;

import ru.znay.znay.tt.gfx.Sprite3D;

/**
 * Created by admin on 12.07.2016.
 */
public class Item {
    public Sprite3D sprite;

    public Item() {
        sprite = new Sprite3D(0, 0, 0, 0, 6 * 16, 16, 16);
        sprite.scale(0.8f);
    }
}
