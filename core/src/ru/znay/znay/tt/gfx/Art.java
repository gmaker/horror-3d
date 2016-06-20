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
    public ShaderProgram spriteShader;
    public ShaderProgram billboardShader;

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
        spriteShader = new ShaderProgram(Gdx.files.internal("sprite.vert"), Gdx.files.internal("sprites.frag"));
        if (!spriteShader.isCompiled()) {
            System.out.println(spriteShader.getLog());
        }

        billboardShader = new ShaderProgram(Gdx.files.internal("billboard.vert"), Gdx.files.internal("sprites.frag"));
        if (!billboardShader.isCompiled()) {
            System.out.println(billboardShader.getLog());
        }
    }

    public void dispose() {
        sheet.dispose();
        spriteShader.dispose();
        billboardShader.dispose();
    }
}
