/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.neural.network.signal;

import pl.krug.neural.network.neuron.NetworkElement;

/**
 * Holds signal itself and source element
 *
 * @author Edhendil
 */
public class SignalEvent {

    private NetworkElement source;
    private NeuralSignal signal;

    public SignalEvent(NetworkElement source, NeuralSignal signal) {
        this.source = source;
        this.signal = signal;
    }

    public SignalEvent() {
    }

    public NeuralSignal getSignal() {
        return signal;
    }

    public void setSignal(NeuralSignal signal) {
        this.signal = signal;
    }

    public NetworkElement getSource() {
        return source;
    }

    public void setSource(NetworkElement source) {
        this.source = source;
    }
}
