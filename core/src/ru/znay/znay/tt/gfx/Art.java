package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import ru.znay.znay.tt.gfx.light.ShadowMap;
import ru.znay.znay.tt.gfx.shader.*;
import ru.znay.znay.tt.tool.R;


/**
 * Created by admin on 19.06.2016.
 */
public class Art {
    public Texture dithering;
    public Texture sheet;
    public Texture blocks;
    public Shader billboardShader;
    public Shader planeShader;
    public Shader lightShader;
    public Shader shadowShader;
    public ShadowMap shadowMap;
    public SpriteBatch3D billboardBatch;
    public SpriteBatch spriteBatch2D;
    public PlaneBatch planeBatch;
    public BitmapFont font;

    public static Art i = new Art();

    private Art() {
        initShaders();
        sheet = R.i.register(new Texture(Gdx.files.internal("sheet.png")));
        blocks = R.i.register(new Texture(Gdx.files.internal("blocks.png")));
        dithering = R.i.register(createDitheringTexture());
        font = R.i.register(new BitmapFont(Gdx.files.internal("fonts/font8h.fnt"), true));
        //font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        //font.getData().setScale(0.2f);
        planeBatch = R.i.register(new PlaneBatch(3500, blocks));
        spriteBatch2D = R.i.register(new SpriteBatch());
        billboardBatch = R.i.register(new SpriteBatch3D(3500, sheet));
        shadowMap = new ShadowMap(shadowShader);
    }

    private Texture createDitheringTexture() {
        int[] dis = {
                1, 9, 3, 11,
                13, 5, 15, 7,
                4, 12, 2, 10,
                16, 8, 14, 6
        };

        Pixmap pixmap = new Pixmap(4, 4, Pixmap.Format.RGBA8888);

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

        billboardShader = new BillboardShader((ShaderProgram) R.i.register(new ShaderProgram(Gdx.files.internal("shaders/billboard.vert"), Gdx.files.internal("shaders/billboard.frag"))));
        if (!billboardShader.shaderProgram.isCompiled()) {
            System.out.println(billboardShader.shaderProgram.getLog());
        }

        planeShader = new PlaneShader((ShaderProgram) R.i.register(new ShaderProgram(Gdx.files.internal("shaders/plane.vert"), Gdx.files.internal("shaders/plane.frag"))));
        if (!planeShader.shaderProgram.isCompiled()) {
            System.out.println(planeShader.shaderProgram.getLog());
        }

        lightShader = new LightShader((ShaderProgram) R.i.register(new ShaderProgram(Gdx.files.internal("shaders/light.vert"), Gdx.files.internal("shaders/light.frag"))));
        if (!lightShader.shaderProgram.isCompiled()) {
            System.out.println(lightShader.shaderProgram.getLog());
        }

        shadowShader = new ShadowShader((ShaderProgram) R.i.register(new ShaderProgram(Gdx.files.internal("shaders/shadow.vert"), Gdx.files.internal("shaders/shadow.frag"))));
        if (!shadowShader.shaderProgram.isCompiled()) {
            System.out.println(shadowShader.shaderProgram.getLog());
        }
    }

    public Pixmap getPixmap(String filePath) {
        Texture levelTexture = R.i.register(new Texture(Gdx.files.internal(filePath)));
        levelTexture.getTextureData().prepare();
        return R.i.register(levelTexture.getTextureData().consumePixmap());
    }

    public void drawString(SpriteBatch sb2d, String msg, int x, int y, Color color) {
        Art.i.font.setColor(Color.GRAY);
        Art.i.font.draw(sb2d, msg, x - 1, y);
        Art.i.font.setColor(color);
        Art.i.font.draw(sb2d, msg, x, y);
    }

    public void drawProgressBar(SpriteBatch sb2d, float x, float y, int width, Color fill, Color empty, float p, float scale) {
        for (int i = 0; i < width; i++) {
            sb2d.setColor(fill);
            if ((i + 1) / (float) width > p) {
                sb2d.setColor(empty);
            }
            sb2d.draw(Art.i.sheet, x + i * scale, y, scale, scale, 0, 16 * 8, 1, 1, false, false);
        }
        sb2d.setColor(Color.WHITE);
    }
}
