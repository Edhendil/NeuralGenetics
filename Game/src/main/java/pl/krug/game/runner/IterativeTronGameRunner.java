package pl.krug.game.runner;

import java.util.ArrayList;
import java.util.List;
import pl.krug.game.NeuralPlayer;
import pl.krug.game.genetic.crosser.NeuralPlayerCrosser;
import pl.krug.game.tron.IterativeTronGame;
import pl.krug.genetic.GeneticEngine;
import pl.krug.genetic.impl.GeneticEngineStandard;
import pl.krug.neural.network.NeuralNetwork;
import pl.krug.neural.network.NeuralNetworkFactory;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NetworkModelFactory;

public class IterativeTronGameRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		IterativeTronGame game = new IterativeTronGame(10);
		
		// create genetic engine for neural player
		GeneticEngine<NeuralPlayer> genetic = new GeneticEngineStandard<NeuralPlayer>(
				game,
				new NeuralPlayerCrosser());

		// create factories
		NetworkModelFactory modelFactory = new NetworkModelFactory();
		NeuralNetworkFactory neuralFactory = new NeuralNetworkFactory();
		
//		NetworkModelDAO dao = new NetworkModelDAO();
		
		List<NeuralPlayer> players = new ArrayList<NeuralPlayer>();
//		for (long i = 429; i < 479; i++) {
//			NeuralPlayer player = new NeuralPlayer();
//			NetworkModel model = dao.getNetwork(i);
//			NeuralNetwork network = neuralFactory.createNetwork(model);
//			player.setModel(model);
//			player.setNeuralNetwork(network);
//			players.add(player);
//		}

		
		for (int i = 0; i < 10; i++) {
			NeuralPlayer player = new NeuralPlayer();
			NetworkModel model = modelFactory.createRandomNetwork(400, 311,
					0.1);
			NeuralNetwork network = neuralFactory.createNetwork(model);
			player.setModel(model);
			player.setNeuralNetwork(network);
			players.add(player);
		}

		game.addPlayers(players);

		long timeBeforeGame, timeAfterGame, timeAfterGeneration;
		
		for (int i = 0; i < 1000; i++) {
			System.out.println("Round " + (i+1));
			timeBeforeGame = System.currentTimeMillis();
			game.runGame();
			timeAfterGame = System.currentTimeMillis();
			List<NeuralPlayer> newPop = genetic.createNextGeneration();
			timeAfterGeneration = System.currentTimeMillis();
			game.resetGame();
			game.addPlayers(newPop);
			System.out.println("Time to run networks: " + ((timeAfterGame - timeBeforeGame)/1000.0));
			System.out.println("Time to run genetic engine: " + ((timeAfterGeneration - timeAfterGame)/1000.0));
//			if ((i+1) % 25 == 0) {
//				List<NetworkModel> models = new ArrayList<NetworkModel>();
//				for (NeuralPlayer player : newPop) {
//					models.add(player.getModel());
//				}
//				dao.saveNetworks(models);
//			}
		}
		
		System.out.println();

	}

}
