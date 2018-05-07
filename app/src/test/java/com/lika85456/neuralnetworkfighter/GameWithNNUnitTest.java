package com.lika85456.neuralnetworkfighter;

import com.lika85456.neuralnetworkfighter.Game.Fighter;
import com.lika85456.neuralnetworkfighter.Game.Game;
import com.lika85456.neuralnetworkfighter.NeuralNetwork.NeuralNetwork;

import org.junit.Test;

public class GameWithNNUnitTest {
    @Test
    public void testGame() {
        Game game = new Game(2);
        NeuralNetwork n1 = new NeuralNetwork(7, 5, 2, 10);
        NeuralNetwork n2 = new NeuralNetwork(7, 5, 2, 10);
        while (!game.isEnd()) {
            float[] output1 = n1.getOutput(generateInput(game, 0));
            float[] output2 = n2.getOutput(generateInput(game, 1));
            proceedOutput(output1, game, 0);
            proceedOutput(output2, game, 1);
            game.nextTurn();
        }
        float f1 = game.getFitness(0);
        float f2 = game.getFitness(1);
    }

    public void proceedOutput(float[] output, Game game, int playerId) {
        if (output[0] > 0) game.shoot(playerId);
        game.rotate(playerId, Math.max(output[1], output[2]) == output[1], (output[3] + 1f) / 2f);
        game.setVelocity(playerId, output[4] > 0 ? 1f : 0f);
    }

    public float[] generateInput(Game game, int playerId) {
        Fighter f = game.fighters[playerId];
        return new float[]{f.FoV / 45f, f.canSeeBullet(game.bullets) ? 1f : -1f, f.canSeeEnemy(game.fighters) ? 1f : -1f, f.canShoot() ? 1f : -1f, f.hp * 2 - 1f, f.x / Game.size, f.y / Game.size};
    }


}
