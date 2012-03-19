package pl.krug.game.runner;

import java.util.ArrayList;
import java.util.List;
import pl.krug.game.NeuralPlayer;
import pl.krug.game.SimpleGame;
import pl.krug.game.genetic.crosser.NeuralPlayerCrosser;
import pl.krug.genetic.GeneticEngine;
import pl.krug.genetic.impl.GeneticEngineStandard;
import pl.krug.neural.dao.NetworkModelDAO;
import pl.krug.neural.network.NeuralNetwork;
import pl.krug.neural.network.NeuralNetworkFactory;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NetworkModelFactory;

public class SimpleGameRunner {

    public static void main(String[] args) {

        // create game object
        SimpleGame game = new SimpleGame();

        // create genetic engine for neural player
        GeneticEngine<NeuralPlayer> genetic = new GeneticEngineStandard<NeuralPlayer>(
                game,
                new NeuralPlayerCrosser());

        // create factories
        NetworkModelFactory modelFactory = new NetworkModelFactory();
        NeuralNetworkFactory neuralFactory = new NeuralNetworkFactory();

        NetworkModelDAO dao = new NetworkModelDAO();

        List<NeuralPlayer> players = new ArrayList<NeuralPlayer>();
        for (int i = 0; i < 100; i++) {
            NeuralPlayer player = new NeuralPlayer();
            NetworkModel model = modelFactory.createRandomNetwork(30, 3, 0.2);
            NeuralNetwork network = neuralFactory.createNetwork(model);
            player.setModel(model);
            player.setNeuralNetwork(network);
            players.add(player);
        }

        game.registerPlayer(players);

        long timeBeforeGame, timeAfterGame, timeAfterGeneration;

        for (int i = 0; i < 100; i++) {
            System.out.println("Round " + (i + 1));
            timeBeforeGame = System.currentTimeMillis();
            game.runGame();
            timeAfterGame = System.currentTimeMillis();
            List<NeuralPlayer> newPop = genetic.createNextGeneration();
            timeAfterGeneration = System.currentTimeMillis();
            game.replacePlayers(newPop);
            System.out.println("Time to run networks: " + ((timeAfterGame - timeBeforeGame) / 1000.0));
            System.out.println("Time to run genetic engine: " + ((timeAfterGeneration - timeAfterGame) / 1000.0));
        }

//		dao.saveNetwork(genetic.getPopulation().get(0).getModel());
        System.out.println();

    }
}
