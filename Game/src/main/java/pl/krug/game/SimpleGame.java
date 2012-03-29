package pl.krug.game;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import pl.krug.genetic.GeneticEvaluator;
import pl.krug.genetic.GeneticSelector;
import pl.krug.genetic.selector.util.impl.GeneticSelectorBestOfTwo;

/**
 * Takes care of multithreading and all the basic stuff that happens in every
 * game
 *
 * @author edhendil
 */
public class SimpleGame implements GeneticSelector<NeuralPlayer>, GeneticEvaluator<NeuralPlayer> {

    // world state
    private double _inputValue;
    private double[] _neuralInput;
    private final Map<NeuralPlayer, Double> _states = new HashMap<NeuralPlayer, Double>();
    // always
    // game level
    // does game without world make sense?
    private List<NeuralPlayer> _players = new ArrayList<NeuralPlayer>();
    // number of ticks before the evaluation phase and crossover
    // often
    private int _numberOfRounds = 10;
    // multithreading helpers
    private int _threadNumber = Runtime.getRuntime().availableProcessors();

    public SimpleGame() {
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
    public void runGame() {
        for (int i = 0; i < _numberOfRounds; i++) {
            runIteration();
        }
    }

    /**
     * Single round from providing input to reading output
     */
    private void runIteration() {
        ExecutorService exec = Executors.newFixedThreadPool(_threadNumber);
        List<PlayerProcessingCallable> tasks = new ArrayList<PlayerProcessingCallable>();
        for (NeuralPlayer player : _players) {
            tasks.add(new PlayerProcessingCallable(player));
        }
        try {
            exec.invokeAll(tasks);
            exec.shutdown();
            exec.awaitTermination(15, TimeUnit.MINUTES);
        } catch (InterruptedException ex) {
        }
        updateWorldState();
    }

    public void registerPlayer(NeuralPlayer player) {
        _states.put(player, 0.0);
        _players.add(player);
    }

    public void registerPlayer(Collection<NeuralPlayer> players) {
        for (NeuralPlayer player : players) {
            registerPlayer(player);
        }
    }

    /**
     * To be replaced by newGenerationCreated from genetic interface
     *
     * @param players
     */
    public void replacePlayers(Collection<NeuralPlayer> players) {
        removeAllPlayers();
        registerPlayer(players);
    }

    public void newGenerationCreated(Map<List<NeuralPlayer>, List<NeuralPlayer>> generationMap) {
        removeAllPlayers();
        for (List<NeuralPlayer> children : generationMap.values()) {
            registerPlayer(children);
        }
    }

    /**
     * Obvious
     */
    public void removeAllPlayers() {
        _states.clear();
        _players.clear();
    }

    /**
     * Obvious
     *
     * @param player
     */
    public void removePlayer(NeuralPlayer player) {
        _states.remove(player);
        _players.remove(player);
    }

    /**
     * Resets world state and players state but does not remove players from the
     * world
     */
    private void resetWorldState() {
        for (NeuralPlayer player : _states.keySet()) {
            _states.put(player, 0.0);
        }
        startNextRound();
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
        for (NeuralPlayer player : _states.keySet()) {
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
        Random rand = new Random();
        _inputValue = rand.nextInt(2);
        _neuralInput = new double[]{_inputValue, 1 - _inputValue};
//        System.out.println("Input " + Arrays.toString(_neuralInput));
    }

    @Override
    public List<List<NeuralPlayer>> selectParents() {
        GeneticSelectorBestOfTwo<NeuralPlayer> select = new GeneticSelectorBestOfTwo<NeuralPlayer>(this);
        return select.selectParents(this._players);
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

    private class PlayerProcessingCallable implements Callable<Void> {

        private NeuralPlayer player;

        public PlayerProcessingCallable(NeuralPlayer player) {
            this.player = player;
        }

        @Override
        public Void call() throws Exception {
            player.runNetwork(getInputForPlayer(player), 50);
            return null;
        }
    }
}
