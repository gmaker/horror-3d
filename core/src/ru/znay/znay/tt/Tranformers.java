package ru.znay.znay.tt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import ru.znay.znay.tt.gfx.Art;
import ru.znay.znay.tt.gfx.BillboardBatch;

import java.util.Random;

public class Tranformers extends Game {

    private Random random = new Random();
    private PerspectiveCamera camera;

    private BillboardBatch grassBatch;
    private ModelBatch modelBatch;
    private Color fogColor = new Color(0.1f, 0.2f, 0.1f, 1.0f);
    private float rotY = 0.0f;
    private float slope = 0.3f;
    private int tickTime = 0;
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;
    private ModelInstance modelInstance;
    private Model box;
    private float angleWave;
    private float amplitudeWave = 3.4f;
    private float angleWaveSpeed = 1.07f;

    @Override
    public void create() {
        Art.i.init();

        camera = new PerspectiveCamera(70.0f, C.WIDTH, C.HEIGHT);
        camera.position.set(0, 4, 20);
        camera.direction.set(0, -slope, -1).nor().rotate(Vector3.Y, rotY);
        camera.near = 1f;
        camera.far = 350f;
        camera.update();


        grassBatch = new BillboardBatch(3000, Art.i.billboardShader);
        grassBatch.setFogColor(fogColor);


        modelBatch = new ModelBatch();
        ModelBuilder modelBuilder = new ModelBuilder();
        int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
        modelBuilder.begin();
        float sz = 8;
        modelBuilder.part("front", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(-sz, -sz, -sz, -sz, sz, -sz, sz, sz, -sz, sz, -sz, -sz, 0, 0, -1);
        modelBuilder.part("back", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(-sz, sz, sz, -sz, -sz, sz, sz, -sz, sz, sz, sz, sz, 0, 0, 1);
        modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(-sz, -sz, sz, -sz, -sz, -sz, sz, -sz, -sz, sz, -sz, sz, 0, -1, 0);
        modelBuilder.part("top", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(-sz, sz, -sz, -sz, sz, sz, sz, sz, sz, sz, sz, -sz, 0, 1, 0);
        modelBuilder.part("left", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(-sz, -sz, sz, -sz, sz, sz, -sz, sz, -sz, -sz, -sz, -sz, -1, 0, 0);
        modelBuilder.part("right", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(Art.i.sprites[0])))
                .rect(sz, -sz, -sz, sz, sz, -sz, sz, sz, sz, sz, -sz, sz, 1, 0, 0);
        box = modelBuilder.end();
        modelInstance = new ModelInstance(box);


        spriteBatch = new SpriteBatch();
        bitmapFont = new BitmapFont(true);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_FRONT);
        Gdx.gl.glColorMask(true, true, true, false);
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


    public void tick() {
        tickTime++;
        float xa = 0.0f;
        float za = 0.0f;
        float rotateSpeed = 1.7f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) xa += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) xa -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) za -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) za += 1;

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) rotY += rotateSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.E)) rotY -= rotateSpeed;
        camera.position.add(new Vector3(xa, 0, za).rotate(Vector3.Y, rotY));
        camera.direction.set(0, -slope, -1).nor().rotate(Vector3.Y, rotY);
        camera.update();

    }

    @Override
    public void render() {

        tick();
        float dt = Gdx.graphics.getDeltaTime();

        angleWave += dt * angleWaveSpeed;
        while(angleWave > Math.PI * 2.0f) {
            angleWave -= Math.PI * 2.0f;
        }
        grassBatch.setWave(angleWave, amplitudeWave);

        Gdx.gl.glClearColor(fogColor.r, fogColor.g, fogColor.b, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        for (int i = 0; i < 10; i++) {
            modelInstance.transform.set(new Matrix4().translate((i % 5) * 32, 0, i / 5 * 32).rotate(Vector3.Y, tickTime));
            modelBatch.render(modelInstance);
        }
        modelBatch.end();

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);
        grassBatch.begin(camera);
        int r = 5;
        for (int z = -r; z <= r; z++) {
            for (int x = -r; x <= r; x++) {
                float xo = x * 32.0f;
                float zo = z * 32.0f;
                Matrix4 modelMatrix = new Matrix4().translate(xo, 0.0f, zo);
                BoundingBox bb = new BoundingBox(grassBatch.bb).mul(modelMatrix);

                if (camera.frustum.boundsInFrustum(bb)) {
                    grassBatch.render(modelMatrix);
                }
            }
        }
        grassBatch.end();

        spriteBatch.setProjectionMatrix(new Matrix4().setToOrtho(0, C.WIDTH, C.HEIGHT, 0, 0, 1));
        spriteBatch.begin();
        bitmapFont.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 10);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        Art.i.dispose();
        spriteBatch.dispose();
        bitmapFont.dispose();
        grassBatch.dispose();
    }
}
