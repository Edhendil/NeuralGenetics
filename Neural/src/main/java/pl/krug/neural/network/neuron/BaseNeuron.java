package pl.krug.neural.network.neuron;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalType;

/**
 * Make it thread safe?
 * @author edhendil
 *
 */
public abstract class BaseNeuron implements Neuron {

    private double _currentEnergy = 0.0;
    private double _nodeActivationLevel;
    private Queue<NeuralSignal> _signals = new LinkedList<NeuralSignal>();
    protected List<SignalListener> _signalListeners = new ArrayList<SignalListener>();

    public BaseNeuron() {
    }

    /**
     * @see pl.krug.neural.network.neuron.Neuron#setCurrentEnergy(double)
     */
    @Override
    public void setCurrentEnergy(double currentEnergy) {
        _currentEnergy = currentEnergy;
    }

    /**
     * @see pl.krug.neural.network.neuron.Neuron#getCurrentEnergy()
     */
    @Override
    public double getCurrentEnergy() {
        return _currentEnergy;
    }

    public double getNodeActivationLevel() {
        return _nodeActivationLevel;
    }

    public void setNodeActivationLevel(double _nodeActivationLevel) {
        this._nodeActivationLevel = _nodeActivationLevel;
    }
    
    @Override
    public void addSignalListener(SignalListener listener) {
        _signalListeners.add(listener);
    }

    @Override
    public void removeSignalListener(SignalListener listener) {
        _signalListeners.remove(listener);
    }

    @Override
    public void signalReceived(NeuralSignal signal) {
        _signals.add(signal);
    }

    @Override
    public void consumeSignals() {
        // backup old value
        double newEnergy = _currentEnergy;
        // for all signals in the queue
        while (!_signals.isEmpty()) {
            NeuralSignal signal = _signals.poll();
            switch (signal.getType()) {
                case ENERGY:
                    newEnergy += signal.getStrength();
                    break;
                case WEIGHT_IMPULSE:
                    for (SignalListener listener : _signalListeners) {
                        listener.signalReceived(new NeuralSignal(SignalType.WEIGHT,
                                signal.getStrength()));
                    }
                    break;
                case SENSITIVITY_IMPULSE:
                    for (SignalListener listener : _signalListeners) {
                        listener.signalReceived(new NeuralSignal(
                                SignalType.SENSITIVITY, signal.getStrength()));
                    }
                    break;
            }
        }
        // dont let energy fall below 0
        _currentEnergy = newEnergy > 0 ? newEnergy : 0.0;
    }
}
