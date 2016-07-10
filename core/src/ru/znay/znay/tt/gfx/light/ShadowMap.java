package ru.znay.znay.tt.gfx.light;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import ru.znay.znay.tt.gfx.PlaneBatch;
import ru.znay.znay.tt.gfx.shader.Shader;
import ru.znay.znay.tt.tool.R;

import java.util.List;

/**
 * Created by admin on 10.07.2016.
 */
public class ShadowMap {
    public final Shader shader;
    public FrameBuffer frameBufferShadows;

    public ShadowMap(Shader shader) {
        this.shader = shader;
    }

    public void render(Camera camera, PlaneBatch pb, List<Light> lights) {
        if (frameBufferShadows == null) {
            frameBufferShadows = R.i.register(new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true));
        }

        frameBufferShadows.begin();

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        boolean firstCall = true;
        for (final Light light : lights) {
            shader.begin(camera);
            light.applyToShader(shader);
            shader.end();
            if (firstCall) {
                Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
                Gdx.gl.glDisable(GL20.GL_BLEND);

                pb.begin(shader, camera);
                pb.render();
                pb.end();

                firstCall = false;
            } else {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
                Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

                pb.begin(shader, camera);
                pb.render();
                pb.end();
            }
        }
        frameBufferShadows.end();
    }
}
