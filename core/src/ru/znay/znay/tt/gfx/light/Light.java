package ru.znay.znay.tt.gfx.light;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.gfx.PlaneBatch;
import ru.znay.znay.tt.gfx.shader.Shader;

/**
 * Created by admin on 09.07.2016.
 */
public abstract class Light {
    protected final Shader shader;
    public final Vector3 pos;
    protected Camera camera;
    public boolean needsUpdate = true;

    public Light(Shader shader, Vector3 pos) {
        this.shader = shader;
        this.pos = pos;
        initCamera();
    }

    public abstract void applyToShader(Shader sceneShader);

    public abstract void initCamera();

    public abstract void render(PlaneBatch pb);
}
