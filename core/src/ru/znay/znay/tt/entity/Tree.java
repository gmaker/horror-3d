package ru.znay.znay.tt.entity;

import ru.znay.znay.tt.gfx.Sprite3D;

/**
 * Created by admin on 28.06.2016.
 */
public class Tree extends Entity {
    Sprite3D sprite;

    public Tree(float x, float y, float z) {
        super(x, y, z);
        sprite = new Sprite3D(0, 0, 0, 16, 0, 16, 16);
        sprites.add(sprite);
    }


}
