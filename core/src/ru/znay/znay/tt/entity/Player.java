package ru.znay.znay.tt.entity;

import ru.znay.znay.tt.ai.MoveOrder;
import ru.znay.znay.tt.ai.Order;
import ru.znay.znay.tt.entity.item.Item;
import ru.znay.znay.tt.level.block.Block;
import ru.znay.znay.tt.particle.Particle;
import ru.znay.znay.tt.particle.SparkParticle;

import java.util.List;

/**
 * Created by admin on 03.07.2016.
 */
public class Player extends Mob {
    public float slope = 0f;
    public float bob = 0.0f;
    public float bobPhase = 0.0f;
    public float turnBob = 0.0f;
    public Item item = Item.pick;
    public int itemUseTime = 0;
    public int stamina;
    public int staminaRecharge;
    public int staminaRechargeDelay;
    public int maxStamina = 10;

    public Player(float x, float y, float z) {
        super(x, y, z);
        rot = (float) -Math.PI / 2.0f;
        stamina = maxStamina;
        spawnTime = 0;
    }

    public void tick(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight) {
        if (item != null) {
            item.tick();
            if (itemUseTime > 0) {
                item.animation.start();
                itemUseTime--;
                if (itemUseTime == 0) {
                    interact(x - (float) Math.sin(rot) * 8.0f, -4 - random.nextInt(3), z - (float) Math.cos(rot) * 8.0f);
                    item.animation.stop();
                }
            }
        }

        if (stamina <= 0 && staminaRechargeDelay == 0 && staminaRecharge == 0) {
            staminaRechargeDelay = 40;
        }

        if (staminaRechargeDelay > 0) {
            staminaRechargeDelay--;
        }

        if (staminaRechargeDelay == 0) {
            staminaRecharge++;
            while (staminaRecharge > 10) {
                staminaRecharge -= 10;
                if (stamina < maxStamina) stamina++;
            }
        }

        if (turnLeft) rotA += rotSpeed;
        if (turnRight) rotA -= rotSpeed;

        float xm = 0;
        float zm = 0;
        if (up) zm--;
        if (down) zm++;
        if (left) xm--;
        if (right) xm++;
        float dd = xm * xm + zm * zm;
        if (dd > 0) {
            dd = (float) Math.sqrt(dd);
            bob += dd;
            bobPhase += dd;
        } else dd = 1;
        xm /= dd;
        zm /= dd;

        if (staminaRechargeDelay % 2 == 0) {
            xa += (xm * Math.cos(rot) + zm * Math.sin(rot)) * walkSpeed;
            za += (zm * Math.cos(rot) - xm * Math.sin(rot)) * walkSpeed;
        }

        move();

        float friction = 0.6f;
        xa *= friction;
        za *= friction;
        bob *= 0.6;
        turnBob += rotA;
        turnBob *= 0.8;
        rot += rotA;
        rotA *= 0.4;

        y = -0.2f + (float) Math.sin(bobPhase * 0.3) * 0.06f * bob;
    }

    private void interact(float xx, int yy, float zz) {
        int xc = (int) (Math.floor(xx + 8.0f) / 16.0f);
        int zc = (int) (Math.floor(zz + 8.0f) / 16.0f);
        int rr = 2;
        for (int z = zc - rr; z <= zc + rr; z++) {
            for (int x = xc - rr; x <= xc + rr; x++) {
                List<Entity> es = level.getBlock(x, z).entities;
                for (int i = 0; i < es.size(); i++) {
                    Entity e = es.get(i);
                    if (e == this) continue;
                    if (e.blocks(this, xx, zz, r)) {
                        e.interactWith(this, item, xx, yy, zz);
                    }
                }
            }
        }
        Block block = level.getBlock(xc, zc);
        block.interactWith(this, item, xx, yy, zz);
    }

    public void use() {
        if (stamina == 0) return;
        if (item == null) return;
        if (itemUseTime > 0) return;
        stamina--;
        staminaRecharge = 0;
        itemUseTime = item.animation.time * item.animation.frames;
    }

    public boolean payStamina(int cost) {
        if (cost > stamina) return false;
        stamina -= cost;
        return true;
    }
}
