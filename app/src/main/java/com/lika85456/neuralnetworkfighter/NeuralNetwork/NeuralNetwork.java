package com.lika85456.neuralnetworkfighter.NeuralNetwork;

import java.util.Random;

public class NeuralNetwork implements IFitnessGetter {
    private int hiddenLayers = 2;
    private int hiddenNeurons = 10;
    private int inputs = 1;
    private int outputs = 1;
    private Random random;

    public float[] weights;

    public NeuralNetwork(int inputs, int outputs, int hiddenLayers, int hiddenNeurons) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.hiddenNeurons = hiddenNeurons;
        this.hiddenLayers = hiddenLayers;

        this.random = new Random();

        this.weights = new float[getNumberOfSynapses()];
        randomFillWeights();
    }

    private int getNumberOfSynapses() {
        return inputs * hiddenNeurons + (hiddenLayers - 1) * hiddenNeurons * hiddenNeurons + hiddenNeurons * outputs;
    }

    private void randomFillWeights() {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextFloat() * 2f - 1f;
        }
    }


    public float[] getOutput(float[] input) {
        float[] neurons = new float[getNumberOfNeurons()];

        //first load input neurons
        for (int i = 0; i < inputs; i++) {
            neurons[i] = input[i];
        }

        int synapseIndex = 0;

        //load first hidden layer
        for (int neuron = 0; neuron < hiddenNeurons; neuron++) {
            int tIndex = neuron + inputs;
            for (int inputNeuron = 0; inputNeuron < inputs; inputNeuron++) {
                neurons[tIndex] += weights[synapseIndex] * neurons[inputNeuron];
                synapseIndex++;
            }

            neurons[tIndex] = Math.max(-1, Math.min(1, neurons[tIndex]));
        }

        //load other hidden layers
        for (int layer = 1; layer < hiddenLayers; layer++) {
            for (int neuron = 0; neuron < hiddenNeurons; neuron++) {
                int tIndex = inputs + layer * hiddenNeurons + neuron;
                for (int neuronBefore = inputs + (layer - 1) * hiddenNeurons; neuronBefore < inputs + layer * hiddenNeurons; neuronBefore++) {
                    neurons[tIndex] += weights[synapseIndex] * neurons[neuronBefore];
                    synapseIndex++;
                }

                neurons[tIndex] = Math.max(-1, Math.min(1, neurons[tIndex]));
            }
        }


        //load output layer
        int totalNeurons = getNumberOfNeurons();
        for (int outputNeuron = 0; outputNeuron < outputs; outputNeuron++) {
            int tIndex = totalNeurons - 1 + outputNeuron - outputs;
            for (int hiddenNeuron = totalNeurons - outputs - 1 - hiddenNeurons; hiddenNeuron < totalNeurons - outputs - 1; hiddenNeuron++) {
                neurons[tIndex] += weights[synapseIndex] * neurons[hiddenNeuron];

                synapseIndex++;
            }
            neurons[tIndex] = Math.max(-1, Math.min(1, neurons[tIndex]));

        }

        float[] toRet = new float[outputs];
        for (int i = 0; i < outputs; i++)
            toRet[i] = neurons[totalNeurons - 1 - outputs + i];

        return toRet;
    }

    private int getNumberOfNeurons() {
        return inputs + hiddenNeurons * hiddenLayers + outputs;
    }

    public NeuralNetwork mutate(float genes) {
        NeuralNetwork nn = new NeuralNetwork(inputs, outputs, hiddenLayers, hiddenNeurons);
        for (int i = 0; i < this.weights.length; i++) {
            nn.weights[i] = this.weights[i];
        }
        for (int i = 0; i < nn.weights.length; i++) {
            if (random.nextFloat() < genes) {
                nn.weights[i] = random.nextFloat() * 2f - 1f;
            }
        }
        return nn;
    }

    public int getFitness() {
        return 0;
    }

}