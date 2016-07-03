package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.NumberUtils;


/**
 * Created by admin on 28.06.2016.
 */
public class PlaneBatch implements Disposable {

    static final int VERTEX_SIZE = 3 + 1 + 3 + 2;
    static final int PLANE_SIZE = 4 * VERTEX_SIZE;

    public Mesh mesh;
    private final float[] vertices;
    private ShaderProgram shader;
    private Color fogColor;
    private Texture texture;
    private int idx = 0;
    private float color;
    private float invTexWidth;
    private float invTexHeight;

    public PlaneBatch(int maxPlanes, ShaderProgram shader) {
        if (maxPlanes > 32767 / VERTEX_SIZE - ((32767 / VERTEX_SIZE) % 3))
            throw new IllegalArgumentException("Can't have planes per batch: " + maxPlanes);

        mesh = new Mesh(true, maxPlanes * 4, maxPlanes * 6,
                new VertexAttribute(0x01, 3, "a_pos"),
                new VertexAttribute(0x04, 4, "a_col"),
                new VertexAttribute(0x08, 3, "a_normal"),
                new VertexAttribute(0x10, 2, "a_uv"));

        vertices = new float[maxPlanes * PLANE_SIZE];

        short[] indices = new short[maxPlanes * 6];
        short j = 0;
        for (int i = 0; i < indices.length; i += 6, j += 4) {
            indices[i + 0] = (short) (j + 1);
            indices[i + 1] = (short) (j + 0);
            indices[i + 2] = (short) (j + 3);
            indices[i + 3] = (short) (j + 3);
            indices[i + 4] = (short) (j + 2);
            indices[i + 5] = (short) (j + 1);
        }

        mesh.setIndices(indices);
        this.shader = shader;
    }

    public void setFogColor(Color fogColor) {
        this.fogColor = fogColor;
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (int) (255 * a) << 24 | (int) (255 * b) << 16 | (int) (255 * g) << 8 | (int) (255 * r);
        color = NumberUtils.intToFloatColor(intBits);
    }

    public void addPlane(float x, float y, float z, float scale, float[] rawData, float sx, float sy, float sw, float sh) {
        addPlane(x, y, z, scale, scale, rawData, sx, sy, sw, sh);
    }

    public void addPlane(float x, float y, float z, float scaleXZ, float scaleY, float[] rawData, float sx, float sy, float sw, float sh) {
        final float u = sx * invTexWidth;
        final float u2 = (sx + sw) * invTexWidth;
        final float v = sy * invTexHeight;
        final float v2 = (sy + sh) * invTexHeight;

        float color = this.color;
        int idx = this.idx;
        int rdx = 0;
        vertices[idx++] = rawData[rdx++] * scaleXZ + x;
        vertices[idx++] = rawData[rdx++] * scaleY + y;
        vertices[idx++] = rawData[rdx++] * scaleXZ + z;
        vertices[idx++] = color;
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = u;
        vertices[idx++] = v;

        vertices[idx++] = rawData[rdx++] * scaleXZ + x;
        vertices[idx++] = rawData[rdx++] * scaleY + y;
        vertices[idx++] = rawData[rdx++] * scaleXZ + z;
        vertices[idx++] = color;
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = u;
        vertices[idx++] = v2;

        vertices[idx++] = rawData[rdx++] * scaleXZ + x;
        vertices[idx++] = rawData[rdx++] * scaleY + y;
        vertices[idx++] = rawData[rdx++] * scaleXZ + z;
        vertices[idx++] = color;
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = u2;
        vertices[idx++] = v2;

        vertices[idx++] = rawData[rdx++] * scaleXZ + x;
        vertices[idx++] = rawData[rdx++] * scaleY + y;
        vertices[idx++] = rawData[rdx++] * scaleXZ + z;
        vertices[idx++] = color;
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = rawData[rdx++];
        vertices[idx++] = u2;
        vertices[idx++] = v;
        this.idx = idx;
    }

    public void begin() {
        shader.begin();
    }

    public void end() {
        shader.end();
    }

    public void render(Camera camera, Matrix4 modelMatrix) {
        if (idx == 0) return;

        shader.setUniformf("u_fogColor", fogColor);
        shader.setUniformMatrix("u_projectMatrix", camera.projection);
        shader.setUniformMatrix("u_viewMatrix", camera.view);
        shader.setUniformMatrix("u_modelMatrix", modelMatrix);
        texture.bind(0);
        shader.setUniformi("u_texture", 0);
        Art.i.dithering.bind(1);
        shader.setUniformi("u_dithering", 1);

        int planesInBatch = idx / PLANE_SIZE;
        int count = planesInBatch * 6;

        Mesh mesh = this.mesh;
        mesh.setVertices(vertices, 0, idx);
        mesh.getIndicesBuffer().position(0);
        mesh.getIndicesBuffer().limit(count);

        mesh.render(shader, GL20.GL_TRIANGLES, 0, count);
    }

    public void renderAndReset(Camera camera, Matrix4 modelMatrix) {
        render(camera, modelMatrix);
        reset();
    }

    public void reset() {
        idx = 0;
    }

    public void dispose() {
        mesh.dispose();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        invTexWidth = 1.0f / texture.getWidth();
        invTexHeight = 1.0f / texture.getHeight();
    }
}
