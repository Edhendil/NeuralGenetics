package pl.krug.neural.network.neuron;

import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalListener;
import pl.krug.neural.network.signal.SignalType;

public class WeightNeuron extends BaseNode {
    
    @Override
    public void processElement() {
        if (isProcessingRequired()) {
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
            if (weightStrength > getNodeActivationLevel()) {
                fireSignal(weightStrength, SignalType.WEIGHT);
            }
            if (sensitivityStrength > getNodeActivationLevel()) {
                fireSignal(sensitivityStrength, SignalType.SENSITIVITY);
            }
            if (energyStrength > getNodeActivationLevel()) {
                fireSignal(energyStrength, SignalType.WEIGHT_IMPULSE);
            }
            setProcessingRequired(false);
        }
    }
}
