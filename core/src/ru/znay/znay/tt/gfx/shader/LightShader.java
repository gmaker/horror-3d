package ru.znay.znay.tt.gfx.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by admin on 09.07.2016.
 */
public class LightShader extends Shader {
    public LightShader(ShaderProgram shaderProgram) {
        super(shaderProgram);
    }

    public void setUniforms(Camera camera) {
        shaderProgram.setUniformMatrix("u_projectMatrix", camera.projection);
        shaderProgram.setUniformMatrix("u_viewMatrix", camera.view);
        shaderProgram.setUniformf("u_cameraFar", camera.far);
        shaderProgram.setUniformf("u_lightPos", camera.position);
    }
}
