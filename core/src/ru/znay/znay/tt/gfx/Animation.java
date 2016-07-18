package ru.znay.znay.tt.gfx;

/**
 * Created by admin on 16.07.2016.
 */
public class Animation {
    private final int x;
    private final int y;
    private final int w;
    private final int h;
    public final int frames;
    public final int time;
    private int frame;
    private int tickTime;
    private final int xa;
    private final int ya;
    private boolean running;

    public Sprite3D sprite;

    public Animation(int x, int y, int w, int h, int xa, int ya, int frames, int time) {
        if (Math.abs(xa) > 1 || Math.abs(ya) > 1 || (xa != 0 && ya != 0))
            throw new IllegalStateException("Failed to get next sprite");

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.frames = frames;
        this.frame = 0;
        this.sprite = new Sprite3D(0, 0, 0, x, y, w, h);
        this.time = time;
        this.xa = xa;
        this.ya = ya;
        stop();
    }

    public void start() {
        this.running = true;
    }

    public void stop() {
        this.running = false;
        this.tickTime = 0;
        reset();
    }

    public Sprite3D reset() {
        this.frame = 0;
        return sprite.set(x, y, w, h);
    }

    public Sprite3D current() {
        return sprite;
    }

    private Sprite3D next() {
        frame = (frame + xa + ya) % frames;

        int sx = x + frame * xa * w;
        int sy = y + frame * ya * h;

        return sprite.set(sx, sy, w, h);
    }

    public void tick() {
        if (running) {
            tickTime++;
            if (tickTime % time == 0) {
                next();
            }
        }
    }
}
