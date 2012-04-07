/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game;

import java.util.concurrent.Callable;

/**
 *
 * @author Edhendil
 */
public class NeuralPlayerProcessingCallable implements Callable<Void> {

    private NeuralPlayer player;
    private double[] input;
    private int networkSteps;

    public NeuralPlayerProcessingCallable(NeuralPlayer player, double[] input, int networkSteps) {
        this.player = player;
        this.input = input;
        this.networkSteps = networkSteps;
    }

    @Override
    public Void call() throws Exception {
        player.runNetwork(input, networkSteps);
        return null;
    }
}
