package ru.znay.znay.tt.gfx.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import ru.znay.znay.tt.C;
import ru.znay.znay.tt.gfx.Art;

/**
 * Created by admin on 09.07.2016.
 */
public class BillboardShader extends Shader {

    public BillboardShader(ShaderProgram shaderProgram) {
        super(shaderProgram);
    }

    @Override
    public void setUniforms(Camera camera) {
        final int textureNum = 4;
        Art.i.shadowMap.frameBufferShadows.getColorBufferTexture().bind(textureNum);
        shaderProgram.setUniformi("u_shadows", textureNum);
        shaderProgram.setUniformf("u_screenWidth", Gdx.graphics.getWidth());
        shaderProgram.setUniformf("u_screenHeight", Gdx.graphics.getHeight());
        shaderProgram.setUniformf("u_fogColor", C.FOG_COLOR);
        shaderProgram.setUniformMatrix("u_projectMatrix", camera.projection);
        shaderProgram.setUniformMatrix("u_viewMatrix", camera.view);
        shaderProgram.setUniformi("u_texture", 0);
        Art.i.dithering.bind(1);
        shaderProgram.setUniformi("u_dithering", 1);

    }
}
