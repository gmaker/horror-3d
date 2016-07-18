package ru.znay.znay.tt.entity.item;

import ru.znay.znay.tt.gfx.Animation;

/**
 * Created by admin on 12.07.2016.
 */
public class Item {
    public static final Item pick = new Item("Pick", 0, 6 * 16, 3, 5).scale(0.8f);

    public Animation animation;
    public final String name;

    public Item(String name, int x, int y, int frames, int time) {
        this.name = name;
        animation = new Animation(x, y, 16, 16, 1, 0, frames, time);
    }

    public Item scale(float scale) {
        animation.sprite.scale(scale);
        return this;
    }

    public void tick() {
        animation.tick();
    }
}
