package ru.znay.znay.tt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import ru.znay.znay.tt.gfx.Art;

public class Tranformers extends Game {

    private PerspectiveCamera camera;

    private Color fogColor = new Color(0.1f, 0.2f, 0.1f, 1.0f);
    private float rotY = 0.0f;
    private float slope = 0.1f;
    private int tickTime = 0;
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

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_FRONT);
        Gdx.gl.glColorMask(true, true, true, false);
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
        while (angleWave > Math.PI * 2.0f) {
            angleWave -= Math.PI * 2.0f;
        }
        Art.i.grassBatch.setWave(angleWave, amplitudeWave);
        Art.i.grassBatch.setFogColor(fogColor);


        Gdx.gl.glClearColor(fogColor.r, fogColor.g, fogColor.b, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Art.i.modelBatch.begin(camera);
        for (int i = 0; i < 10 * 10; i++) {
            int x = i % 10 - 5;
            int y = i / 10 - 5;
            if (x == -5 || y == -5 || x == 4 || y == 4) {
                Art.i.wallInstance.transform.set(new Matrix4().translate(x * 16, 0, y * 16));
                Art.i.modelBatch.render(Art.i.wallInstance);
            }

        }
        Art.i.modelBatch.end();

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);

        Art.i.grassBatch.begin(camera);
        int r = 5;
        for (int z = -r; z <= r; z++) {
            for (int x = -r; x <= r; x++) {
                float xo = x * 32.0f;
                float zo = z * 32.0f;
                Matrix4 modelMatrix = new Matrix4().translate(xo, 0.0f, zo);
                BoundingBox bb = new BoundingBox(Art.i.grassBatch.bb).mul(modelMatrix);

                if (camera.frustum.boundsInFrustum(bb)) {
                    Art.i.grassBatch.render(modelMatrix);
                }
            }
        }
        Art.i.grassBatch.end();

        Art.i.spriteBatch.setProjectionMatrix(new Matrix4().setToOrtho(0, C.WIDTH, C.HEIGHT, 0, 0, 1));
        Art.i.spriteBatch.begin();
        Art.i.font.draw(Art.i.spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 10);
        Art.i.spriteBatch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        Art.i.dispose();
    }
}
