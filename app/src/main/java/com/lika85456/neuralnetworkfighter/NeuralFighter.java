package com.lika85456.neuralnetworkfighter;

import android.support.annotation.NonNull;

import com.lika85456.neuralnetworkfighter.Game.Fighter;
import com.lika85456.neuralnetworkfighter.Game.Game;
import com.lika85456.neuralnetworkfighter.NeuralNetwork.NeuralNetwork;

public class NeuralFighter extends NeuralNetwork implements Comparable {

    public Fighter fighter;


    public NeuralFighter(Fighter fighter) {
        super(7, 5, 5, 10);
        this.fighter = fighter;
    }

    public void play(Game game) {
        proceedOutput(generateInput(game), game);
    }

    private void proceedOutput(float[] output, Game game) {
        if (output[0] > 0) {
            game.shoot(fighter.id);
            if (fighter.canShoot() && fighter.canSeeEnemy(game.fighters))
                fighter.shootWhileSeeingEnemy++;
        }
        game.rotate(fighter.id, Math.max(output[1], output[2]) == output[1]);
        game.setVelocity(fighter.id, output[3]);
        game.fighters[fighter.id].setFoV(output[4]);
    }

    private float[] generateInput(Game game) {
        return new float[]{fighter.FoV / 45f, fighter.canSeeBullet(game.bullets) ? 1f : -1f, fighter.canSeeEnemy(game.fighters) ? 1f : -1f, fighter.canShoot() ? 1f : -1f, fighter.hp * 2 - 1f, fighter.x / Game.size, fighter.y / Game.size};
    }

    public int getFitness() {
        return (int) (fighter.hp * 100) + fighter.shootWhileSeeingEnemy + fighter.hitEnemy * 50;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return getFitness() - ((NeuralFighter) (o)).getFitness();
    }

    public NeuralFighter mutate(float genes) {
        NeuralNetwork mutated = super.mutate(genes);
        NeuralFighter toRet = new NeuralFighter(null);
        toRet.weights = mutated.weights;
        return toRet;
    }

}
