package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import ru.znay.znay.tt.C;

/**
 * Created by admin on 02.09.2016.
 */
public class TextureGenerator {

    public static Texture mergeTiles(int tile0, int tile1) {
        Pixmap sheetPM = Art.i.getPixmap(Art.i.blocks);
        Pixmap pixmap = new Pixmap(C.TILE_SIZE * 16, C.TILE_SIZE, Pixmap.Format.RGBA8888);

        for (int i = 0; i < 16; i++) {

            int a = (i >> 0) & 1;
            int b = (i >> 1) & 1;
            int c = (i >> 2) & 1;
            int d = (i >> 3) & 1;

            double iSz = 1.0 / C.TILE_SIZE;
            for (int y = 0; y < C.TILE_SIZE; y++) {
                double yp = y * iSz;
                for (int x = 0; x < C.TILE_SIZE; x++) {
                    double xp = x * iSz;

                    double ab = a + (b - a) * xp;
                    double cd = c + (d - c) * xp;
                    double val = ab + (cd - ab) * yp;

                    if (val < 0) val = 0;
                    if (val > 1.0) val = 1.0;

                    int c0 = sheetPM.getPixel(x + (tile0 % 8) * C.TILE_SIZE, (y + (tile0 / 8) * C.TILE_SIZE));
                    int c1 = sheetPM.getPixel(x + (tile1 % 8) * C.TILE_SIZE, (y + (tile1 / 8) * C.TILE_SIZE));

                    int r0 = (c0 >> 24) & 0xff;
                    int g0 = (c0 >> 16) & 0xff;
                    int b0 = (c0 >> 8) & 0xff;

                    int r1 = (c1 >> 24) & 0xff;
                    int g1 = (c1 >> 16) & 0xff;
                    int b1 = (c1 >> 8) & 0xff;

                    int rr = (int) (r0 + (r1 - r0) * val);
                    int gg = (int) (g0 + (g1 - g0) * val);
                    int bb = (int) (b0 + (b1 - b0) * val);

                    pixmap.drawPixel(x + i * 16, y, (rr << 24) | (gg << 16) | bb << 8 | 0xff);
                }
            }
        }

        return new Texture(pixmap);
    }

    public static Texture mergeTextures(Texture t0, Texture t1) {
        if (!t0.getTextureData().isPrepared()) {
            t0.getTextureData().prepare();
        }
        if (!t1.getTextureData().isPrepared()) {
            t1.getTextureData().prepare();
        }

        int w = Math.max(t0.getWidth(), t1.getWidth());

        Pixmap pixmap = new Pixmap(w, t0.getHeight() + t1.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.drawPixmap(t0.getTextureData().consumePixmap(), 0, 0);
        pixmap.drawPixmap(t1.getTextureData().consumePixmap(), 0, t0.getHeight());

        return new Texture(pixmap);
    }
}
