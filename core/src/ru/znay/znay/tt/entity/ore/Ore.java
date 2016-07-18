package ru.znay.znay.tt.entity.ore;

import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.gfx.Sprite3D;

/**
 * Created by admin on 17.07.2016.
 */
public class Ore extends Entity {
    private Sprite3D sprite;

    public Ore(float x, float y, float z) {
        super(x, y, z);
        r = 6f;
        sprite = new Sprite3D(0, 0, 0, 16, 16 * 2, 16, 16);
        sprites.add(sprite);
    }
}
