package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    public Pixmap level;
    private ShaderProgram billboardShader;
    private ShaderProgram planeShader;
    public SpriteBatch3D billboardBatch;
    public SpriteBatch spriteBatch2D;
    public PlaneBatch planeBatch;
    public BitmapFont font;

    public static Art i = new Art();

    private Art() {

        initShaders();
        Texture levelTexture = R.i.register(new Texture(Gdx.files.internal("level.png")));
        levelTexture.getTextureData().prepare();;
        level = R.i.register(levelTexture.getTextureData().consumePixmap());
        sheet = R.i.register(new Texture(Gdx.files.internal("sheet.png")));
        blocks = R.i.register(new Texture(Gdx.files.internal("blocks.png")));
        dithering = R.i.register(createDitheringTexture());
        font = R.i.register(new BitmapFont(true));
        planeBatch = R.i.register(new PlaneBatch(3000, planeShader));
        spriteBatch2D = R.i.register(new SpriteBatch());
        billboardBatch = R.i.register(new SpriteBatch3D(3000, billboardShader));

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

        billboardShader = R.i.register(new ShaderProgram(Gdx.files.internal("shaders/billboard.vert"), Gdx.files.internal("shaders/billboard.frag")));
        if (!billboardShader.isCompiled()) {
            System.out.println(billboardShader.getLog());
        }

        planeShader = R.i.register(new ShaderProgram(Gdx.files.internal("shaders/plane.vert"), Gdx.files.internal("shaders/plane.frag")));
        if (!planeShader.isCompiled()) {
            System.out.println(planeShader.getLog());
        }
    }
}
