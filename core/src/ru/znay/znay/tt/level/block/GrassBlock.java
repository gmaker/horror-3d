package ru.znay.znay.tt.level.block;

import ru.znay.znay.tt.gfx.Sprite3D;

/**
 * Created by admin on 28.06.2016.
 */
public class GrassBlock extends Block {
    public GrassBlock() {
        int rr = 2;
        for (int zz = 0; zz < rr; zz++) {
            for (int xx = 0; xx < rr; xx++) {
                float x = (xx / (float)rr + (random.nextFloat() - 0.5f) * 0.1f) * 16.0f;
                float y = (-0.5f + (random.nextFloat() * 0.5f) * 0.1f) * 8.0f;
                float z = (zz / (float)rr + (random.nextFloat() - 0.5f) * 0.1f) * 16.0f;
                float br = 1.0f - random.nextFloat() * 0.3f;
                float r = (1.0f - random.nextFloat() * 0.1f) * br;
                float g = (1.0f - random.nextFloat() * 0.1f) * br;
                float b = (1.0f - random.nextFloat() * 0.1f) * br;
                sprites.add(new Sprite3D(x, y, z, 0, 16, 16, 16).color(r, g, b, 1.0f));
            }
        }
    }
}
