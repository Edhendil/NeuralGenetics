package pl.krug.neural.network.neuron;

import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalProducer;

/**
 * Usunąć settery i gettery z interfejsów.
 *
 * @author edhendil
 */
public interface Neuron extends SignalProducer, SignalListener {

    public abstract void setCurrentEnergy(double currentEnergy);

    public abstract double getCurrentEnergy();
}