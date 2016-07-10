package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.NumberUtils;
import ru.znay.znay.tt.gfx.shader.Shader;

/**
 * Created by admin on 19.06.2016.
 */
public class SpriteBatch3D implements Disposable{

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

    private Shader shader;

    float color = Color.WHITE.toFloatBits();
    public int renderCalls = 0;
    public int totalRenderCalls = 0;
    public int maxSpritesInBatch = 0;

    private Texture texture;
    private float invTexWidth;
    private float invTexHeight;

    public SpriteBatch3D(int size, Texture texture) {
        // 32767 is max index, so 32767 / 8 - (32767 / 8 % 3) = 4095.
        if (size > 32767 / VERTEX_SIZE - ((32767 / VERTEX_SIZE) % 3)) throw new IllegalArgumentException("Can't have more than 4095 sprites per batch: " + size);

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
        setTexture(texture);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
        invTexWidth = 1.0f / texture.getWidth();
        invTexHeight = 1.0f / texture.getHeight();
    }

    public void begin(Shader shader, Camera camera) {
        this.shader = shader;
        renderCalls = 0;

        shader.begin(camera);

        drawing = true;
    }

    public void end() {
        shader.end();
    }

    public void setColor(float r, float g, float b, float a) {
        int intBits = (int) (255 * a) << 24 | (int) (255 * b) << 16 | (int) (255 * g) << 8 | (int) (255 * r);
        color = NumberUtils.intToFloatColor(intBits);
    }

    public void addSprite(float x, float y, float z, float sx, float sy, float w, float h) {
        final float xo = -w / 2.0f;
        final float yo = -h / 2.0f;
        addSprite(x, y, z, sx, sy, w, h, xo, yo);
    }

    public void addSprite(float x, float y, float z, float sx, float sy, float w, float h, float xo, float yo) {
        float[] vertices = this.vertices;

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

    public void render() {
        if (idx == 0) return;

        texture.bind(0);

        renderCalls++;
        totalRenderCalls++;
        int spritesInBatch = idx / SPRITE_SIZE;
        if (spritesInBatch > maxSpritesInBatch) maxSpritesInBatch = spritesInBatch;
        int count = spritesInBatch * 6;

        Mesh mesh = this.mesh;
        mesh.setVertices(vertices, 0, idx);
        mesh.getIndicesBuffer().position(0);
        mesh.getIndicesBuffer().limit(count);

        mesh.render(shader.shaderProgram, GL20.GL_TRIANGLES, 0, count);
    }

    public void reset() {
        idx = 0;
    }

    public void renderAndReset() {
        render();
        reset();
    }

    public void dispose() {
        mesh.dispose();
    }
}

