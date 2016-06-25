package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Random;

/**
 * Created by admin on 19.06.2016.
 */
public class Art {
    private static final Random random = new Random();
    public static final int SPRITE_SIZE = 16;
    public Texture sheet;
    public TextureRegion[] sprites;
    private ShaderProgram billboardShader;
    public BillboardBatch grassBatch;
    public BillboardBatch billboardBatch;
    public SpriteBatch spriteBatch;
    public ModelBatch modelBatch;
    public BitmapFont font;
    public ModelInstance wallInstance;
    public ModelInstance tileInstance;

    public static Art i = new Art();

    private Art() {
    }

    public void init() {
        initSprites();
        initShaders();
        initGraphics();
        initGrass();
        initModels();
    }

    private void initSprites() {
        sheet = new Texture(Gdx.files.internal("sheet.png"));
        TextureRegion[][] regions = TextureRegion.split(sheet, 16, 16);

        int w = sheet.getWidth() / 16;
        int h = sheet.getHeight() / 16;
        sprites = new TextureRegion[w * h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                sprites[x + y * w] = regions[y][x];
            }
        }
    }

    private void initShaders() {
        ShaderProgram.pedantic = false;

        billboardShader = new ShaderProgram(Gdx.files.internal("billboard.vert"), Gdx.files.internal("billboard.frag"));
        if (!billboardShader.isCompiled()) {
            System.out.println(billboardShader.getLog());
        }
    }

    private void initGraphics() {
        font = new BitmapFont(true);
        modelBatch = new ModelBatch();
        spriteBatch = new SpriteBatch();
        billboardBatch = new BillboardBatch(3000, billboardShader);
        grassBatch = new BillboardBatch(3000, billboardShader);
    }

    private void initGrass() {
        for (int zz = 0; zz < 5; zz++) {
            for (int xx = 0; xx < 5; xx++) {
                float x = (xx / 5.0f + (random.nextFloat() - 0.5f) * 0.1f) * 64.0f;
                float y = (-0.5f + (random.nextFloat() * 0.5f) * 0.1f) * 8.0f;
                float z = (zz / 5.0f + (random.nextFloat() - 0.5f) * 0.1f) * 64.0f;
                float br = 1.0f - random.nextFloat() * 0.1f;
                float r = (1.0f - random.nextFloat() * 0.1f) * br;
                float g = (1.0f - random.nextFloat() * 0.1f) * br;
                float b = (1.0f - random.nextFloat() * 0.1f) * br;
                grassBatch.setColor(r, g, b, 1.0f);
                grassBatch.addSprite(x, y, z, 0, 16, 16, 16);
            }
        }
    }

    private void initModels() {
        //wall
        ModelBuilder modelBuilder = new ModelBuilder();
        int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
        modelBuilder.begin();
        float sz = 8;
        modelBuilder.part("front", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(-sz, -sz, -sz, -sz, sz, -sz, sz, sz, -sz, sz, -sz, -sz, 0, 0, -1);
        modelBuilder.part("back", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(-sz, sz, sz, -sz, -sz, sz, sz, -sz, sz, sz, sz, sz, 0, 0, 1);
        //modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
        //  .rect(-sz, -sz, sz, -sz, -sz, -sz, sz, -sz, -sz, sz, -sz, sz, 0, -1, 0);
        //   modelBuilder.part("top", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
        //  .rect(-sz, sz, -sz, -sz, sz, sz, sz, sz, sz, sz, sz, -sz, 0, 1, 0);
        modelBuilder.part("left", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(-sz, -sz, sz, -sz, sz, sz, -sz, sz, -sz, -sz, -sz, -sz, -1, 0, 0);
        modelBuilder.part("right", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(sz, -sz, -sz, sz, sz, -sz, sz, sz, sz, sz, -sz, sz, 1, 0, 0);
        Model model = modelBuilder.end();
        wallInstance = new ModelInstance(model);

        //tile
        modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.part("tile", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(-sz, -sz, -sz, -sz, -sz, sz, sz, -sz, sz, sz, -sz, -sz, 0, 1, 0);
        model = modelBuilder.end();
        tileInstance = new ModelInstance(model);
    }

    public void dispose() {
        sheet.dispose();
        billboardShader.dispose();
        spriteBatch.dispose();
        grassBatch.dispose();
        billboardBatch.dispose();
        modelBatch.dispose();
        font.dispose();
    }
}
