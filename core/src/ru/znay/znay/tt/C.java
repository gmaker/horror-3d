package ru.znay.znay.tt;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by admin on 18.06.2016.
 */
public class C {
    public static final int WIDTH = 320;
    public static final int HEIGHT = WIDTH * 9 / 12 ;
    public static final int DESKTOP_SCALE = 1024 / WIDTH;

    public static final int DEPTH_MAP_SIZE = WIDTH;

    public static Color FOG_COLOR = new Color(0.0f, 0.0f, 0.1f, 1.0f);

    public static float BAR_ALPHA = 0.9f;
    public static Color BAR_HP_COLOR = new Color(1.0f, 0.3f, 0.1f, BAR_ALPHA);
    public static Color BAR_ST_COLOR = new Color(1.0f, 1.0f, 0.2f, BAR_ALPHA);
    public static Color BAR_EMPTY_COLOR = new Color(0.5f, 0.5f, 0.5f, BAR_ALPHA);

    public static final int TILE_SIZE = 16;
}
