package com.lika85456.neuralnetworkfighter.Game;

public class Bullet {
    public float startX;
    public float startY;
    public float angle;

    public float x;
    public float y;
    public int ownerId;

    public Bullet(float x, float y, float angle, int ownerId) {
        this.x = x;
        this.y = y;
        startX = x;
        startY = y;
        this.angle = angle;
        this.ownerId = ownerId;
    }

    public void move() {
        x += Math.sin(angle * 2f * Math.PI) * Game.bulletVelocity;
        y += Math.cos(angle * 2f * Math.PI) * Game.bulletVelocity;
    }

    public boolean isOut() {
        return x > Game.size || x < 0 || y > Game.size || y < 0;
    }

    public Fighter hitsSomebody(Fighter[] fighters) {
        for (int id = 0; id < fighters.length; id++) {
            if (id == ownerId) continue;
            Fighter fighter = fighters[id];
            float x = fighter.x - this.x;
            float y = fighter.y - this.y;
            float total = x * x + y * y;
            if (total < 100) return fighter;
        }
        return null;
    }
}
