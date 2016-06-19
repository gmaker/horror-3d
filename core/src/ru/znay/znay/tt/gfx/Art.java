package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by admin on 19.06.2016.
 */
public class Art {
    public static final int SPRITE_SIZE = 16;
    public Texture sheet = new Texture(Gdx.files.internal("sheet.png"));
    public TextureRegion[] sprites;
    public ShaderProgram quadShader;

    public static Art i = new Art();

    private Art() {

    }

    public void init() {
        TextureRegion[][] regions = TextureRegion.split(sheet, 16, 16);

        int w = sheet.getWidth() / 16;
        int h = sheet.getHeight() / 16;
        sprites = new TextureRegion[w * h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                sprites[x + y * w] = regions[y][x];
            }
        }
        ShaderProgram.pedantic = false;
        quadShader = new ShaderProgram(Gdx.files.internal("quad.vert"), Gdx.files.internal("quad.frag"));
        if (!quadShader.isCompiled()) {
            System.out.println(quadShader.getLog());
        }
    }

    public void dispose() {
        sheet.dispose();
        quadShader.dispose();
    }
}
