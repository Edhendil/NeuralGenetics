package pl.krug.neural.network;

import java.util.Arrays;
import java.util.List;
import pl.krug.neural.network.neuron.BasicNetworkElement;
import pl.krug.neural.network.neuron.NetworkLink;
import pl.krug.neural.network.neuron.NeuralLink;
import pl.krug.neural.network.neuron.NetworkNode;
import pl.krug.neural.network.signal.NeuralSignal;
import pl.krug.neural.network.signal.SignalEvent;
import pl.krug.neural.network.signal.SignalType;

/**
 * To be used in neural engine That's right, it extends BasicNetworkElement.
 * Root node is an element as well.Basically it simplifies code.
 *
 * @author edhendil
 *
 */
public class NeuralNetwork extends BasicNetworkElement {

    // all neurons
    private List<NetworkNode> _neurons;
    // all links
    private List<NeuralLink> _links;
    // part of neurons responsible for communicating with the world
    // helper
    private List<NetworkNode> _interfacingNodes;
    // holds input data from outside (can be anything, not tied to any world model)
    // it is sent as signals to proper interfacing nodes at each network step
    private double[] input;
    // holds output data from network (network registers itself as listener on
    // interfacing nodes to catch signal firing)
    private double[] response;

    // I provide different methods to separate inner network interfaces and other modules
    public void runNetworkStep() {
        processElement();
    }

    public double[] runNetworkStep(double[] input, int rounds) {
        setInput(input);
        for (int i = 0; i < rounds; i++) {
            runNetworkStep();
        }
        return response;
    }

    /*
     * Each time these values will be sent to network nodes as signals
     */
    public void setInput(double[] input) {
        this.input = input;
    }

    /**
     * Latest state of interfacing nodes
     *
     * @return
     */
    public double[] getResponse() {
        return response;
    }

    /**
     * Must be done this way. Neurons produce signals that are dispatched to
     * links. In the second stage signals from links are dispatched to other
     * neurons. With these stages whole network does not have to be cloned in
     * order to achieve simultaneous signal consumption.
     */
    @Override
    public void processElement() {
        clearOutput();
        provideInput();
        for (NetworkNode node : _neurons) {
            node.processElement();
        }
        for (NetworkLink link : _links) {
            link.processElement();
        }
    }

    private void provideInput() {
        int i = 0;
        int interSize = _interfacingNodes.size();
        int inputSize = input.length;
        // if sizes do not match then leave it without signals
        while (i < interSize && i < inputSize) {
            _interfacingNodes.get(i).signalReceived(new SignalEvent(this, new NeuralSignal(SignalType.ENERGY, input[i])));
            ++i;
        }
    }

    /**
     * Instead of saving all signals in collection I process it immediately
     *
     * @param event
     */
    @Override
    public void signalReceived(SignalEvent event) {
        if (event.getSignal().getType() == SignalType.ENERGY) {
            response[_interfacingNodes.indexOf(event.getSource())] += event.getSignal().getStrength();
        }
    }

    private void clearOutput() {
        // array values are initialized to 0.0
        response = new double[_interfacingNodes.size()];
    }

    public List<NetworkNode> getNeurons() {
        return _neurons;
    }

    public void setNeurons(List<NetworkNode> neurons) {
        _neurons = neurons;
    }

    public List<NeuralLink> getLinks() {
        return _links;
    }

    public void setLinks(List<NeuralLink> links) {
        _links = links;
    }

    public List<NetworkNode> getInterfacingNodes() {
        return _interfacingNodes;
    }

    public void setInterfacingNodes(List<NetworkNode> interfacingNodes) {
        _interfacingNodes = interfacingNodes;
    }
}
