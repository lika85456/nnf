package com.lika85456.neuralnetworkfighter;

import com.lika85456.neuralnetworkfighter.Game.Game;

import java.util.ArrayList;
import java.util.Collections;

public class Trainer {

    public NeuralFighter train(int generations) {
        ArrayList<NeuralFighter> best = new ArrayList<NeuralFighter>();
        //create 100 best networks (=100 games = 500 players)
        for (int i = 0; i < 100; i++) {
            best.add(Collections.max(playGame(5, null)));
        }
        //now decimate it for 10 best
        while (best.size() > 10) {
            best.remove(Collections.min(playGame(best.size(), best)));
        }
        ArrayList<NeuralFighter> result = playGame(best.size(), best);
        for (int i = 0; i < generations; i++) {
            result = playGame(result.size(), result);
            while (result.size() > 5) {
                result.remove(Collections.min(result));
            }
            for (int t = 0; t < best.size(); t++) {
                result.add(result.get(i).mutate(0.125f));
            }
        }

        return Collections.max(result);
    }

    /***
     *
     * @param count number of players in game
     * @return best network
     */
    public ArrayList<NeuralFighter> playGame(int count, ArrayList<NeuralFighter> fighters) {
        Game game = new Game(count);
        if (fighters == null)
            fighters = generateRandomFighters(count, game);
        else {
            for (int i = 0; i < fighters.size(); i++)
                fighters.get(i).fighter = game.fighters[i];
        }
        game.nextTurn();
        while (!game.isEnd()) {
            for (NeuralFighter fo : fighters) {
                fo.play(game);
            }
            game.nextTurn();
            //System.out.println(game.turns);
        }
        return fighters;
    }

    private ArrayList<NeuralFighter> generateRandomFighters(int count, Game game) {
        ArrayList<NeuralFighter> toRet = new ArrayList<NeuralFighter>();
        for (int i = 0; i < count; i++) {
            toRet.add(new NeuralFighter(game.fighters[i]));
        }
        return toRet;
    }
}
