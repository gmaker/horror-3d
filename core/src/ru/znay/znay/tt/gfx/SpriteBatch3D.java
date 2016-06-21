package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.NumberUtils;

/**
 * Created by admin on 19.06.2016.
 */
public class SpriteBatch3D {

    @Deprecated
    public static Mesh.VertexDataType defaultVertexDataType = Mesh.VertexDataType.VertexArray;

    /*

        x, y, z,
        color,
        u, v,
        xo, yo

     */
    static final int VERTEX_SIZE = 3 + 1 + 2 + 2;
    static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
    private Mesh mesh;

    final float[] vertices;
    int idx = 0;

    boolean drawing = false;

    private final Matrix4 projectMatrix = new Matrix4();
    private final Matrix4 viewMatrix = new Matrix4();

    private final ShaderProgram shader;

    float color = Color.WHITE.toFloatBits();
    private Color tempColor = new Color(1, 1, 1, 1);

    public int renderCalls = 0;
    public int totalRenderCalls = 0;
    public int maxSpritesInBatch = 0;
    public Color fogColor;
    public BoundingBox bb = new BoundingBox();

    public SpriteBatch3D(int size, ShaderProgram shader) {
        // 32767 is max index, so 32767 / 8 - (32767 / 8 % 3) = 4095.
        if (size > 4095) throw new IllegalArgumentException("Can't have more than 4095 sprites per batch: " + size);

        Mesh.VertexDataType vertexDataType = (Gdx.gl30 != null) ? Mesh.VertexDataType.VertexBufferObjectWithVAO : defaultVertexDataType;

        mesh = new Mesh(vertexDataType, false, size * 4, size * 6,
                new VertexAttribute(0x01, 3, "a_pos"),
                new VertexAttribute(0x04, 4, "a_col"),
                new VertexAttribute(0x02, 2, "a_uv"),
                new VertexAttribute(0x08, 2, "a_offs"));

        vertices = new float[size * SPRITE_SIZE];

        int len = size * 6;
        short[] indices = new short[len];
        short j = 0;
        for (int i = 0; i < len; i += 6, j += 4) {
            indices[i + 0] = j;
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 2);
            indices[i + 3] = (short) (j + 2);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = j;
        }
        mesh.setIndices(indices);
        this.shader = shader;
    }

    public void setFogColor(Color fogColor) {
        this.fogColor = fogColor;
    }

    public void begin() {
        if (drawing) throw new IllegalStateException("SpriteBatch3D.end must be called before begin.");
        renderCalls = 0;

        shader.begin();

        drawing = true;
    }

    public void end() {
        if (!drawing) throw new IllegalStateException("SpriteBatch3D.begin must be called before end.");
        drawing = false;

        shader.end();
    }

    public void setColor(Color tint) {
        color = tint.toFloatBits();
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (int) (255 * a) << 24 | (int) (255 * b) << 16 | (int) (255 * g) << 8 | (int) (255 * r);
        color = NumberUtils.intToFloatColor(intBits);
    }

    public void setColor(float color) {
        this.color = color;
    }

    public Color getColor() {
        int intBits = NumberUtils.floatToIntColor(color);
        Color color = tempColor;
        color.r = (intBits & 0xff) / 255f;
        color.g = ((intBits >>> 8) & 0xff) / 255f;
        color.b = ((intBits >>> 16) & 0xff) / 255f;
        color.a = ((intBits >>> 24) & 0xff) / 255f;
        return color;
    }

    public float getPackedColor() {
        return color;
    }

    public void addSprite(float x, float y, float z, int sprite) {
        addSprite(x, y, z, (sprite % 8) * 16, (sprite / 8) * 16, 16, 16);
    }

    public void addSprite(float x, float y, float z, float sx, float sy, float w, float h) {

        float[] vertices = this.vertices;

        Texture texture = Art.i.sheet;

        final float xo = -w / 2.0f;
        final float yo = -h / 2.0f;
        bb.ext(new Vector3(x, y, z));

        final float invTexWidth = 1f / texture.getWidth();
        final float invTexHeight = 1f / texture.getHeight();

        final float u = sx * invTexWidth;
        final float u2 = (sx + w) * invTexWidth;
        final float v = (sy + h) * invTexHeight;
        final float v2 = sy * invTexHeight;

        float color = this.color;
        int idx = this.idx;
        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = z;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v;
        vertices[idx++] = xo + 0;
        vertices[idx++] = yo + 0;

        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = z;
        vertices[idx++] = color;
        vertices[idx++] = u;
        vertices[idx++] = v2;
        vertices[idx++] = xo + 0;
        vertices[idx++] = yo + h;

        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = z;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v2;
        vertices[idx++] = xo + w;
        vertices[idx++] = yo + h;

        vertices[idx++] = x;
        vertices[idx++] = y;
        vertices[idx++] = z;
        vertices[idx++] = color;
        vertices[idx++] = u2;
        vertices[idx++] = v;
        vertices[idx++] = xo + w;
        vertices[idx++] = yo + 0;
        this.idx = idx;
    }

    public void render(Matrix4 modelMatrix) {
        if (idx == 0) return;

        shader.setUniform3fv("u_fogColor", new float[]{fogColor.r, fogColor.g, fogColor.b}, 0, 3);
        shader.setUniformMatrix("u_projectMatrix", projectMatrix);
        shader.setUniformMatrix("u_viewMatrix", viewMatrix);
        shader.setUniformMatrix("u_modelMatrix", modelMatrix);
        shader.setUniformi("u_texture", 0);

        renderCalls++;
        totalRenderCalls++;
        int spritesInBatch = idx / SPRITE_SIZE;
        if (spritesInBatch > maxSpritesInBatch) maxSpritesInBatch = spritesInBatch;
        int count = spritesInBatch * 6;

        Art.i.sheet.bind();
        Mesh mesh = this.mesh;
        mesh.setVertices(vertices, 0, idx);
        mesh.getIndicesBuffer().position(0);
        mesh.getIndicesBuffer().limit(count);

        mesh.render(shader, GL20.GL_TRIANGLES, 0, count);
    }

    public void reset() {
        idx = 0;
        bb.clr();
    }

    public void renderAndReset(Matrix4 modelMatrix) {
        render(modelMatrix);
        reset();
    }

    public void dispose() {
        mesh.dispose();
    }

    public void setProjectMatrix(Matrix4 project) {
        projectMatrix.set(project);
    }

    public void setViewMatrix(Matrix4 view) {
        viewMatrix.set(view);
    }
}

