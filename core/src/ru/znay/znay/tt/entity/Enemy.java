package ru.znay.znay.tt.entity;

import ru.znay.znay.tt.gfx.Sprite3D;

/**
 * Created by admin on 03.07.2016.
 */
public class Enemy extends Entity {
    private Sprite3D sprite;
    private int tickTime = 0;
    private int[] steps = new int[]{0, 1, 0, 2};

    public Enemy(float x, float y, float z) {
        super(x, y, z);
        sprite = new Sprite3D(0, 0, 0, 32, 0, 16, 16, -8, -8);
        sprites.add(sprite);
    }

    public void tick() {
        tickTime++;
        int frame = steps[tickTime / 15 % steps.length];
        sprite.setSprite(32 + frame * 16, 0, 16, 16);
    }


}
