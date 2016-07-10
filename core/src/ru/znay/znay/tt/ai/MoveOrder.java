package ru.znay.znay.tt.ai;

/**
 * Created by admin on 06.07.2016.
 */
public class MoveOrder extends Order {

    public final float x;
    public final float z;

    public MoveOrder(float x, float z) {
        this.x = x;
        this.z = z;
    }


    public void tick() {
        float xd = this.x - owner.x ;
        float zd = this.z  - owner.z;
        float dd = (float) Math.sqrt(xd * xd + zd * zd);
        owner.xa += xd / dd * owner.walkSpeed;
        owner.za += zd / dd * owner.walkSpeed;
    }

    public boolean isCompleted() {
        float xd = owner.x - this.x;
        float zd = owner.z - this.z;
        float dd = (float) Math.sqrt(xd * xd + zd * zd);
        return dd <= 4.0 || Math.random() < 0.01;
    }
}
