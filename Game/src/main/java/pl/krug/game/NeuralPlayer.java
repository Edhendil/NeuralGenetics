package pl.krug.game;

import pl.krug.neural.network.NeuralNetwork;
import pl.krug.neural.network.model.NetworkModel;

/**
 * Game/world counterpart of neural network
 *
 * @author edhendil
 *
 */
public class NeuralPlayer {

    //TODO utworzyć jakiś moduł do przetwarzania informacji ze świata na impulsy dla sieci
    private NeuralNetwork _neuralNetwork;
    private NetworkModel _model;

    public NeuralNetwork getNeuralNetwork() {
	return _neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
	_neuralNetwork = neuralNetwork;
    }

    public NetworkModel getModel() {
	return _model;
    }

    public void setModel(NetworkModel model) {
	_model = model;
    }

    public void runNetwork(double[] neuralInput, int steps) {
	for (int i = 0; i < steps; i++) {
	    _neuralNetwork.runNetworkStep(neuralInput);
	}
    }

    public double[] getResponse() {
	return _neuralNetwork.getNetworkResponse();
    }
}
