package pl.krug.neural.runner;

import java.util.ArrayList;
import java.util.List;

import pl.krug.genetic.crosser.impl.NetworkModelCrosser;
import pl.krug.neural.dao.NetworkModelDAO;
import pl.krug.neural.network.NeuralNetwork;
import pl.krug.neural.network.NeuralNetworkFactory;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NetworkModelFactory;
import pl.krug.neural.network.model.NeuronModel;

public class TestRunner {

	public static void main(String[] args) {

		NetworkModelDAO dao = new NetworkModelDAO();

		long timeBeforeGame = System.currentTimeMillis();
		
		for (int i = 0 ; i < 1; i++) {
		NetworkModel model = dao.getNetwork(100+i);
		for (NeuronModel neu : model.getNeurons()) {
			neu.getLinks().size();
		}
		System.out.println(i);
		}
		
		long timeAfterGame = System.currentTimeMillis();
		System.out.println("Time to load networks: " + ((timeAfterGame - timeBeforeGame)/1000.0));
		System.out.println("Done");
		
		
	}

}
