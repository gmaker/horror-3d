package ru.znay.znay.tt.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;

import java.nio.IntBuffer;

/**
 * Created by admin on 09.07.2016.
 */
public class FrameBufferCubeMap implements Disposable {
    protected Cubemap colorTexture;
    private int framebufferHandle;
    private int depthbufferHandle;
    private int stencilbufferHandle;
    private static int defaultFramebufferHandle = 0;

    protected final int sz;
    protected final boolean hasDepth;
    protected final boolean hasStencil;
    protected final Pixmap.Format format;

    public FrameBufferCubeMap(final Pixmap.Format format, final int sz, final boolean hasDepth, final boolean hasStencil) {
        this.sz = sz;
        this.format = format;
        this.hasDepth = hasDepth;
        this.hasStencil = hasStencil;
        colorTexture = new Cubemap(sz, sz, sz, format);
        colorTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        colorTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        build();
    }

    private void build() {
        final GL20 gl = Gdx.gl20;

        final IntBuffer handle = BufferUtils.newIntBuffer(1);
        gl.glGenFramebuffers(1, handle);
        framebufferHandle = handle.get(0);

        if (hasDepth) {
            handle.clear();
            gl.glGenRenderbuffers(1, handle);
            depthbufferHandle = handle.get(0);
        }
        if (hasStencil) {
            handle.clear();
            gl.glGenRenderbuffers(1, handle);
            stencilbufferHandle = handle.get(0);
        }

        gl.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, colorTexture.getTextureObjectHandle());

        if (hasDepth) {
            gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, depthbufferHandle);
            gl.glRenderbufferStorage(GL20.GL_RENDERBUFFER, GL20.GL_DEPTH_COMPONENT16, colorTexture.getWidth(), colorTexture.getHeight());
        }
        if (hasStencil) {
            gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, stencilbufferHandle);
            gl.glRenderbufferStorage(GL20.GL_RENDERBUFFER, GL20.GL_STENCIL_INDEX8, colorTexture.getWidth(), colorTexture.getHeight());
        }

        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebufferHandle);
        gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_CUBE_MAP, colorTexture.getTextureObjectHandle(), 0);
        if (hasDepth) {
            gl.glFramebufferRenderbuffer(GL20.GL_FRAMEBUFFER, GL20.GL_DEPTH_ATTACHMENT, GL20.GL_RENDERBUFFER, depthbufferHandle);
        }
        if (hasStencil) {
            gl.glFramebufferRenderbuffer(GL20.GL_FRAMEBUFFER, GL20.GL_STENCIL_ATTACHMENT, GL20.GL_RENDERBUFFER, stencilbufferHandle);
        }

        gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, 0);
        gl.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, 0);
        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, defaultFramebufferHandle);

        final int result = gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER);

        if (result != GL20.GL_FRAMEBUFFER_COMPLETE) {
            colorTexture.dispose();
            if (hasDepth) {
                handle.clear();
                handle.put(depthbufferHandle);
                handle.flip();
                gl.glDeleteRenderbuffers(1, handle);
            }

            if (hasStencil) {
                handle.clear();
                handle.put(stencilbufferHandle);
                handle.flip();
                gl.glDeleteRenderbuffers(1, handle);
            }

            handle.clear();
            handle.put(framebufferHandle);
            handle.flip();
            gl.glDeleteFramebuffers(1, handle);

            if (result == GL20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT) {
                throw new IllegalStateException("frame buffer couldn't be constructed: incomplete attachment");
            }
            if (result == GL20.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS) {
                throw new IllegalStateException("frame buffer couldn't be constructed: incomplete dimensions");
            }
            if (result == GL20.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT) {
                throw new IllegalStateException("frame buffer couldn't be constructed: missing attachment");
            }
            if (result == GL20.GL_FRAMEBUFFER_UNSUPPORTED) {
                throw new IllegalStateException("frame buffer couldn't be constructed: unsupported combination of formats");
            }
            throw new IllegalStateException("frame buffer couldn't be constructed: unknown error " + result);
        }
    }

    public void bind(final int side) {
        Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebufferHandle);
        Gdx.gl20.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, side, colorTexture.getTextureObjectHandle(), 0);
    }

    public static void unbind() {
        Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, defaultFramebufferHandle);
    }

    public void begin(final Cubemap.CubemapSide side, final Camera camera) {
        begin(side.glEnum);
        if (camera != null) {
            switch (side) {
                case NegativeX:
                    camera.up.set(0, -1, 0);
                    camera.direction.set(-1, 0, 0);
                    break;
                case NegativeY:
                    camera.up.set(0, 0, -1);
                    camera.direction.set(0, -1, 0);
                    break;
                case NegativeZ:
                    camera.up.set(0, -1, 0);
                    camera.direction.set(0, 0, -1);
                    break;
                case PositiveX:
                    camera.up.set(0, -1, 0);
                    camera.direction.set(1, 0, 0);
                    break;
                case PositiveY:
                    camera.up.set(0, 0, 1);
                    camera.direction.set(0, 1, 0);
                    break;
                case PositiveZ:
                    camera.up.set(0, -1, 0);
                    camera.direction.set(0, 0, 1);
                    break;
                default:
                    break;
            }
            camera.update();
        }
    }

    private void begin(final int side) {
        bind(side);
        setFrameBufferViewport();
    }

    protected void setFrameBufferViewport() {
        Gdx.gl20.glViewport(0, 0, colorTexture.getWidth(), colorTexture.getHeight());
    }

    public void end() {
        unbind();
        setDefaultFrameBufferViewport();
    }

    protected void setDefaultFrameBufferViewport() {
        Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void end(final int x, final int y, final int width, final int height) {
        unbind();
        Gdx.gl20.glViewport(x, y, width, height);
    }

    public Cubemap getColorBufferTexture() {
        return colorTexture;
    }

    public int getHeight() {
        return colorTexture.getHeight();
    }

    public int getWidth() {
        return colorTexture.getWidth();
    }

    public void dispose() {
        final GL20 gl = Gdx.gl20;

        final IntBuffer handle = BufferUtils.newIntBuffer(1);

        colorTexture.dispose();
        if (hasDepth) {
            handle.put(depthbufferHandle);
            handle.flip();
            gl.glDeleteRenderbuffers(1, handle);
        }

        if (hasStencil) {
            handle.put(stencilbufferHandle);
            handle.flip();
            gl.glDeleteRenderbuffers(1, handle);
        }

        handle.clear();
        handle.put(framebufferHandle);
        handle.flip();
        gl.glDeleteFramebuffers(1, handle);
    }
}
