package ru.znay.znay.tt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import ru.znay.znay.tt.gfx.Art;
import ru.znay.znay.tt.gfx.SpriteBatch3D;

import java.util.Random;

public class Tranformers extends Game {
    PerspectiveCamera camera;

    SpriteBatch3D sprites;
    SpriteBatch3D billboards;
    private Color fogColor = new Color(0.1f, 0.2f, 0.1f, 1.0f);
    private float rotY = 0.0f;
    private int tickTime = 0;
    private SpriteBatch spriteBatch;
    private BitmapFont bitmapFont;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        bitmapFont = new BitmapFont();
        Art.i.init();

        camera = new PerspectiveCamera(60.0f, C.WIDTH, C.HEIGHT);
        camera.position.set(0, 10, 0);
        // camera.lookAt(0,0,-1);
        camera.near = 1f;
        camera.far = 350f;
        camera.update();
        sprites = new SpriteBatch3D(3000, Art.i.spriteShader);
        billboards = new SpriteBatch3D(3000, Art.i.billboardShader);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        //Gdx.gl.glCullFace(GL20.GL_FRONT);
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
                billboards.setColor(r, g, b, 1.0f);
                billboards.addSprite(x, y, z, 0, 16, 16, 16);
            }
        }
/*
        for (int i = 0; i < 32; i++) {
            for (int z = 0; z < 2; z++) {
                if (z == 0) {
                    sprites.addSprite(i * 16, 0, z * 16, 0);
                } else {
                    if (i % 2 == 0)
                        billboards.addSprite(i * 16, 0, z * 16, 1);
                }

            }
        }*/
    }

    private Random random = new Random();
    private Vector3 tempVector = new Vector3();

    @Override
    public void render() {
        tickTime++;

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        sprites.setFogColor(fogColor);
        sprites.setProjectMatrix(camera.projection);
        sprites.setViewMatrix(camera.view);

        billboards.setFogColor(fogColor);
        billboards.setProjectMatrix(camera.projection);
        billboards.setViewMatrix(camera.view);


        billboards.begin();
        int r = 50;
        int c = 0;
        for (int z = -r; z <= r; z++) {
            for (int x = -r; x <= r; x++) {
                float xo = x * 32.0f;
                float zo = z * 32.0f;
                c++;
                Matrix4 modelMatrix = new Matrix4().translate(xo, 0.0f, zo);
                BoundingBox bb = new BoundingBox(billboards.bb).mul(modelMatrix);

                if (camera.frustum.boundsInFrustum(bb)) {
                    billboards.render(modelMatrix);
                }
            }
        }
        if (tickTime % 60 == 0) {
            System.out.println("fps: " + Gdx.graphics.getFramesPerSecond());
            System.out.println("rc: " + billboards.renderCalls + " c: " + c);
        }

        billboards.end();


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
        camera.direction.set(0, 0, -1).rotate(Vector3.Y, rotY);
        camera.update();
        spriteBatch.begin();
        bitmapFont.draw(spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        Art.i.dispose();
        spriteBatch.dispose();
        bitmapFont.dispose();
        sprites.dispose();
        billboards.dispose();
    }
}
