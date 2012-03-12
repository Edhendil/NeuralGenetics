package pl.krug.neural.network.neuron;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalProducer;
import pl.krug.neural.network.signal.SignalType;

/**
 * One-way link between neurons.
 * 
 * @author edhendil
 * 
 */
public class NeuralLink implements SignalListener, SignalProducer {

	private double _changeFactor;
	private double _weight;

	private Queue<NeuralSignal> _signals = new LinkedList<NeuralSignal>();
	private List<SignalListener> _signalListeners = new ArrayList<SignalListener>();

	public double getChangeFactor() {
		return _changeFactor;
	}

	public void setChangeFactor(double changeFactor) {
		_changeFactor = changeFactor;
	}

	public double getWeight() {
		return _weight;
	}

	public void setWeight(double weight) {
		_weight = weight;
	}

	public void addSignalListener(SignalListener listener) {
		_signalListeners.add(listener);
	}

	public void removeSignalListener(SignalListener listener) {
		_signalListeners.remove(listener);
	}

	@Override
	public void signalReceived(NeuralSignal signal) {
		_signals.add(signal);
	}

	/**
	 * Consume all signals simultaneously
	 */
	public void consumeSignals() {
		// backup old values
		double newWeight = _weight;
		double newChangeFactor = _changeFactor;
		// for all signals in the queue
		while (!_signals.isEmpty()) {
			NeuralSignal signal = _signals.poll();
			switch (signal.getType()) {
			case ENERGY:
				for (SignalListener listener : _signalListeners) {
					listener.signalReceived(new NeuralSignal(SignalType.ENERGY,
							signal.getStrength() * _weight));
//					System.out.println("energy");
				}
				break;
			case WEIGHT_IMPULSE:
				for (SignalListener listener : _signalListeners) {
					listener.signalReceived(new NeuralSignal(
							SignalType.WEIGHT_IMPULSE, signal.getStrength()
									* _weight));
//					System.out.println("w impulse");
				}
				break;
			case SENSITIVITY_IMPULSE:
				for (SignalListener listener : _signalListeners) {
					listener.signalReceived(new NeuralSignal(
							SignalType.SENSITIVITY_IMPULSE, signal.getStrength()
									* _weight));
//					System.out.println("s impulse");
				}
				break;
			case WEIGHT:
				newWeight += signal.getStrength() * _changeFactor;
//				System.out.println("w");
				break;
			case SENSITIVITY:
				newChangeFactor += signal.getStrength();
//				System.out.println("s");
				break;
			}
		}
		_weight = newWeight;
		// dont let change factor fall below 0
		_changeFactor = newChangeFactor > 0 ? newChangeFactor : 0.0;
	}

	/**
	 * Does not produce any signals
	 */
	@Override
	public void produceSignals() {

	}

}
