package pl.krug.neural.network.neuron;

import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalType;

/**
 * One-way link between neurons.
 *
 * @author edhendil
 *
 */
public class NeuralLink extends BasicNetworkElement implements NetworkLink {

    private double _changeFactor;
    private double _weight;

    public double getChangeFactor() {
        return _changeFactor;
    }

    public void setChangeFactor(double changeFactor) {
        _changeFactor = changeFactor;
    }

    public double getWeight() {
        return _weight;
    }

    public void setWeight(double weight) {
        _weight = weight;
    }

    @Override
    public void processElement() {
        if (isProcessingRequired()) {
            // backup old values
            double newWeight = _weight;
            double newChangeFactor = _changeFactor;
            // helper vars
            double energyStrength = 0.0;
            double weightStrength = 0.0;
            double sensitivityStrength = 0.0;
            // for all signals in the queue
            while (!getSignals().isEmpty()) {
                NeuralSignal signal = getSignals().poll();
                switch (signal.getType()) {
                    case ENERGY:
                        energyStrength += signal.getStrength();
                        break;
                    case WEIGHT_IMPULSE:
                        weightStrength += signal.getStrength();
                        break;
                    case SENSITIVITY_IMPULSE:
                        sensitivityStrength += signal.getStrength();
                        break;
                    case WEIGHT:
                        newWeight += signal.getStrength() * _changeFactor;
                        break;
                    case SENSITIVITY:
                        newChangeFactor += signal.getStrength();
                        break;
                }
            }
            // fire proper signals
            if (weightStrength > 0.0) {
                fireSignal(weightStrength * _weight, SignalType.WEIGHT_IMPULSE);
            }
            if (sensitivityStrength > 0.0) {
                fireSignal(sensitivityStrength * _weight, SignalType.SENSITIVITY_IMPULSE);
            }
            if (energyStrength > 0.0) {
                fireSignal(energyStrength * _weight, SignalType.ENERGY);
            }
            // assign new weight
            _weight = newWeight;
            // dont let change factor fall below 0
            _changeFactor = newChangeFactor > 0 ? newChangeFactor : 0.0;
            setProcessingRequired(false);
        }
    }
}
