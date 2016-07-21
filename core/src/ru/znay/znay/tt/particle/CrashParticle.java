package ru.znay.znay.tt.particle;

import ru.znay.znay.tt.gfx.Sprite3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 19.07.2016.
 */
public class CrashParticle extends Particle {

    public CrashParticle(Sprite3D spritePart, float x, float y, float z) {
        super(x, y, z);
        sprite = spritePart;
        xa *= 0.05;
        ya *= 0.05;
        za *= 0.05;
        timeLife = maxTimeLife = random.nextInt(20) + 10;
    }

    public static List<CrashParticle> crashParticles(float x, float y, float z, Sprite3D spriteToBeCrashed, int parts) {
        if (parts <= 0) return Collections.EMPTY_LIST;
        List<CrashParticle> result = new ArrayList<CrashParticle>(parts);
        float ww = spriteToBeCrashed.sw / parts;
        float hh = spriteToBeCrashed.sh / parts;
        for (int i = 0; i < parts; i++) {
            for (int j = 0; j < parts; j++) {
                float xx = x + (random.nextFloat() - 0.5f) * 12.0f;
                float yy = y + (i / (float) parts) * 4;
                float zz = z + (random.nextFloat() - 0.5f) * 12.0f;
                Sprite3D spritePart = new Sprite3D(0, 0, 0, spriteToBeCrashed.sx + i * ww, spriteToBeCrashed.sy + j * hh, ww, hh);
                result.add(new CrashParticle(spritePart, xx, yy, zz));
            }
        }
        return result;
    }

}
