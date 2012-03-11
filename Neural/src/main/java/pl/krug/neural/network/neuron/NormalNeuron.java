package pl.krug.neural.network.neuron;

import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalType;

public class NormalNeuron extends BaseNeuron {

	@Override
	public void produceSignals() {
		if (getCurrentEnergy() > 0) {
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
