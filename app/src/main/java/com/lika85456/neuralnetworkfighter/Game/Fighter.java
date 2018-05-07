package com.lika85456.neuralnetworkfighter.Game;

import java.util.ArrayList;

public class Fighter {
    public float FoV = 45f;
    public boolean seeBullet = false;
    public boolean seeEnemy = false;
    public float hp = 1f;
    public float x;
    public float y;
    public float angle; //angle is from 0f to 1f
    public float velocity;
    public int id;
    private boolean hasLoadedBullet = true;
    private long shootTime;

    public Fighter(int id) {
        this.id = id;
    }

    public void move() {
        x += Math.sin(angle * 2f * Math.PI) * velocity;
        y += Math.cos(angle * 2f * Math.PI) * velocity;
        if (x > Game.size) x = Game.size;
        if (x < 0) x = 0;
        if (y > Game.size) y = Game.size;
        if (y < 0) y = 0;
    }

    public void shooted() {
        shootTime = System.currentTimeMillis();
        hasLoadedBullet = false;
    }

    public void getHitted() {
        hp -= 0.2f;
    }

    public boolean canShoot() {
        if (hasLoadedBullet == false) {
            if (System.currentTimeMillis() > shootTime + Game.reloadingTime) {
                hasLoadedBullet = true;
                return true;
            } else
                return false;
        } else
            return true;
    }

    public void setFoV(float fov) {
        if (fov > 45f) fov = 45f;
        if (fov < 45f) fov = 1f;
        this.FoV = fov;
    }

    public boolean canSeeEnemy(Fighter[] fighters) {
        for (int i = 0; i < fighters.length; i++) {
            Fighter player = fighters[i];
            if (player.id == id) continue;
            float tAngle = getAngle(x, y, player.x, player.y); // in degrees
            if (tAngle > this.angle * 360f - FoV / 2 && tAngle < this.angle * 360f + FoV / 2)
                return true;
        }
        return false;
    }

    public boolean canSeeBullet(ArrayList<Bullet> bullets) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (bullet.ownerId == id) continue;
            float tAngle = getAngle(x, y, bullet.x, bullet.y); // in degrees
            if (tAngle > this.angle * 360f - FoV / 2 && tAngle < this.angle * 360f + FoV / 2)
                return true;
        }
        return false;
    }

    private float getAngle(float x, float y, float xx, float yy) {
        float angle = (float) Math.toDegrees(Math.atan2(yy - y, xx - x));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }
}
