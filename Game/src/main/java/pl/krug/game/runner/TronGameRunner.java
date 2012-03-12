package pl.krug.game.runner;

import java.util.ArrayList;
import java.util.List;
import pl.krug.game.NeuralPlayer;
import pl.krug.game.tron.TronGame;
import pl.krug.neural.dao.NetworkModelDAO;
import pl.krug.neural.network.NeuralNetwork;
import pl.krug.neural.network.NeuralNetworkFactory;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NetworkModelFactory;

public class TronGameRunner {

	public static void main(String[] args) {

		// create factories
		NetworkModelFactory modelFactory = new NetworkModelFactory();
		NeuralNetworkFactory neuralFactory = new NeuralNetworkFactory();

		// 79 - 128
		// 129 - 178
		// 179 - 228
		// 229 - 278

		NetworkModelDAO dao = new NetworkModelDAO();

		long startIndex = 1492;

		List<NeuralPlayer> players = new ArrayList<NeuralPlayer>();
		for (int i = 0; i < 2; i++) {
			NeuralPlayer player = new NeuralPlayer();
			NetworkModel model = dao.getNetwork(startIndex + i);
			NeuralNetwork network = neuralFactory.createNetwork(model);
			player.setModel(model);
			player.setNeuralNetwork(network);
			players.add(player);
		}

		for (int i = 0; i < 5; i++) {
			System.out.println("Game start");
			TronGame game = new TronGame(players.get(0), players.get(1));
			NeuralPlayer winner = game.runGame();
			if (winner != null) {
				if (winner == players.get(0))
					System.out.println("Left won");
				else
					System.out.println("Right won");
			} else
				System.out.println("Draw");
		}

	}

}
