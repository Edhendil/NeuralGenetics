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
 *
 * @author edhendil
 *
 */
public abstract class BaseNeuron extends BasicNetworkElement implements NetworkNode {

    private double _nodeActivationLevel;

    public double getNodeActivationLevel() {
        return _nodeActivationLevel;
    }

    public void setNodeActivationLevel(double _nodeActivationLevel) {
        this._nodeActivationLevel = _nodeActivationLevel;
    }
}
