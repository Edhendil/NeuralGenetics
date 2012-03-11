package pl.krug.neural.network.neuron;

import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalProducer;

public interface Neuron extends SignalProducer, SignalListener {

	public abstract void setCurrentEnergy(double currentEnergy);
	public abstract double getCurrentEnergy();

}