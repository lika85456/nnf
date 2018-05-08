package com.lika85456.neuralnetworkfighter;

import org.junit.Test;

public class GameWithNNUnitTest {
    @Test
    public void testGame() {
        Trainer trainer = new Trainer();
        NeuralFighter nn = trainer.train(100);
        System.out.println(nn.getFitness());
    }


}
