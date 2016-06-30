package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import ru.znay.znay.tt.tool.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 19.06.2016.
 */
public class Art {
    public Texture sheet;
    public Texture blocks;
    private ShaderProgram billboardShader;
    private ShaderProgram spriteShader3D;
    private ShaderProgram cubeShader;
    public SpriteBatch3D billboardBatch;
    public SpriteBatch3D spriteBatch3D;
    public SpriteBatch spriteBatch2D;
    public Cube cube;
    public BitmapFont font;

    public static Art i = new Art();

    private Art() {
        initShaders();
        sheet = R.i.register(new Texture(Gdx.files.internal("sheet.png")));
        blocks = R.i.register(new Texture(Gdx.files.internal("blocks.png")));
        font = R.i.register(new BitmapFont(true));
        spriteBatch2D = R.i.register(new SpriteBatch());
        billboardBatch = R.i.register(new SpriteBatch3D(3000, billboardShader));
        spriteBatch3D = R.i.register(new SpriteBatch3D(3000, spriteShader3D));
        cube = R.i.register(new Cube(cubeShader));
    }

    private void initShaders() {
        ShaderProgram.pedantic = false;

        billboardShader = R.i.register(new ShaderProgram(Gdx.files.internal("shaders/billboard.vert"), Gdx.files.internal("shaders/common.frag")));
        if (!billboardShader.isCompiled()) {
            System.out.println(billboardShader.getLog());
        }

        spriteShader3D = R.i.register(new ShaderProgram(Gdx.files.internal("shaders/sprite.vert"), Gdx.files.internal("shaders/common.frag")));
        if (!spriteShader3D.isCompiled()) {
            System.out.println(spriteShader3D.getLog());
        }

        cubeShader = R.i.register(new ShaderProgram(Gdx.files.internal("shaders/cube.vert"), Gdx.files.internal("shaders/common.frag")));
        if (!cubeShader.isCompiled()) {
            System.out.println(cubeShader.getLog());
        }
    }
}
