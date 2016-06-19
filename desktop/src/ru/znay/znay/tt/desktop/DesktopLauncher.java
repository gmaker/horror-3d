package ru.znay.znay.tt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.znay.znay.tt.C;
import ru.znay.znay.tt.Tranformers;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = C.WIDTH * C.DESKTOP_SCALE;
        config.height = C.HEIGHT * C.DESKTOP_SCALE;
        new LwjglApplication(new Tranformers(), config);
    }
}
