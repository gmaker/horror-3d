package ru.znay.znay.tt.gfx;

/**
 * Created by admin on 02.07.2016.
 */
public class ModelData {
    public static final float[] rawDataLeftOut = {
            -0.5f, 0.5f, -0.5f,
            -1.0f, 0.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,
            -1.0f, 0.0f, 0.0f,

            -0.5f, -0.5f, 0.5f,
            -1.0f, 0.0f, 0.0f,

            -0.5f, 0.5f, 0.5f,
            -1.0f, 0.0f, 0.0f,
    };

    public static final float[] rawDataRightOut = {
            0.5f, 0.5f, 0.5f,
            1.0f, 0.0f, 0.0f,

            0.5f, -0.5f, 0.5f,
            1.0f, 0.0f, 0.0f,

            0.5f, -0.5f, -0.5f,
            1.0f, 0.0f, 0.0f,

            0.5f, 0.5f, -0.5f,
            1.0f, 0.0f, 0.0f,
    };


    public static final float[] rawDataTopOut = {
            -0.5f, 0.5f, -0.5f,
            0.0f, 1.0f, 0.0f,

            -0.5f, 0.5f, 0.5f,
            0.0f, 1.0f, 0.0f,

            0.5f, 0.5f, 0.5f,
            0.0f, 1.0f, 0.0f,

            0.5f, 0.5f, -0.5f,
            0.0f, 1.0f, 0.0f,
    };
    public static final float[] rawDataBottomOut = {
            -0.5f, -0.5f, 0.5f,
            0.0f, -1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,
            0.0f, -1.0f, 0.0f,

            0.5f, -0.5f, -0.5f,
            0.0f, -1.0f, 0.0f,

            0.5f, -0.5f, 0.5f,
            0.0f, -1.0f, 0.0f,
    };
    public static final float[] rawDataFrontOut = {
            -0.5f, 0.5f, 0.5f,
            0.0f, 0.0f, 1.0f,

            -0.5f, -0.5f, 0.5f,
            0.0f, 0.0f, 1.0f,

            0.5f, -0.5f, 0.5f,
            0.0f, 0.0f, 1.0f,

            0.5f, 0.5f, 0.5f,
            0.0f, 0.0f, 1.0f,
    };

    public static final float[] rawDataBackOut = {
            0.5f, 0.5f, -0.5f,
            0.0f, 0.0f, -1.0f,

            0.5f, -0.5f, -0.5f,
            0.0f, 0.0f, -1.0f,

            -0.5f, -0.5f, -0.5f,
            0.0f, 0.0f, -1.0f,

            -0.5f, 0.5f, -0.5f,
            0.0f, 0.0f, -1.0f,
    };

    public static final float[] rawDataFrontIn = {
            0.5f, 0.5f, 0.5f,
            0.0f, 0.0f, -1.0f,

            0.5f, -0.5f, 0.5f,
            0.0f, 0.0f, -1.0f,

            -0.5f, -0.5f, 0.5f,
            0.0f, 0.0f, -1.0f,

            -0.5f, 0.5f, 0.5f,
            0.0f, 0.0f, -1.0f,
    };

    public static final float[] rawDataRightIn = {
            0.5f, 0.5f, -0.5f,
            -1.0f, 0.0f, 0.0f,

            0.5f, -0.5f, -0.5f,
            -1.0f, 0.0f, 0.0f,

            0.5f, -0.5f, 0.5f,
            -1.0f, 0.0f, 0.0f,

            0.5f, 0.5f, 0.5f,
            -1.0f, 0.0f, 0.0f,
    };

    public static final float[] rawDataTopIn = {
            -0.5f, 0.5f, 0.5f,
            0.0f, -1.0f, 0.0f,

            -0.5f, 0.5f, -0.5f,
            0.0f, -1.0f, 0.0f,

            0.5f, 0.5f, -0.5f,
            0.0f, -1.0f, 0.0f,

            0.5f, 0.5f, 0.5f,
            0.0f, -1.0f, 0.0f,
    };

    public static final float[] rawDataBottomIn = {
            -0.5f, -0.5f, -0.5f,
            0.0f, 1.0f, 0.0f,

            -0.5f, -0.5f, 0.5f,
            0.0f, 1.0f, 0.0f,

            0.5f, -0.5f, 0.5f,
            0.0f, 1.0f, 0.0f,

            0.5f, -0.5f, -0.5f,
            0.0f, 1.0f, 0.0f,
    };
}
