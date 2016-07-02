package ru.znay.znay.tt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.gfx.*;
import ru.znay.znay.tt.level.Level;
import ru.znay.znay.tt.tool.R;

public class Tranformers extends Game {

    private PerspectiveCamera camera;

    private Color fogColor = new Color(0.1f, 0.2f, 0.2f, 1.0f);
    private float rotY = -90.0f;
    private float slope = 0.02f;
    private int tickTime = 0;
    private float angleWave;
    private float amplitudeWave = 3.4f;
    private float angleWaveSpeed = 1.07f;
    private Level level;
    private double unprocessed = 0.0;
    private long lastTime = System.nanoTime();
    private double iNsPerSec = 60.0 / 1000000000.0;

    public void create() {
        camera = new PerspectiveCamera(70.0f, C.WIDTH, C.HEIGHT);
        camera.near = 1f;
        camera.far = 128f;
        camera.update();

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Gdx.gl.glColorMask(true, true, true, false);
        newGame();
    }

    public void newGame() {
        level = new Level(Art.i.level);
        camera.position.set(level.xSpawn * 16 - 8, 0, level.ySpawn * 16 - 8);
        camera.direction.set(0, -slope, -1).nor().rotate(Vector3.Y, rotY);
        camera.update();
    }


    public void tick() {
        float dt = Gdx.graphics.getDeltaTime();

        angleWave += dt * angleWaveSpeed;
        while (angleWave > Math.PI * 2.0f) {
            angleWave -= Math.PI * 2.0f;
        }

        tickTime++;

        if (tickTime % 60 == 0) {
            System.out.println(Gdx.graphics.getFramesPerSecond());
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
        camera.direction.set(0, -slope, -1).nor().rotate(Vector3.Y, rotY);
        camera.update();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(fogColor.r, fogColor.g, fogColor.b, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        long now = System.nanoTime();
        unprocessed += (now - lastTime) * iNsPerSec;
        lastTime = now;
        while (unprocessed >= 1.0) {
            unprocessed -= 1.0;
            tick();
        }

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_FRONT);

        PlaneBatch pb = Art.i.planeBatch;
        pb.setFogColor(fogColor);
        pb.begin();
        pb.setTexture(Art.i.blocks);
        level.renderBlocks(camera, pb);
        pb.end();

        SpriteBatch3D sb = Art.i.billboardBatch;
        sb.setWave(angleWave, amplitudeWave);
        sb.setFogColor(fogColor);
        sb.setTexture(Art.i.sheet);

        sb.begin(camera);
        level.renderSprites(camera, sb);
        sb.end();



/*        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        Art.i.spriteBatch2D.setProjectionMatrix(new Matrix4().setToOrtho(0, C.WIDTH, C.HEIGHT, 0, 0, 1));
        Art.i.spriteBatch2D.begin();
        Art.i.spriteBatch2D.setColor(Color.WHITE);
        //Art.i.font.draw(Art.i.spriteBatch2D, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 10);
        Art.i.spriteBatch2D.end();*/
    }

    @Override
    public void dispose() {
        super.dispose();
        R.i.dispose();
    }
}
