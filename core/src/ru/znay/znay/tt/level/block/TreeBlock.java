package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.gfx.Sprite3D;

/**
 * Created by admin on 01.07.2016.
 */
public class TreeBlock extends GrassBlock {
    public TreeBlock() {
        super();
        int c = random.nextInt(3) + 1;
        for (int i = 0; i < c; i++) {
            float x = ((random.nextFloat() - 0.5f)) * 16.0f;
            float z = ((random.nextFloat() - 0.5f)) * 16.0f;
            sprites.add(new Sprite3D(x, 0, z, 16, 0, 16, 32, -8, -8));
        }

    }
}
