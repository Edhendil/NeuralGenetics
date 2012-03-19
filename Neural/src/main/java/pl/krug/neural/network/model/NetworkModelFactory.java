package pl.krug.neural.network.model;

import java.util.Random;
import pl.krug.neural.dao.NetworkModelDAO;

/**
 * Utility class to construct networks Random, empty and so on
 *
 * @author edhendil
 *
 */
public class NetworkModelFactory {

    NetworkModelDAO _dao = new NetworkModelDAO();
    Random rand = new Random();

    public NetworkModel createEmptyNetwork() {
	throw new UnsupportedOperationException();
    }

    /**
     * Supports only normal neurons
     *
     * @param neuronsSize
     * @param interNodesSize
     * @param linkProb
     * @return
     */
    public NetworkModel createRandomNetwork(int neuronsSize, int interNodesSize, double linkProb) {
	NetworkModel model = new NetworkModel();
	NeuronType normalType = _dao.getNeuronType(1);
	// create neurons
	for (int i = 0; i < neuronsSize; i++) {
	    NeuronModel neuron = rand.nextDouble() < 0.05 ? createWeightNeuron() : createNormalNeuron();
	    neuron.setNetwork(model);
	    neuron.setPosition(i);
	    neuron.setActivationLevel(rand.nextDouble());
	    model.getNeurons().add(neuron);
	}
	// choose random neurons as interfacing nodes
	for (int i = 0; i < interNodesSize; i++) {
	    int index = rand.nextInt(neuronsSize);
	    model.getInterfacingNodes().add(model.getNeurons().get(index));
	}
	// create random links
	for (int i = 0; i < neuronsSize; i++) {
	    for (int j = 0; j < neuronsSize; j++) {
		// randomize the chance to create a link
		if (rand.nextDouble() < linkProb) {
		    NeuralLinkModel link = createNeuralLink();
		    link.setDestination(model.getNeurons().get(j));
		    model.getNeurons().get(i).addLink(link);
		}
	    }
	}
	return model;
    }

    public NeuronModel createNormalNeuron() {
	NeuronModel model = new NeuronModel();
	model.setType(_dao.getNeuronType(1));
	return model;
    }

    public NeuronModel createWeightNeuron() {
	NeuronModel model = new NeuronModel();
	model.setType(_dao.getNeuronType(2));
	return model;
    }

    public NeuralLinkModel createNeuralLink() {
	NeuralLinkModel link = new NeuralLinkModel();
	link.setChangeFactor(rand.nextDouble());
	link.setWeight(rand.nextGaussian());
	return link;
    }
}
