package pl.krug.neural.network.neuron;

/**
 * Just a base for all network nodes.
 *
 * @author edhendil
 *
 */
public abstract class BaseNode extends BasicNetworkElement implements NetworkNode {

    private double _nodeActivationLevel;

    public double getNodeActivationLevel() {
        return _nodeActivationLevel;
    }

    public void setNodeActivationLevel(double _nodeActivationLevel) {
        this._nodeActivationLevel = _nodeActivationLevel;
    }
}
