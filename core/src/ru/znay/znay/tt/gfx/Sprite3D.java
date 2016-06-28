package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.graphics.Camera;

/**
 * Created by admin on 28.06.2016.
 */
public class Sprite3D {
    public float x, y, z;
    public float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;
    private float sx, sy, sw, sh;

    public Sprite3D(float x, float y, float z, float sx, float sy, float sw, float sh) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.sx = sx;
        this.sy = sy;
        this.sw = sw;
        this.sh = sh;
    }

    public Sprite3D color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    public final void addSprite(Camera camera, SpriteBatch3D spriteBatch3D) {
        addSprite(0, 0, 0, camera, spriteBatch3D);
    }

    public final void addSprite(float xo, float yo, float zo, Camera camera, SpriteBatch3D spriteBatch3D) {
        if (camera.frustum.pointInFrustum(x + xo, y + yo, z + zo)) {
            spriteBatch3D.setColor(r, g, b, a);
            spriteBatch3D.addSprite(x + xo, y + yo, z + zo, sx, sy, sw, sh);
        }
    }

}
