package pl.krug.game;

import java.util.*;
import java.util.concurrent.Callable;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

/**
 * Takes care of multithreading and all the basic stuff that happens in every
 * game
 *
 * @author edhendil
 */
public class SimpleGame extends AbstractGridGame implements GeneticGame<NeuralPlayer> {

    private Random randomGenerator = new Random();
    // world state
    private double _inputValue;
    private double[] _neuralInput;
    private final Map<NeuralPlayer, Double> _states = new HashMap<NeuralPlayer, Double>();
    // number of ticks before the evaluation phase and crossover
    // often
    private int _numberOfRounds = 20;

    public SimpleGame() {
        super(100, 50);
        startNextRound();
    }

    @Override
    public double evaluate(NeuralPlayer individual) {
        return _states.get(individual);
    }

    /**
     * Initialize starting state for world and players Play an arbitrary number
     * of rounds Game method
     */
    @Override
    public void runGame() {
        for (int i = 0; i < _numberOfRounds; i++) {
            runIteration();
        }
    }

    /**
     * Single round from providing input to reading output
     */
    private void runIteration() {
        List<NeuralPlayer> players = getPlayersList();
        for (NeuralPlayer player : players) {
            getTasksQueue().addTask(new NeuralPlayerProcessingCallable(player, getInputForPlayer(player), 10));
        }
        getTasksQueue().processTasksAndWait();
        updateWorldState();
    }

    /**
     * Creates input for player based on his and worlds state
     *
     * @param player
     * @return
     */
    private double[] getInputForPlayer(NeuralPlayer player) {
        return _neuralInput;
    }

    /**
     * Takes all responses from players, update their states and the worlds
     * Single tick/round
     */
    private void updateWorldState() {
        // players state
        List<NeuralPlayer> players = getPlayersList();
        for (NeuralPlayer player : players) {
            double[] neuralOutput = player.getResponse();
            _states.put(
                    player,
                    _states.get(player)
                    - Math.abs((double) _inputValue - neuralOutput[2]));
        }
        // world state
        startNextRound();
    }

    /**
     * Sets all important aspects
     */
    private void startNextRound() {
        _inputValue = randomGenerator.nextInt(2);
        _neuralInput = new double[]{_inputValue, 1 - _inputValue};
    }

    /**
     * Prints all vital information, best evaluation, mean, standard deviation.
     */
    public void printStatistics() {

        // Get a DescriptiveStatistics instance
        SummaryStatistics stats = new SummaryStatistics();
        Double[] values = new Double[_states.values().size()];
        _states.values().toArray(values);

        // Add the data from the array
        for (int i = 0; i < values.length; i++) {
            stats.addValue(values[i]);
        }

        // Compute some statistics
        double mean = stats.getMean();
        double std = stats.getStandardDeviation();
        double max = stats.getMax();

        System.out.println("Best: " + max + "\nMean:" + mean + "\nDeviation: " + std);

    }

    @Override
    protected void initializePlayerState(NeuralPlayer player) {
        _states.put(player, 0.0);
    }

    @Override
    protected void destroyPlayerState(NeuralPlayer player) {
        _states.remove(player);
    }
}
