package pl.krug.neural.network.neuron;

import java.util.ArrayList;
import java.util.List;

import pl.krug.neural.network.NeuralOutputEvent;
import pl.krug.neural.network.NeuralOutputListener;
import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalType;

/**
 * To be removed
 * @author edhendil
 *
 */
public class Effector extends NormalNeuron {

	List<NeuralOutputListener> _outputListeners = new ArrayList<NeuralOutputListener>();

	public void addNeuralOutputListener(NeuralOutputListener listener) {
		_outputListeners.add(listener);
	}

	public void removeNeuralOutputListener(NeuralOutputListener listener) {
		_outputListeners.remove(listener);
	}

	@Override
	public void produceSignals() {
		if (getCurrentEnergy() > 0) {
			// act like a normal neuron
			double signalStrenght = getCurrentEnergy()
					/ _signalListeners.size();
			for (SignalListener listener : _signalListeners) {
				listener.signalReceived(new NeuralSignal(SignalType.ENERGY,
						signalStrenght));
			}
			// part responsible for firing any output events
			for (NeuralOutputListener listen : _outputListeners) {
				listen.outputReceived(new NeuralOutputEvent(getCurrentEnergy()));
			}
			setCurrentEnergy(0);
		}

	}

}
