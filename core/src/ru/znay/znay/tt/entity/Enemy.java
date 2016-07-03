package ru.znay.znay.tt.entity;

import ru.znay.znay.tt.gfx.Sprite3D;

/**
 * Created by admin on 03.07.2016.
 */
public class Enemy extends Entity {
    public Enemy(float x, float y, float z) {
        super(x, y, z);
        sprites.add(new Sprite3D(0, 0, 0, 32, 0, 16, 16, -8, -8));
    }


}
