package ru.znay.znay.tt.gfx.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.C;
import ru.znay.znay.tt.gfx.Art;
import ru.znay.znay.tt.gfx.FrameBufferCubeMap;
import ru.znay.znay.tt.gfx.PlaneBatch;
import ru.znay.znay.tt.gfx.shader.Shader;

/**
 * Created by admin on 09.07.2016.
 */
public class PointLight extends Light {
    public FrameBufferCubeMap frameBuffer;
    public Cubemap depthMap;
    public float radius;

    public PointLight(Vector3 pos, float radius) {
        super(Art.i.lightShader, pos);
        this.radius = radius;
    }

    public void applyToShader(Shader sceneShader) {
        final int textureNum = 2;
        depthMap.bind(textureNum);
        sceneShader.shaderProgram.setUniformf("u_type", 2);
        sceneShader.shaderProgram.setUniformi("u_depthMapCube", textureNum);
        sceneShader.shaderProgram.setUniformf("u_cameraFar", camera.far);
        sceneShader.shaderProgram.setUniformf("u_lightPos", pos);
    }

    public void initCamera() {
        camera = new PerspectiveCamera(90f, C.DEPTH_MAP_SIZE, C.DEPTH_MAP_SIZE);
        camera.near = 1f;
        camera.far =32.0f;
        camera.position.set(pos);
        camera.update();
    }

    public void render(PlaneBatch pb) {
        if (!needsUpdate) return;
        needsUpdate = false;

        if (frameBuffer == null) {
            frameBuffer = new FrameBufferCubeMap(Pixmap.Format.RGBA8888, C.DEPTH_MAP_SIZE, true, false);
        }
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
        Gdx.gl.glCullFace(GL20.GL_FRONT);
        for (int s = 0; s <= 5; s++) {
            final Cubemap.CubemapSide side = Cubemap.CubemapSide.values()[s];
            frameBuffer.begin(side, camera);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            pb.begin(shader, camera);
            pb.render();
            pb.end();
        }

        frameBuffer.end();
        depthMap = frameBuffer.getColorBufferTexture();
    }
}
