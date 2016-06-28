package ru.znay.znay.tt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.entity.Tree;
import ru.znay.znay.tt.gfx.Art;
import ru.znay.znay.tt.gfx.SpriteBatch3D;
import ru.znay.znay.tt.level.Level;

public class Tranformers extends Game {

    private PerspectiveCamera camera;

    private Color fogColor = new Color(0.1f, 0.2f, 0.1f, 1.0f);
    private float rotY = 0.0f;
    private float slope = 0.1f;
    private int tickTime = 0;
    private float angleWave;
    private float amplitudeWave = 3.4f;
    private float angleWaveSpeed = 1.07f;
    private Level level;


    @Override
    public void create() {
        Art.i.init();

        camera = new PerspectiveCamera(70.0f, C.WIDTH, C.HEIGHT);
        camera.position.set(0, 4, 20);
        camera.direction.set(0, -slope, -1).nor().rotate(Vector3.Y, rotY);
        camera.near = 1f;
        camera.far = 150f;
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
        level = new Level(32, 32);
        for (int i = 0; i < 10; i++) {
            level.addEntity(new Tree((i % 5) * 16, 0, (i / 5) * 16));
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
        Gdx.gl.glClearColor(fogColor.r, fogColor.g, fogColor.b, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        tick();
        float dt = Gdx.graphics.getDeltaTime();

        angleWave += dt * angleWaveSpeed;
        while (angleWave > Math.PI * 2.0f) {
            angleWave -= Math.PI * 2.0f;
        }

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_FRONT);

        SpriteBatch3D sb = Art.i.billboardBatch;
        sb.setWave(angleWave, amplitudeWave);
        sb.setFogColor(fogColor);
        sb.setTexture(Art.i.sheet);

        sb.begin(camera);
        level.render(camera, sb);
        sb.end();

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        Art.i.spriteBatch2D.setProjectionMatrix(new Matrix4().setToOrtho(0, C.WIDTH, C.HEIGHT, 0, 0, 1));
        Art.i.spriteBatch2D.begin();
        Art.i.spriteBatch2D.setColor(Color.WHITE);
        Art.i.font.draw(Art.i.spriteBatch2D, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 10);
        Art.i.spriteBatch2D.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        Art.i.dispose();
    }
}
