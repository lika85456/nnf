package com.lika85456.neuralnetworkfighter;

class Neuron {
    float value;

    Neuron() {
        value = 0;
    }

    void add(float value) {
        this.value += value;
        this.value = max(-1, min(1, this.value));
    }

    void clear() {
        value = 0;
    }
}

class Synapse {
    float weight;
    Neuron input;
    Neuron output;

    Synapse(float weight, Neuron input, Neuron output) {
        this.weight = weight;
        this.input = input;
        this.output = output;
    }

    void run() {
        output.add(input.value * weight);
    }

}

class NeuralNet {
    int inputCount;
    int layerCount;
    int layerWidth;
    int outputCount;

    Neuron[] input;
    Neuron[][] hiden;
    Neuron[] output;

    Synapse[][] inputSynapses;
    Synapse[][][] hidenSynapses;
    Synapse[][] outputSynapses;
    NeuralNet brain;

    NeuralNet(int inputCount, int layerCount, int layerWidth, int outputCount) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.layerCount = layerCount;
        this.layerWidth = layerWidth;

        input = new Neuron[inputCount];
        hiden = new Neuron[layerCount][layerWidth];
        output = new Neuron[outputCount];

        inputSynapses = new Synapse[inputCount][layerWidth];
        hidenSynapses = new Synapse[layerCount - 1][layerWidth][layerWidth];
        outputSynapses = new Synapse[layerWidth][outputCount];

        for (int i = 0; i < inputCount; i++)
            input[i] = new Neuron();

        for (int i = 0; i < outputCount; i++)
            output[i] = new Neuron();

        for (int i = 0; i < layerCount; i++)
            for (int j = 0; j < layerWidth; j++)
                hiden[i][j] = new Neuron();

        for (int i = 0; i < inputCount; i++)
            for (int j = 0; j < layerWidth; j++)
                inputSynapses[i][j] = new Synapse(random(-1, 1), input[i], hiden[0][j]);

        for (int i = 0; i < layerCount - 1; i++)
            for (int j = 0; j < layerWidth; j++)
                for (int k = 0; k < layerWidth; k++)
                    hidenSynapses[i][j][k] = new Synapse(random(-1, 1), hiden[i][j], hiden[i + 1][k]);

        for (int i = 0; i < layerWidth; i++)
            for (int j = 0; j < outputCount; j++)
                outputSynapses[i][j] = new Synapse(random(-1, 1), hiden[layerCount - 1][i], output[j]);
    }

}

    float[] run(float[] inData) {
        float[] outData = new float[outputCount];

        for (int i = 0; i < inputCount; i++)
            input[i].value = inData[i];

        for (int i = 0; i < outputCount; i++)
            output[i].clear();

        for (int i = 0; i < layerCount; i++)
            for (int j = 0; j < layerWidth; j++)
                hiden[i][j].clear();

        for (int i = 0; i < inputCount; i++)
            for (int j = 0; j < layerWidth; j++)
                inputSynapses[i][j].run();

        for (int i = 0; i < layerCount - 1; i++)
            for (int j = 0; j < layerWidth; j++)
                for (int k = 0; k < layerWidth; k++)
                    hidenSynapses[i][j][k].run();

        for (int i = 0; i < layerWidth; i++)
            for (int j = 0; j < outputCount; j++)
                outputSynapses[i][j].run();

        for (int i = 0; i < outputCount; i++)
            outData[i] = output[i].value;

        return outData;
    }

    void setup() {
        size(512, 512);
        colorMode(HSB, 2);
        loadPixels();
        thread("generate");
    }

    void draw() {
        updatePixels();
    }

    void generate() {
        brain = new NeuralNet(2, 10, 10, 1);
        for (float i = 0; i < width; i++)
            for (float j = 0; j < height; j++)
                pixels[(int) (i * width + j)] = color(brain.run(new float[]{i / width, j / height})[0] + 1, 2, 2);
    }

    void mouseReleased() {
        thread("generate");
    }