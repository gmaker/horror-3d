package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import ru.znay.znay.tt.tool.R;

/**
 * Created by admin on 19.06.2016.
 */
public class Art {
    public Texture dithering;
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
        dithering = R.i.register(createDitheringTexture());
        font = R.i.register(new BitmapFont(true));
        spriteBatch2D = R.i.register(new SpriteBatch());
        billboardBatch = R.i.register(new SpriteBatch3D(3000, billboardShader));
        spriteBatch3D = R.i.register(new SpriteBatch3D(3000, spriteShader3D));
        cube = R.i.register(new Cube(cubeShader));

    }

    private Texture createDitheringTexture() {
        int[] dis = {
                1, 9, 3, 11,
                13, 5, 15, 7,
                4, 12, 2, 10,
                16, 8, 14, 6
        };

        Pixmap pixmap = new Pixmap(4, 4, Pixmap.Format.Alpha);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                pixmap.drawPixel(x, y, dis[x + y * 4]);
            }
        }
        Texture result = new Texture(pixmap);
        pixmap.dispose();

        return result;
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
