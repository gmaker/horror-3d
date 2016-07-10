package ru.znay.znay.tt.ai;

/**
 * Created by admin on 06.07.2016.
 */
public class IdleOrder extends Order {
    @Override
    public void tick() {

    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
