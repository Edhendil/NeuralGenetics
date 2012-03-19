package pl.krug.neural.network.signal;

/**
 * Generic neurotransmitter. Only strength and type matters. Add TTL (time to
 * live)? How to make different classes and allow neurons to make functions
 * processing them. Or enumeration is good?
 *
 * @author edhendil
 *
 */
public class NeuralSignal {

    private double _strength;
    private SignalType _type;

    public NeuralSignal() {
    }

    public NeuralSignal(SignalType type, double strength) {
	_strength = strength;
	_type = type;
    }

    public NeuralSignal(NeuralSignal signal) {
	_strength = signal._strength;
	_type = signal._type;
    }

    public void setStrength(double strength) {
	_strength = strength;
    }

    public double getStrength() {
	return _strength;
    }

    public SignalType getType() {
	return _type;
    }

    public void setType(SignalType type) {
	_type = type;
    }
}
