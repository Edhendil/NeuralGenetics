package pl.krug.game.tron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

import pl.krug.game.NeuralPlayer;
import pl.krug.genetic.GeneticEvaluator;
import pl.krug.genetic.GeneticSelector;

/**
 * Many games played by randomly chosen players to see which one is the best
 * Number of players must be a multiplication of 2, if not then one of players
 * will have instaloss each round
 *
 * Iterative grid? Wtedy kazdy z kazdym sąsiadem po kilka razy. A na koniec
 * dopiero z każdego segmentu wybieranych byłoby 2 zwyciezców z największą
 * ilością punktów, które mogli zdobyć też w innych meczach z jeszcze dalszymi
 * osobnikami.
 *
 * PopulationGrid? Działałoby altough można by wykombinować większą dozę
 * abstrakcji. Damn, ale na pewno to by się dało lepiej zrobić.
 *
 * Jeśli świat też byłby selectorem? Wtedy populacja musiałaby wylecieć z
 * silnika. Silnik tylko by mial selector i crosser, bez samej populacji.
 * Poszczególne osobniki byłyby wybierane przez selector i to on ewentualnie
 * mialby dostęp bezpośredni do całej populacji.
 *
 * @author edhendil
 *
 */
public class IterativeTronGame implements GeneticSelector<NeuralPlayer> {

    private Map<NeuralPlayer, Integer> _gamesWon = new HashMap<NeuralPlayer, Integer>();
    private List<NeuralPlayer> _players = new ArrayList<NeuralPlayer>();
    private int _gamesPlayed = 0;
    private RandomData random = new RandomDataImpl();
    private Queue<TronGame> _currentGames = new ConcurrentLinkedQueue<TronGame>();
    private int _threads = Runtime.getRuntime().availableProcessors();
    private CyclicBarrier _barrier = new CyclicBarrier(_threads + 1);
    private int _roundNumber;

    public IterativeTronGame(int roundNumber) {
        _roundNumber = roundNumber;
    }

    public void runIteration() {
        int matches = _players.size() / 2;
        int[] perm = random.nextPermutation(_players.size(), _players.size());
        for (int i = 0; i < matches; i++) {
            TronGame game = new TronGame(_players.get(perm[i * 2]),
                    _players.get(perm[i * 2 + 1]));
            _currentGames.add(game);
        }
        // start threads
        for (int i = 0; i < _threads; i++) {
            new Thread(new MatchProcessingThread()).start();
        }
        try {
            _barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        _gamesPlayed++;
    }

    public void runGame() {
        for (int i = 0; i < _roundNumber; i++) {
            runIteration();
        }
    }

    /**
     * Removes all players and states
     */
    public void resetGame() {
        _gamesWon.clear();
        _players.clear();
        _gamesPlayed = 0;
    }

    public void addPlayers(List<NeuralPlayer> players) {
        for (NeuralPlayer player : players) {
            _gamesWon.put(player, 0);
        }
        _players.addAll(players);
    }

    public List<List<NeuralPlayer>> selectParents() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Takes take of multithreading GameEngine responsibility
     *
     * @author edhendil
     *
     */
    private class MatchProcessingThread implements Runnable {

        @Override
        public void run() {
            TronGame game;
            NeuralPlayer winner;
            while ((game = _currentGames.poll()) != null) {
                winner = game.runGame();
                if (winner != null) {
                    // it is safe
                    // player cant win two games at the same time
                    _gamesWon.put(winner, _gamesWon.get(winner) + 1);
                }
            }
            try {
                _barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public double evaluate(NeuralPlayer individual) {
        double result = _gamesWon.get(individual);
        System.out.println(result);
        return result;
    }
}
