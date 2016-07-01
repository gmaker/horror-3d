package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.gfx.Sprite3D;

/**
 * Created by admin on 01.07.2016.
 */
public class TreeBlock extends GrassBlock {
    public TreeBlock() {
        super();
        float x = ((random.nextFloat() - 0.5f) * 0.2f) * 8.0f;
        float z = ((random.nextFloat() - 0.5f) * 0.2f) * 8.0f;
        sprites.add(new Sprite3D(x, 0, z, 16, 0, 16, 16));
    }
}
