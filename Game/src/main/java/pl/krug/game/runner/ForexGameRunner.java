/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game.runner;

import java.util.*;
import pl.krug.game.NeuralPlayer;
import pl.krug.game.SimpleGame;
import pl.krug.game.forex.ForexDAO;
import pl.krug.game.forex.ForexGame;
import pl.krug.game.forex.ForexHistoricalData;
import pl.krug.game.genetic.crosser.NeuralPlayerCrosser;
import pl.krug.genetic.GeneticEngine;
import pl.krug.genetic.impl.GeneticEngineStandard;
import pl.krug.neural.network.NeuralNetwork;
import pl.krug.neural.network.NeuralNetworkFactory;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NetworkModelFactory;

/**
 *
 * @author Edhendil
 */
public class ForexGameRunner {

    public static void main(String[] args) {

        ForexDAO dao = new ForexDAO();

        GregorianCalendar todayCal = new GregorianCalendar();
        GregorianCalendar someAgo = new GregorianCalendar(2011, 10, 1);

        Date today = todayCal.getTime();
        Date weekAgo = someAgo.getTime();
        List<ForexHistoricalData> data = dao.getData(weekAgo, today);

        // create game object
        ForexGame game = new ForexGame(10, 10, data);

        // create genetic engine for neural player
        GeneticEngine<NeuralPlayer> genetic = new GeneticEngineStandard<NeuralPlayer>(
                game,
                new NeuralPlayerCrosser(game));

        // create factories
        NetworkModelFactory modelFactory = new NetworkModelFactory();
        NeuralNetworkFactory neuralFactory = new NeuralNetworkFactory();

//        NetworkModelDAO dao = new NetworkModelDAO();

        List<NeuralPlayer> players = new ArrayList<NeuralPlayer>();
        for (int i = 0; i < 100; i++) {
            NeuralPlayer player = new NeuralPlayer();
            NetworkModel model = modelFactory.createRandomNetwork(30, 4, 0.2);
            NeuralNetwork network = neuralFactory.createNetwork(model);
            player.setModel(model);
            player.setNeuralNetwork(network);
            players.add(player);
        }

        game.addPlayers(players);

        long timeBeforeGame, timeAfterGame, timeAfterGeneration;

        for (int i = 0; i < 5000; i++) {
            System.out.println("Round " + (i + 1));
            timeBeforeGame = System.currentTimeMillis();
            game.runGame();
            timeAfterGame = System.currentTimeMillis();
            Map<List<NeuralPlayer>, List<NeuralPlayer>> newPop = genetic.createNextGeneration();
            timeAfterGeneration = System.currentTimeMillis();
            game.printStatistics();
            game.newGenerationCreated(newPop);
            System.out.println("Time to run networks: " + ((timeAfterGame - timeBeforeGame) / 1000.0));
            System.out.println("Time to run genetic engine: " + ((timeAfterGeneration - timeAfterGame) / 1000.0));
        }

//		dao.saveNetwork(genetic.getPopulation().get(0).getModel());
        System.out.println();
    }
}
