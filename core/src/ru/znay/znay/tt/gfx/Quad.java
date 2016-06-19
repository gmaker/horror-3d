package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by admin on 19.06.2016.
 */
public class Quad extends DepthSpriteBatch {
    private Vector3 tempVector = new Vector3();

    public Quad(int batchSize, ShaderProgram shader) {
        super(batchSize, shader);
    }

    public void setFogColor(float r, float g, float b) {
        getShader().setUniform3fv("u_fogColor", new float[]{r, g, b}, 0, 3);
    }

    public void renderBillboard(Camera camera, float x, float y, float z, int sprite) {
        renderBillboard(camera, x, y, z, 1.0f, 1.0f, 1.0f, 1.0f, sprite);
    }

    public void renderBillboard(Camera camera, float x, float y, float z, float r, float g, float b, float a, int sprite) {
        Matrix4 transform = new Matrix4();
        tempVector.set(x, y, z);
        Vector3 camPos = camera.position.cpy();
        Vector3 camDir = camera.direction.cpy();
        transform.setToTranslation(tempVector).rotate(Vector3.Z, camPos.add(camDir).sub(tempVector).nor());
        transform.translate(-Art.SPRITE_SIZE / 2, -Art.SPRITE_SIZE / 2, 0);
        transform.getTranslation(tempVector);

        if (camera.frustum.sphereInFrustum(tempVector, Art.SPRITE_SIZE)) {
            setTransformMatrix(transform);
            this.setColor(r, g, b, a);
            this.draw(Art.i.sprites[sprite], 0, 0, 0.5f);
        }
    }

    public void render(Camera camera, float x, float y, float z, int sprite) {
        render(camera, x, y, z, 1.0f, 1.0f, 1.0f, 1.0f, sprite);
    }

    public void render(Camera camera, float x, float y, float z, float r, float g, float b, float a, int sprite) {
        Matrix4 transform = new Matrix4();
        transform.translate(x - Art.SPRITE_SIZE / 2, y - Art.SPRITE_SIZE / 2, z);
        transform.getTranslation(tempVector);
        if (camera.frustum.sphereInFrustum(tempVector.add(Art.SPRITE_SIZE / 2, Art.SPRITE_SIZE / 2, 0), Art.SPRITE_SIZE / 2)) {
            setTransformMatrix(transform);
            this.setColor(r, g, b, a);
            this.draw(Art.i.sprites[sprite], 0, 0, 0.0f);
        }
    }
}
