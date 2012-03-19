/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.neural.network.neuron;

import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalListener;

/**
 * Interface for all elements of the network, be it normal neurons or links
 * between them
 *
 * @author Edhendil
 */
public interface NetworkElement extends SignalListener {

    /**
     * Method responsible for conducting all necessary operations within this
     * element. This includes consuming received signals and generating new ones
     */
    public void processElement();

    public void addSignalListener(SignalListener listener);

    public void removeSignalListener(SignalListener listener);
}
