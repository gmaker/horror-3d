package ru.znay.znay.tt.gfx.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by admin on 10.07.2016.
 */
public class ShadowShader extends Shader {
    public ShadowShader(ShaderProgram shaderProgram) {
        super(shaderProgram);
    }

    @Override
    public void setUniforms(Camera camera) {
        shaderProgram.setUniformMatrix("u_projectMatrix", camera.projection);
        shaderProgram.setUniformMatrix("u_viewMatrix", camera.view);
    }
}
