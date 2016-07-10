package ru.znay.znay.tt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import ru.znay.znay.tt.entity.Player;
import ru.znay.znay.tt.gfx.Art;
import ru.znay.znay.tt.gfx.PlaneBatch;
import ru.znay.znay.tt.gfx.SpriteBatch3D;
import ru.znay.znay.tt.gfx.light.Light;
import ru.znay.znay.tt.level.Level;
import ru.znay.znay.tt.tool.R;

import java.util.List;

public class Tranformers extends Game {

    private PerspectiveCamera camera;


    private int tickTime = 0;
    private Level level;
    private double unprocessed = 0.0;
    private long lastTime = System.nanoTime();
    private double iNsPerSec = 60.0 / 1000000000.0;
    private Player player;
    public Stage stage;

    public void create() {
        camera = new PerspectiveCamera(70.0f, C.WIDTH, C.HEIGHT);
        camera.near = 1f;
        camera.far = 128f;
        camera.update();

        stage = new Stage(new FitViewport(C.WIDTH, C.HEIGHT, camera));

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glCullFace(GL20.GL_FRONT);

        newGame();
    }

    public void newGame() {
        level = new Level(Art.i.level);
        player = new Player(level.xSpawn * 16, 0, level.ySpawn * 16);
        level.addEntity(player);
        updateCam(player);
    }

    public void updateCam(Player p) {
        camera.position.set(p.x, p.y, p.z);
        camera.direction.set(0, -(p.slope), -1).nor().rotate(Vector3.Y, (float) (p.rot / Math.PI) * 180.0f);
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }


    public void tick() {
        tickTime++;

        if (tickTime % 60 == 0) {
            System.out.println(Gdx.graphics.getFramesPerSecond());
        }

        level.tick();

        player.tick(
                Gdx.input.isKeyPressed(Input.Keys.W),
                Gdx.input.isKeyPressed(Input.Keys.S),
                Gdx.input.isKeyPressed(Input.Keys.A),
                Gdx.input.isKeyPressed(Input.Keys.D),
                Gdx.input.isKeyPressed(Input.Keys.Q),
                Gdx.input.isKeyPressed(Input.Keys.E));
        updateCam(player);
    }

    @Override
    public void render() {

        long now = System.nanoTime();
        unprocessed += (now - lastTime) * iNsPerSec;
        lastTime = now;
        while (unprocessed >= 1.0) {
            unprocessed -= 1.0;
            tick();
        }

        PlaneBatch pb = Art.i.planeBatch;
        SpriteBatch3D sb = Art.i.billboardBatch;

        List<Light> lights = level.buildAllToRender(camera, pb, sb, 6);

        renderLights(lights, pb);
        renderShadows(lights, pb);
        renderScene(pb, sb);

        pb.reset();
        sb.reset();
    }

    private void renderLights(List<Light> lights, PlaneBatch pb) {
        Gdx.gl.glDisable(GL20.GL_BLEND);
        for (Light light : lights) {
            light.render(pb);
        }
    }

    private void renderShadows(List<Light> lights, PlaneBatch pb) {
        Art.i.shadowMap.render(camera, pb, lights);
    }

    private void renderScene(PlaneBatch pb, SpriteBatch3D sb) {
        Gdx.gl.glClearColor(C.FOG_COLOR.r, C.FOG_COLOR.g, C.FOG_COLOR.b, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glCullFace(GL20.GL_FRONT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        pb.begin(Art.i.planeShader, camera);
        pb.render();
        pb.end();

        sb.begin(Art.i.billboardShader, camera);
        sb.render();
        sb.end();
    }


    @Override
    public void dispose() {
        super.dispose();
        R.i.dispose();
    }
}
