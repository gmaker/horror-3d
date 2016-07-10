package ru.znay.znay.tt.level.block;

import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.gfx.light.PointLight;
import ru.znay.znay.tt.level.Level;

import java.util.Random;

/**
 * Created by admin on 05.07.2016.
 */
public class TorchBlock extends Block {
    public Sprite3D torchSprite;
    public PointLight pointLight;
    public int tickTime = 0;

    public TorchBlock(int xt, int zt) {
        torchSprite = new Sprite3D(0, 0, 0, 0, 4 * 16, 16, 16);
        sprites.add(torchSprite);
        pointLight = new PointLight(new Vector3(xt * 16, +4.0f, zt * 16), 50.0f);
    }

    public void decorate(Level level, int x, int y) {
        Random random = new Random((x + y * 1000) * 341871231);
        float r = 7f;
        for (int i = 0; i < 1000; i++) {
            int face = random.nextInt(4);
            if (face == 0 && level.getBlock(x - 1, y).solidRender) {
                torchSprite.x -= r;
                break;
            }
            if (face == 1 && level.getBlock(x, y - 1).solidRender) {
                torchSprite.z -= r;
                break;
            }
            if (face == 2 && level.getBlock(x + 1, y).solidRender) {
                torchSprite.x += r;
                break;
            }
            if (face == 3 && level.getBlock(x, y + 1).solidRender) {
                torchSprite.z += r;
                break;
            }
        }
    }

    public void tick() {
        super.tick();
        if (random.nextInt(4) == 0) {
            int frame = random.nextInt(2);
            torchSprite.setSprite(frame * 16, 4 * 16, 16, 16);
        }
        if (tickTime++ % 60 == 0) {
            pointLight.needsUpdate = true;
        }
    }
}
