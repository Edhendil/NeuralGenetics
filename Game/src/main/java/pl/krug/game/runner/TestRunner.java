package pl.krug.game.runner;

import java.util.Arrays;
import pl.krug.neural.network.NeuralNetwork;
import pl.krug.neural.network.NeuralNetworkFactory;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NetworkModelFactory;

public class TestRunner {

    public static void main(String[] args) {

//		NetworkModelDAO dao = new NetworkModelDAO();
//
//		long timeBeforeGame = System.currentTimeMillis();
//		
//		for (int i = 0 ; i < 1; i++) {
//		NetworkModel model = dao.getNetwork(100+i);
//		for (NeuronModel neu : model.getNeurons()) {
//			neu.getLinks().size();
//		}
//		System.out.println(i);
//		}
//		
//		long timeAfterGame = System.currentTimeMillis();
//		System.out.println("Time to load networks: " + ((timeAfterGame - timeBeforeGame)/1000.0));
//		System.out.println("Done");

        // create factories
        NetworkModelFactory modelFactory = new NetworkModelFactory();
        NeuralNetworkFactory neuralFactory = new NeuralNetworkFactory();

        NetworkModel test = modelFactory.createRandomNetwork(2, 2, 1);
        NeuralNetwork testneu = neuralFactory.createNetwork(test);

        double[] input = {100.0, 0.0};
        testneu.setInput(input);
        
        testneu.processElement();
        
        System.out.println(Arrays.toString(testneu.getResponse()));

    }
}
