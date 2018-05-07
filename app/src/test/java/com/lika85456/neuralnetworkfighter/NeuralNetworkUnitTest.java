package com.lika85456.neuralnetworkfighter;

import com.lika85456.neuralnetworkfighter.NeuralNetwork.NeuralNetwork;

import org.junit.Test;

public class NeuralNetworkUnitTest {
    @Test
    public void testNeuralNetwork() {
        NeuralNetwork nn = new NeuralNetwork(7, 5, 2, 10);
        float[] input = new float[]{-1f, 0f, -1f, 1f, 0f, 1f, -1f};
        float[] output = nn.getOutput(input);
        int i = 0;
    }
}
