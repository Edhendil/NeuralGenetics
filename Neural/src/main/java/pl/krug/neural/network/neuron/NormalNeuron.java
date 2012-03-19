package pl.krug.neural.network.neuron;

import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalType;

public class NormalNeuron extends BaseNeuron {

    @Override
    public void processElement() {
        // backup old value
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
            }
        }
        if (weightStrength > 0) {
            fireSignal(weightStrength, SignalType.WEIGHT);
        }
        if (sensitivityStrength > 0) {
            fireSignal(sensitivityStrength, SignalType.SENSITIVITY);
        }
        if (energyStrength > 0) {
            fireSignal(energyStrength, SignalType.ENERGY);
        }

    }
}
