package ru.znay.znay.tt.entity.ore;

import ru.znay.znay.tt.entity.Entity;
import ru.znay.znay.tt.entity.Player;
import ru.znay.znay.tt.entity.item.Item;
import ru.znay.znay.tt.gfx.Sprite3D;
import ru.znay.znay.tt.particle.CrashParticle;
import ru.znay.znay.tt.particle.SparkParticle;

/**
 * Created by admin on 17.07.2016.
 */
public class Ore extends Entity {
    private int spriteLife = 5;

    public Ore(float x, float y, float z) {
        super(x, y, z);
        for (int i = 0; i < 1; i++) {
            float xx = (random.nextFloat() - 0.5f) * 4.0f;
            float zz = (random.nextFloat() - 0.5f) * 4.0f;
            Sprite3D sprite = new Sprite3D(xx, 0, zz, 16, 16 * 2, 16, 16);
            sprites.add(sprite);
        }
    }

    public void interactWith(Player player, Item item, float xx, int yy, float zz) {
        if (removed) return;
        if (item == Item.pick && player.payStamina(4)) {
            for (int j = 0; j < 10; j++) {
                level.addParticle(new SparkParticle(xx, yy, zz));
            }
            spriteLife--;
            if (spriteLife <= 0) {
                if (sprites.size() > 0) {
                    Sprite3D sprite = sprites.get(0);
                    for (CrashParticle crashParticle : CrashParticle.crashParticles(x, y, z, sprite, 4)) {
                        level.addParticle(crashParticle);

                    }
                    sprites.remove(0);
                }
                if (sprites.size() == 0) {
                    remove();
                }
                spriteLife = 5;
            }

        }
    }
}
