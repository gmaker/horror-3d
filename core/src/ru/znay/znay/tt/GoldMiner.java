package ru.znay.znay.tt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.znay.znay.tt.entity.Player;
import ru.znay.znay.tt.gfx.Art;
import ru.znay.znay.tt.gfx.PlaneBatch;
import ru.znay.znay.tt.gfx.SpriteBatch3D;
import ru.znay.znay.tt.gfx.light.Light;
import ru.znay.znay.tt.level.Level;
import ru.znay.znay.tt.level.block.StairBlock;
import ru.znay.znay.tt.tool.R;

import java.util.List;

public class GoldMiner extends Game {

    private PerspectiveCamera camera;
    private OrthographicCamera scaledCamera;
    private OrthographicCamera guiCamera;
    private FrameBuffer sceneBuffer;

    private int tickTime = 0;
    private Level level;
    private double unprocessed = 0.0;
    private long lastTime = System.nanoTime();
    private double iNsPerSec = 60.0 / 1000000000.0;
    private Player player;
    private Viewport viewport;
    private int saveTime = 0;

    public void create() {
        Art.i.generateThings();
        sceneBuffer = R.i.register(new FrameBuffer(Pixmap.Format.RGBA8888, C.WIDTH, C.HEIGHT, true));
        sceneBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        camera = new PerspectiveCamera(70.0f, C.WIDTH, C.HEIGHT);
        camera.near = 1f;
        camera.far = 2000f;
        camera.update();

        scaledCamera = new OrthographicCamera();
        scaledCamera.position.set(C.WIDTH / 2.0f, C.HEIGHT / 2.0f, 0);
        scaledCamera.setToOrtho(true, C.WIDTH, C.HEIGHT);
        scaledCamera.update();

        viewport = new ExtendViewport(C.WIDTH, C.HEIGHT, scaledCamera);

        guiCamera = new OrthographicCamera();
        guiCamera.position.set(C.WIDTH / 2.0f, C.HEIGHT / 2.0f, 0);
        guiCamera.setToOrtho(true, C.WIDTH, C.HEIGHT);
        guiCamera.update();

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glCullFace(GL20.GL_FRONT);

        newGame();
    }

    public void newGame() {
        level = Level.loadLevel(this, 0);
        player = new Player(level.xSpawn * 16, 0, level.ySpawn * 16);
        level.addEntity(player);
        updateCam(player);
    }

    public void switchLevel(int currentLevel, int nextLevel) {
        level.removeEntityImmediately(player);
        level = Level.loadLevel(this, nextLevel);
        level.findSpawn(currentLevel);
        player.x = level.xSpawn * 16;
        player.y = 0;
        player.z = level.ySpawn * 16;
        ((StairBlock) level.getBlock(level.xSpawn, level.ySpawn)).wait = true;
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
        viewport.update(width, height, false);
    }

    public void saveDecoration() {
        if (saveTime > 0) return;
        saveTime = 60;
        level.saveDecoration();
    }

    public void loadDecoration() {
        level.loadDecoration();
    }

    public void tick() {
        tickTime++;

        if (tickTime % 60 == 0) {
            //System.out.println(Gdx.graphics.getFramesPerSecond());
        }

        level.tick();
        if (saveTime > 0) saveTime--;
        if (Gdx.input.isKeyPressed(Input.Keys.F5)) {
            saveDecoration();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F8)) {
            loadDecoration();
        }

        player.tick(
                Gdx.input.isKeyPressed(Input.Keys.W),
                Gdx.input.isKeyPressed(Input.Keys.S),
                Gdx.input.isKeyPressed(Input.Keys.A),
                Gdx.input.isKeyPressed(Input.Keys.D),
                Gdx.input.isKeyPressed(Input.Keys.Q),
                Gdx.input.isKeyPressed(Input.Keys.E));
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.use();
        }
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

        List<Light> lights = level.buildAllToRender(camera, pb, sb, 8);

        renderLights(lights, pb);
        renderShadows(lights, pb);
        sceneBuffer.begin();
        Gdx.gl.glViewport(0, 0, sceneBuffer.getWidth(), sceneBuffer.getHeight());
        renderScene(pb, sb);

        pb.reset();
        sb.reset();

        renderPlayerItem(sb);
        renderGui();

        sceneBuffer.end();

        scaledCamera.update();
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        Gdx.gl.glClearColor(C.FOG_COLOR.r, C.FOG_COLOR.g, C.FOG_COLOR.b, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        SpriteBatch sb2d = Art.i.spriteBatch2D;
        sb2d.setProjectionMatrix(scaledCamera.combined);
        sb2d.begin();
        sb2d.draw(sceneBuffer.getColorBufferTexture(), 0, 0);
        sb2d.end();
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
    }

    private void renderGui() {
        guiCamera.update();
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);

        SpriteBatch sb2d = Art.i.spriteBatch2D;
        sb2d.setProjectionMatrix(guiCamera.combined);
        sb2d.begin();


        int x = 2;
        int y = 2;

        Art.i.drawString(sb2d, "HP", x, y, Color.GOLD);
        Art.i.drawProgressBar(sb2d, x + 16, y, 6, C.BAR_HP_COLOR, C.BAR_EMPTY_COLOR, player.health / (float) player.maxHealth, 5);

        y += 6;

        Art.i.drawString(sb2d, "ST", x, y, Color.GOLD);
        Art.i.drawProgressBar(sb2d, x + 16, y, 6, C.BAR_ST_COLOR, C.BAR_EMPTY_COLOR, player.stamina / (float) player.maxStamina, 5);


        sb2d.end();
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
    }

    private void renderPlayerItem(SpriteBatch3D sb) {
        if (player.item == null) return;
        Vector3 f = camera.direction.cpy().scl(8f);
        Vector3 u = camera.up.cpy();
        Vector3 r = u.crs(f).nor();
        r.scl(player.turnBob * 32.0f + 4);
        float yy = (float) (Math.sin(player.bobPhase * 0.4) * 0.2 * player.bob + player.bob * 1) - 2;

        if (player.itemUseTime == 0) {
            yy -= 6;
        }

        player.item.animation.current().addSprite(player.x + f.x - r.x, yy, player.z + f.z - r.z, camera, sb);
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        sb.begin(Art.i.billboardShader, camera);
        sb.renderAndReset();
        sb.end();
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT /*| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0)*/);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glCullFace(GL20.GL_FRONT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);



        pb.begin(Art.i.planeShader, camera);
        level.applyToShader(Art.i.planeShader);
        pb.render();
        pb.end();

        sb.begin(Art.i.billboardShader, camera);
        level.applyToShader(Art.i.billboardShader);
        sb.render();
        sb.end();
    }


    @Override
    public void dispose() {
        super.dispose();
        R.i.dispose();
    }
}
