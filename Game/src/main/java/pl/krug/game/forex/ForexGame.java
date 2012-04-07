/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game.forex;

import java.util.*;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import pl.krug.game.AbstractGridGame;
import pl.krug.game.GeneticGame;
import pl.krug.game.NeuralPlayer;
import pl.krug.game.NeuralPlayerProcessingCallable;
import pl.krug.genetic.MutationRateProvider;

/**
 * Based on Forex data. Players are trying to earn as much money as possible.
 *
 * Input: only open value is taken into consideration. Only value is provided on
 * one neuron.
 *
 * Output: 3 neurons. One for each of actions to be taken.
 *
 * On action change new player money value is calculated.
 *
 * @author Edhendil
 */
public class ForexGame extends AbstractGridGame implements GeneticGame<NeuralPlayer> {

    private double startingMoney = 1;
    private Map<NeuralPlayer, ForexPlayerState> states = new HashMap<NeuralPlayer, ForexPlayerState>();
    private int gameLength = 500;
    private int roundNumber = 0;
    private List<ForexHistoricalData> data;
    private double[] neuralInput;
    private double currentOpenValue;
    private double spread = 0;

    public ForexGame(int width, int height, List<ForexHistoricalData> data) {
        super(width, height);
        this.data = data;
    }

    @Override
    protected void initializePlayerState(NeuralPlayer player) {
        states.put(player, new ForexPlayerState(startingMoney));
    }

    @Override
    protected void destroyPlayerState(NeuralPlayer player) {
        states.remove(player);
    }

    @Override
    public void runGame() {
        for (int i = 0; i < gameLength; i++) {
            runIteration();
            roundNumber++;
        }
        // end all transactions 
//        for (ForexPlayerState state : states.values()) {
//            ForexAction action = state.getAction();
//            if (action != ForexAction.NOTHING) {
//                endTransaction(state, action);
//            }
//        }
    }

    private void runIteration() {
        createRoundInput(data.get(roundNumber));
        for (NeuralPlayer player : getPlayersList()) {
            getTasksQueue().addTask(new NeuralPlayerProcessingCallable(player, neuralInput, 10));
        }
        getTasksQueue().processTasksAndWait();
        updateWorldState();
    }

    private void createRoundInput(ForexHistoricalData item) {
        currentOpenValue = item.getOpenValue();
        neuralInput = new double[1];
        neuralInput[0] = currentOpenValue;
    }

    @Override
    public double evaluate(NeuralPlayer individual) {
        return states.get(individual).getMoney();
    }

    private void updateWorldState() {
        for (NeuralPlayer player : getPlayersList()) {
            double[] response = Arrays.copyOfRange(player.getResponse(), 1, 4);
//            System.out.println(Arrays.toString(response));
            int actionNumber = getMaxPosition(response);
//            System.out.println(actionNumber);
            ForexAction action = ForexAction.values()[actionNumber];
            ForexPlayerState state = states.get(player);
            ForexAction oldAction = state.getAction();
            // if there is any change
            if (oldAction != action) {
                if (oldAction != ForexAction.NOTHING) {
                    endTransaction(state, oldAction);
                }
                if (action != ForexAction.NOTHING) {
                    startNewTransaction(state);
                }
                state.setAction(action);
            }


        }
    }

    private void endTransaction(ForexPlayerState state, ForexAction oldAction) {
//        System.out.println("End trans");
        if (oldAction == ForexAction.RISING) {
            double change = (currentOpenValue - spread) / state.getLastTransactionOpening();
            state.setMoney(state.getMoney() * change);
        } else {
            double change = (currentOpenValue + spread) / state.getLastTransactionOpening();
            state.setMoney(state.getMoney() / change);
        }
    }

    private void startNewTransaction(ForexPlayerState state) {
//        System.out.println("Start trans");
        state.setLastTransactionOpening(currentOpenValue);
    }

    private int getMaxPosition(double[] array) {
        if (array.length == 0) {
            return -1;
        }
        double maxValue = array[0];
        int index = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                index = i;
            }
        }
        return index;
    }

    public void printStatistics() {

        List<NeuralPlayer> players = getPlayersList();

        // Get a DescriptiveStatistics instance
        SummaryStatistics stats = new SummaryStatistics();

        // Add the data from the array
        for (NeuralPlayer player : players) {
            stats.addValue(evaluate(player));
        }

        // Compute some statistics
        double mean = stats.getMean();
        double std = stats.getStandardDeviation();
        double max = stats.getMax();

        System.out.println("Best: " + max + "\nMean:" + mean + "\nDeviation: " + std);

    }
}
