package com.lika85456.neuralnetworkfighter.Game;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    public static final int size = 500;
    public static final long reloadingTime = 1000;
    public static final int maxTurns = 1000;
    public static final float bulletVelocity = 2f;
    public ArrayList<Bullet> bullets;
    public Fighter[] fighters;
    private int nPlayers;
    private int turns = 0;
    private Random random;


    public Game(int numberOfPlayers) {
        this.nPlayers = numberOfPlayers;
        fighters = new Fighter[nPlayers];
        random = new Random();
        bullets = new ArrayList<Bullet>();
        generateFighters();
    }

    public static float intToFloat(int toConvert, int maximum) {
        return (float) toConvert / (float) maximum * 2f - 1f;
    }

    private void generateFighters() {
        for (int i = 0; i < nPlayers; i++) {
            fighters[i] = generateFighter(i);
        }
    }

    private Fighter generateFighter(int id) {
        Fighter toRet = new Fighter(id);
        toRet.x = random.nextInt(size);
        toRet.y = random.nextInt(size);
        toRet.angle = random.nextFloat();
        toRet.velocity = 0;
        return toRet;
    }

    public void nextTurn() {
        turns++;

        //players moving
        for (int i = 0; i < nPlayers; i++) {
            fighters[i].move();
        }

        //bullets moving + interaction with players
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).move();
            Fighter fighter = bullets.get(i).hitsSomebody(fighters);
            if (fighter != null) {
                fighter.getHitted();
                bullets.remove(i);
            }
            fighter = null;

            if (bullets.get(i).isOut()) bullets.remove(i);
        }

        System.out.println("PlayerID: 0 - x: " + fighters[0].x + " y: " + fighters[0].y + "velocity: " + fighters[0].velocity);
        System.out.println("PlayerID: 1 - x: " + fighters[1].x + " y: " + fighters[1].y + "velocity: " + fighters[1].velocity);
    }

    public void rotate(int playerId, boolean way, float strength) {
        if (strength > 0.5f) strength = 0.5f;
        if (strength < 0) strength = 0;
        Fighter fighter = fighters[playerId];
        if (way)
            fighter.angle += strength;
        else
            fighter.angle -= strength;
        if (fighter.angle < 0) fighter.angle += 1;
        if (fighter.angle > 1) fighter.angle = fighter.angle % 1;
    }

    public void setVelocity(int playerId, float velocity) {
        if (velocity > 1f) velocity = 1f;
        if (velocity < 1f) velocity = 0f;
        fighters[playerId].velocity = velocity;
    }

    public void shoot(int playerId) {
        Fighter player = fighters[playerId];
        if (player.canShoot()) {
            player.shooted();
            shootBullet(playerId);
            System.out.println("Player: " + playerId + " shoots");
        }

    }

    private void shootBullet(int playerId) {
        Fighter fighter = fighters[playerId];
        Bullet bullet = new Bullet(fighter.x, fighter.y, fighter.angle, playerId);
        bullets.add(bullet);
    }

    public boolean isEnd() {
        for (int i = 0; i < nPlayers; i++) {
            if (fighters[i].hp <= 0) return true;
        }
        return turns >= maxTurns;
    }

    public float getFitness(int playerId) {
        Fighter fighter = fighters[playerId];
        float fitness = 0;
        fitness += fighter.hp;
        return fitness;
    }

}
