package ru.znay.znay.tt.level.block;

import com.badlogic.gdx.math.Vector3;
import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.gfx.light.PointLight;
import ru.znay.znay.tt.level.Level;
import ru.znay.znay.tt.particle.FireParticle;
import ru.znay.znay.tt.particle.Particle;

/**
 * Created by admin on 12.07.2016.
 */
public class FireBlock extends Block {
    public PointLight pointLight;

    public FireBlock(Level level, int xt, int zt) {
        super(level, xt, zt);
        sprites.add(new Sprite3D(0, 0, 0, 0, 5 * 16, 16, 16));
        pointLight = new PointLight(new Vector3(xt * 16, 0, zt * 16), 64.0f);
    }

    @Override
    public void tick(Level level) {
        super.tick(level);
        if (level.tickTime % 2 == 0) {
            float x = (random.nextFloat() - 0.5f) * 5.0f;
            float z = (random.nextFloat() - 0.5f) * 5.0f;
            Particle p = new FireParticle(xt * 16 + x, -6, zt * 16 + z);
            level.addParticle(p);
        }
        if (level.tickTime % 60 == 0) {
            pointLight.needsUpdate = true;
        }
    }
}
