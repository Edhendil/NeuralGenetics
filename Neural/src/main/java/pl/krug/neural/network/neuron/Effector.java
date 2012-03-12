package pl.krug.neural.network.neuron;

import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalType;

/**
 * To be removed
 * @author edhendil
 *
 */
public class Effector extends NormalNeuron {

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
			setCurrentEnergy(0);
		}

	}

}
