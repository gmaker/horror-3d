package ru.znay.znay.tt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.gfx.Art;
import ru.znay.znay.tt.gfx.Quad;

import java.util.Random;

public class Tranformers extends Game {
    PerspectiveCamera camera;

    Quad quad;
    private Color fogColor = new Color(0.1f, 0.2f, 0.0f, 1.0f);
    private float rotY = 0.0f;
    private int tickTime = 0;

    @Override
    public void create() {
        Art.i.init();

        camera = new PerspectiveCamera(60.0f, C.WIDTH, C.HEIGHT);
        camera.position.set(0, 0, 50);
        // camera.lookAt(0, 8.0f, 0);
        camera.near = 1f;
        camera.far = 200f;
        camera.update();
        quad = new Quad(1000, Art.i.quadShader);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        //Gdx.gl.glCullFace(GL20.GL_FRONT);
        Gdx.gl.glColorMask(true, true, true, false);
    }

    private Random random = new Random();

    @Override
    public void render() {
        tickTime++;
        Gdx.gl.glClearColor(fogColor.r, fogColor.g, fogColor.b, fogColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        quad.setProjectionMatrix(camera.combined);
        quad.begin();
        quad.setFogColor(fogColor.r, fogColor.g, fogColor.b);


        for (int z = 0; z < 64; z++) {
            for (int x = 0; x < 64; x++) {
                random.setSeed(x * 124124L + z * 1234124L + 1288127L);
                float xo = (random.nextFloat() * 2.0f - 1.0f) * 4.0f;
                float yo = (random.nextFloat() * 2.0f - 1.0f) * 2.0f;
                float zo = (random.nextFloat() * 2.0f - 1.0f) * 4.0f;

                quad.render(camera, x * 32 + xo, -8 + yo, z * 8 + zo, 0 + 1 * 8);
                quad.render(camera, x * 32 + 16 + xo, -8 + yo, z * 8 + zo, 1 + 1 * 8);
            }
        }

        for (int i = 0; i < 16; i++) {
            for (int z = 0; z < 2; z++) {
                if (z == 0) {
                    quad.render(camera, i * 16, 0, z * 16, 0);
                } else {
                    if (i % 2 == 0)
                        quad.renderBillboard(camera, i * 16, 0, z * 16, 1);
                }

            }
        }

        quad.end();
        if (tickTime % 60 == 0) {
            System.out.println(quad.renderCalls);
        }

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
    }

    @Override
    public void dispose() {
        super.dispose();
        Art.i.dispose();
        quad.dispose();
    }
}
