package pl.krug.neural.network;

import java.util.List;
import pl.krug.neural.network.neuron.NeuralLink;
import pl.krug.neural.network.neuron.Neuron;

/**
 * To be used in neural engine
 * 
 * @author edhendil
 * 
 */
public class NeuralNetwork {

	// all neurons
	private List<Neuron> _neurons;
	// all links
	private List<NeuralLink> _links;
	// part of neurons responsible for communicating with the world
	// helper
	private List<Neuron> _interfacingNodes;

	public void runNetworkStep() {
		// if helpers are not initialized, altough they should be placed in
		// setters
		// done one time and not checked every single run
		produceSignals();
		consumeProducedSignals();
	}

	public void runNetworkStep(double[] state) {
		// add energy to input neurons
		for (int i = 0; i < state.length; i++) {
			Neuron neu = getInterfacingNodes().get(i);
			if (neu != null)
				neu.setCurrentEnergy(neu.getCurrentEnergy() + state[i]);
		}
		// do a step
		runNetworkStep();
	}

	public double[] getNetworkResponse() {
		double[] out = new double[getInterfacingNodes().size()];
		for (int i = 0; i < out.length; i++) {
			if (getInterfacingNodes().get(i) != null)
				out[i] = getInterfacingNodes().get(i).getCurrentEnergy();
			else
				out[i] = 0;
		}
		return out;
	}

	private void produceSignals() {
		for (Neuron neuron : _neurons) {
			neuron.produceSignals();
		}
	}

	private void consumeProducedSignals() {
		// send energy and impulses
		for (NeuralLink link : _links) {
			link.consumeSignals();
		}
		for (Neuron neuron : _neurons) {
			neuron.consumeSignals();
		}
		// consume active signals
		for (NeuralLink link : _links) {
			link.consumeSignals();
		}
		// no more signals should be produced
	}

	public List<Neuron> getNeurons() {
		return _neurons;
	}

	public void setNeurons(List<Neuron> neurons) {
		_neurons = neurons;
	}

	public List<NeuralLink> getLinks() {
		return _links;
	}

	public void setLinks(List<NeuralLink> links) {
		_links = links;
	}

	public List<Neuron> getInterfacingNodes() {
		return _interfacingNodes;
	}

	public void setInterfacingNodes(List<Neuron> interfacingNodes) {
		_interfacingNodes = interfacingNodes;
	}
}
