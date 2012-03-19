package pl.krug.neural.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NeuralLinkModel;
import pl.krug.neural.network.model.NeuronModel;
import pl.krug.neural.network.neuron.*;

/**
 * Creates NeuralNetwork object out of NetworkModel.
 *
 * @author edhendil
 *
 */
public class NeuralNetworkFactory {

    public NeuralNetwork createNetwork(NetworkModel model) {
        NeuralNetwork net = new NeuralNetwork();
        List<NetworkNode> neurons = new ArrayList<NetworkNode>();
        List<NetworkNode> interfacingNodes = new ArrayList<NetworkNode>(model.getInterfacingNodes().size());
        List<NeuralLink> links = new ArrayList<NeuralLink>();

        // helpers
        // map to find neural neurons
        Map<NeuronModel, NetworkNode> modelNeuronMap = new HashMap<NeuronModel, NetworkNode>();

        // create all neurons
        for (NeuronModel neu : model.getNeurons()) {
            NetworkNode neuron = null;
            if (neu.getType() == NeuronTypeEnum.NORMAL) {
                NormalNeuron norm = new NormalNeuron();
                norm.setNodeActivationLevel(neu.getActivationLevel());
                neuron = norm;
            } else if (neu.getType() == NeuronTypeEnum.WEIGHT) {
                WeightNeuron weight = new WeightNeuron();
                weight.setNodeActivationLevel(neu.getActivationLevel());
                neuron = weight;
            }

            // if its null here then there's an error and I want to know about
            // it, should be moved to test
            neurons.add(neuron);

            // add to helper map
            modelNeuronMap.put(neu, neuron);
        }

        // put sensors and effectors in proper arrays
        for (NeuronModel neu : model.getInterfacingNodes()) {
            interfacingNodes.add(modelNeuronMap.get(neu));
        }

        // create all links
        for (NeuronModel neu : model.getNeurons()) {
            for (NeuralLinkModel link : neu.getLinks()) {
                NeuralLink neuLink = new NeuralLink();
                neuLink.setChangeFactor(link.getChangeFactor());
                neuLink.setWeight(link.getWeight());
                // set as a listener at the source neuron
                modelNeuronMap.get(link.getSource()).addSignalListener(neuLink);
                // link to the destination
                neuLink.addSignalListener(modelNeuronMap.get(link.getDestination()));
                links.add(neuLink);
            }
        }

        net.setNeurons(neurons);
        net.setInterfacingNodes(interfacingNodes);
        net.setLinks(links);

        // network listens to what happens with interfacing nodes
        for (NetworkNode node : net.getInterfacingNodes()) {
            node.addSignalListener(net);
        }

        return net;
    }
}