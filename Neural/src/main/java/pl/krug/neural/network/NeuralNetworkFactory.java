package pl.krug.neural.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NeuralLinkModel;
import pl.krug.neural.network.model.NeuronModel;
import pl.krug.neural.network.neuron.NeuralLink;
import pl.krug.neural.network.neuron.Neuron;
import pl.krug.neural.network.neuron.NormalNeuron;
import pl.krug.neural.network.neuron.WeightNeuron;

/**
 * Creates NeuralNetwork object out of NetworkModel.
 *
 * @author edhendil
 *
 */
public class NeuralNetworkFactory {

    public NeuralNetwork createNetwork(NetworkModel model) {
	NeuralNetwork net = new NeuralNetwork();
	List<Neuron> neurons = new ArrayList<Neuron>();
	List<Neuron> interfacingNodes = new ArrayList<Neuron>(model.getInterfacingNodes().size());
	List<NeuralLink> links = new ArrayList<NeuralLink>();

	// helpers
	// map to find neural neurons
	Map<NeuronModel, Neuron> positions = new HashMap<NeuronModel, Neuron>();

	// create all neurons
	for (NeuronModel neu : model.getNeurons()) {
	    Neuron neuron = null;
	    if (neu.getType().getId() == 1) {
		NormalNeuron norm = new NormalNeuron();
		norm.setNodeActivationLevel(neu.getActivationLevel());
		neuron = norm;
	    } else if (neu.getType().getId() == 2) {
		WeightNeuron weight = new WeightNeuron();
		weight.setNodeActivationLevel(neu.getActivationLevel());
		neuron = weight;
	    }

	    // if its null here then there's an error and I want to know about
	    // it, should be moved to test
	    neurons.add(neuron);

	    // add to helper map
	    positions.put(neu, neuron);
	}

	// put sensors and effectors in proper arrays
	for (NeuronModel neu : model.getInterfacingNodes()) {
	    interfacingNodes.add(positions.get(neu));
	}

	// create all links
	for (NeuronModel neu : model.getNeurons()) {
	    for (NeuralLinkModel link : neu.getLinks()) {
		NeuralLink neuLink = new NeuralLink();
		neuLink.setChangeFactor(link.getChangeFactor());
		neuLink.setWeight(link.getWeight());
		// set as a listener at the source neuron
		positions.get(link.getSource()).addSignalListener(neuLink);
		// link to the destination
		neuLink.addSignalListener(positions.get(link.getDestination()));
		links.add(neuLink);
	    }
	}

	net.setNeurons(neurons);
	net.setInterfacingNodes(interfacingNodes);
	net.setLinks(links);

	return net;
    }
}