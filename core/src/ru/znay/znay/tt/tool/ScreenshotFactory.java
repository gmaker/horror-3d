package ru.znay.znay.tt.tool;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.ByteBuffer;

/**
 * Created by admin on 10.07.2016.
 */
public class ScreenshotFactory {

    private static int counter = 1;

    public static void saveTexture(Texture texture, String filePath) {
        FileHandle h = Gdx.files.absolute(filePath);
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        PixmapIO.writePNG(h, pixmap);
    }

    public static void saveScreenshot(final int w, final int h, final String prefix) {
        try {
            FileHandle fh;
            do {
                if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
                    fh = Gdx.files.local("bin/screenshot_" + prefix + "_" + counter++ + ".png");
                } else {
                    fh = Gdx.files.local("screenshot_" + prefix + "_" + counter++ + ".png");
                }
            }
            while (fh.exists());
            final Pixmap pixmap = getScreenshot(0, 0, w, h, true);
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();
            Gdx.app.log("screenshot", "Screenshot saved to " + fh);
        } catch (final Exception e) {
        }
    }

    private static Pixmap getScreenshot(final int x, final int y, final int w, final int h, final boolean yDown) {

        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

        if (yDown) {
            final ByteBuffer pixels = pixmap.getPixels();
            final int numBytes = w * h * 4;
            final byte[] lines = new byte[numBytes];
            final int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
        }

        return pixmap;
    }
}