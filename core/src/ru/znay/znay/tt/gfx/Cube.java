package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.NumberUtils;


/**
 * Created by admin on 28.06.2016.
 */
public class Cube implements Disposable {

    public Mesh mesh;
    private final float[] vertices;
    private ShaderProgram shader;
    private Matrix4 projectMatrix;
    private Matrix4 viewMatrix;
    private Color fogColor;
    private Texture texture;

    public Cube(ShaderProgram shader) {
        int planes = 6;
        int maxVertices = planes * 4;
        int maxIndices = planes * 6;

        mesh = new Mesh(true, maxVertices, maxIndices,
                new VertexAttribute(0x01, 3, "a_pos"),
                new VertexAttribute(0x04, 4, "a_col"),
                new VertexAttribute(0x08, 3, "a_normal"),
                new VertexAttribute(0x10, 2, "a_uv"));

        vertices = new float[maxVertices * (3 + 1 + 3 + 2)];

        float[] vertexes = {
                //bottom
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,

                //top
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,

                //back
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,

                //front
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                //left
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,

                //right
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
        };

        float[] normals = {
                0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f,

                0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,

                -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,

                1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f
        };

        float[] uvs = {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        float color = NumberUtils.intToFloatColor(0xffffffff);

        int pIdx = 0;
        int nIdx = 0;
        int tIdx = 0;
        for (int i = 0; i < vertices.length; ) {
            vertices[i++] = vertexes[pIdx++];
            vertices[i++] = vertexes[pIdx++];
            vertices[i++] = vertexes[pIdx++];
            vertices[i++] = color;
            vertices[i++] = normals[nIdx++];
            vertices[i++] = normals[nIdx++];
            vertices[i++] = normals[nIdx++];
            vertices[i++] = uvs[tIdx++ % uvs.length];
            vertices[i++] = uvs[tIdx++ % uvs.length];
        }

        short[] indices = new short[maxIndices];
        short j = 0;
        for (int i = 0; i < maxIndices; i += 6, j += 4) {
            indices[i + 0] = (short) (j + 1);
            indices[i + 1] = (short) (j + 0);
            indices[i + 2] = (short) (j + 3);
            indices[i + 3] = (short) (j + 3);
            indices[i + 4] = (short) (j + 2);
            indices[i + 5] = (short) (j + 1);
        }

        mesh.setIndices(indices);
        mesh.setVertices(vertices);
        this.shader = shader;
    }

    public void setFogColor(Color fogColor) {
        this.fogColor = fogColor;
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (int) (255 * a) << 24 | (int) (255 * b) << 16 | (int) (255 * g) << 8 | (int) (255 * r);
        float color = NumberUtils.intToFloatColor(intBits);
        for (int i = 0; i < vertices.length; i += 9) {
            vertices[i + 3] = color;
        }
    }

    public void begin(Camera camera) {
        this.projectMatrix = camera.projection;
        this.viewMatrix = camera.view;
        shader.begin();
    }

    public void end() {
        shader.end();
    }

    public void render(Matrix4 modelMatrix, int block) {
        shader.setUniformf("u_fogColor", fogColor);
        shader.setUniformMatrix("u_projectMatrix", projectMatrix);
        shader.setUniformMatrix("u_viewMatrix", viewMatrix);
        shader.setUniformMatrix("u_modelMatrix", modelMatrix);
        shader.setUniformf("u_block", block);
        texture.bind(0);
        shader.setUniformi("u_texture", 0);
        Art.i.dithering.bind(1);
        shader.setUniformi("u_dithering", 1);

        mesh.setVertices(vertices);
        mesh.render(shader, GL20.GL_TRIANGLES, 0, 6 * 6);
    }

    public void dispose() {
        mesh.dispose();
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
