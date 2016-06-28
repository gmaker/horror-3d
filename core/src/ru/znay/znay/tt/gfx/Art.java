package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Random;

/**
 * Created by admin on 19.06.2016.
 */
public class Art {
    private static final Random random = new Random();
    public static final int SPRITE_SIZE = 16;
    public Texture sheet = new Texture(Gdx.files.internal("sheet.png"));
    public Texture floor = new Texture(Gdx.files.internal("floor.png"));
    private ShaderProgram billboardShader;
    private ShaderProgram spriteShader3D;
    public SpriteBatch3D billboardBatch;
    public SpriteBatch3D spriteBatch3D;
    public SpriteBatch spriteBatch2D;
    public BitmapFont font;

    public static Art i = new Art();

    private Art() {
    }

    public void init() {
        initShaders();
        initGraphics();
    }


    private void initShaders() {
        ShaderProgram.pedantic = false;

        billboardShader = new ShaderProgram(Gdx.files.internal("billboard.vert"), Gdx.files.internal("billboard.frag"));
        if (!billboardShader.isCompiled()) {
            System.out.println(billboardShader.getLog());
        }

        spriteShader3D = new ShaderProgram(Gdx.files.internal("sprite.vert"), Gdx.files.internal("billboard.frag"));
        if (!spriteShader3D.isCompiled()) {
            System.out.println(spriteShader3D.getLog());
        }
    }

    private void initGraphics() {
        font = new BitmapFont(true);
        spriteBatch2D = new SpriteBatch();
        billboardBatch = new SpriteBatch3D(3000, billboardShader);
        spriteBatch3D = new SpriteBatch3D(3000, spriteShader3D);
    }

    private void initGrass() {

    }


    public void dispose() {
        sheet.dispose();
        billboardShader.dispose();
        spriteShader3D.dispose();
        spriteBatch2D.dispose();
        spriteShader3D.dispose();
        billboardBatch.dispose();
        font.dispose();
    }
}
