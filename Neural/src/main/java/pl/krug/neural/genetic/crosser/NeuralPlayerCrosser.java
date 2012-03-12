package pl.krug.neural.genetic.crosser;

import java.util.ArrayList;
import java.util.List;

import pl.krug.game.NeuralPlayer;
import pl.krug.genetic.GeneticCrosser;
import pl.krug.neural.network.NeuralNetworkFactory;
import pl.krug.neural.network.model.NetworkModel;

public class NeuralPlayerCrosser implements GeneticCrosser<NeuralPlayer> {

	NetworkModelCrosser _crosser = new NetworkModelCrosser();
	
	@Override
	public List<NeuralPlayer> crossover(List<NeuralPlayer> parents) {
		List<NetworkModel> parentModels = new ArrayList<NetworkModel>(2);
		for (NeuralPlayer pl : parents) {
			parentModels.add(pl.getModel());
		}
		List<NetworkModel> childModels = _crosser.crossover(parentModels);
		
		// here I must create a full Neural Player using the models I just created
		List<NeuralPlayer> result = new ArrayList<NeuralPlayer>();
		NeuralNetworkFactory factory = new NeuralNetworkFactory();
		
		for (NetworkModel model : childModels) {
			NeuralPlayer player = new NeuralPlayer();
			player.setModel(model);
			player.setNeuralNetwork(factory.createNetwork(model));
			result.add(player);
		}
		
		return result;
	}

}
