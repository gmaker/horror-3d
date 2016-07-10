package ru.znay.znay.tt.gfx.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by admin on 09.07.2016.
 */
public abstract class Shader {
    public final ShaderProgram shaderProgram;

    public Shader(ShaderProgram shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    public abstract void setUniforms(Camera camera);

    public final void begin(Camera camera) {
        shaderProgram.begin();
        setUniforms(camera);
    }

    public final void end() {
        shaderProgram.end();
    }
}
