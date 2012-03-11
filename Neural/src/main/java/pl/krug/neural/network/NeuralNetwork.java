package pl.krug.neural.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pl.krug.neural.network.neuron.NeuralLink;
import pl.krug.neural.network.neuron.Neuron;
import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalProducer;

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
	// part of neurons responsible for processing the input
	// helper
	private List<Neuron> _sensors;
	// part of neurons responsible for affecting the world
	// helper
	private List<Neuron> _effectors;

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
			Neuron neu = getSensors().get(i);
			if (neu != null)
				neu.setCurrentEnergy(neu.getCurrentEnergy() + state[i]);
		}
		// do a step
		runNetworkStep();
	}

	public double[] getNetworkResponse() {
		double[] out = new double[getEffectors().size()];
		for (int i = 0; i < out.length; i++) {
			if (getEffectors().get(i) != null)
				out[i] = getEffectors().get(i).getCurrentEnergy();
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

	public List<Neuron> getSensors() {
		return _sensors;
	}

	public List<Neuron> getEffectors() {
		return _effectors;
	}

	public void setEffectors(List<Neuron> effectors) {
		_effectors = effectors;
	}

	public void setSensors(List<Neuron> sensors) {
		_sensors = sensors;
	}
}
