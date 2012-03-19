/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.neural.network.neuron;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalEvent;
import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalType;

/**
 *
 * @author Edhendil
 */
public abstract class BasicNetworkElement implements NetworkElement {

    public BasicNetworkElement() {
    }
    private Queue<NeuralSignal> _signals = new LinkedList<NeuralSignal>();
    private List<SignalListener> _signalListeners = new ArrayList<SignalListener>();

    @Override
    public void addSignalListener(SignalListener listener) {
        _signalListeners.add(listener);
    }

    @Override
    public void removeSignalListener(SignalListener listener) {
        _signalListeners.remove(listener);
    }

    @Override
    public void signalReceived(SignalEvent event) {
        _signals.add(event.getSignal());
    }

    protected List<SignalListener> getSignalListeners() {
        return _signalListeners;
    }

    protected Queue<NeuralSignal> getSignals() {
        return _signals;
    }

    protected void fireSignal(double strength, SignalType type) {
        for (SignalListener listener : getSignalListeners()) {
            listener.signalReceived(new SignalEvent(this, new NeuralSignal(type,
                    strength)));
        }
    }
}
