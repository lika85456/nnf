package com.lika85456.neuralnetworkfighter;

import com.lika85456.neuralnetworkfighter.Game.Game;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class GameUnitTest {
    @Test
    public void convertingTest() {
        assertEquals(0f, Game.intToFloat(250, 500), 0.01f);
        assertEquals(1f, Game.intToFloat(500, 500), 0.01f);
        assertEquals(-1f, Game.intToFloat(0, 500), 0.01f);

    }
}
