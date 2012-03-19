package pl.krug.neural.network.model;

import java.util.Random;
import pl.krug.neural.dao.NetworkModelDAO;
import pl.krug.neural.network.neuron.NeuronTypeEnum;

/**
 * Utility class to construct networks Random, empty and so on
 *
 * @author edhendil
 *
 */
public class NetworkModelFactory {
    
    NetworkModelDAO _dao = new NetworkModelDAO();
    Random rand = new Random();
    
    public NetworkModel createEmptyNetwork() {
        throw new UnsupportedOperationException();
    }

    /**
     * Supports only normal neurons
     * Turned off weight neurons as they were causing troubles in networks.
     *
     * @param neuronsSize
     * @param interNodesSize
     * @param linkProb
     * @return
     */
    public NetworkModel createRandomNetwork(int neuronsSize, int interNodesSize, double linkProb) {
        NetworkModel model = new NetworkModel();
        // create neurons
        for (int i = 0; i < neuronsSize; i++) {
            NeuronModel neuron = rand.nextDouble() < 0.00 ? createWeightNeuron() : createNormalNeuron();
            neuron.setNetwork(model);
            neuron.setPosition(i);
            neuron.setActivationLevel(rand.nextDouble()*2);
            model.getNeurons().add(neuron);
        }
        // choose random neurons as interfacing nodes
        for (int i = 0; i < interNodesSize; i++) {
            int index = rand.nextInt(neuronsSize);
            model.getInterfacingNodes().add(model.getNeurons().get(index));
        }
        // create random links
        for (int i = 0; i < neuronsSize; i++) {
            for (int j = 0; j < neuronsSize; j++) {
                // randomize the chance to create a link
                if (rand.nextDouble() < linkProb) {
                    NeuralLinkModel link = createNeuralLink();
                    link.setDestination(model.getNeurons().get(j));
                    model.getNeurons().get(i).addLink(link);
                }
            }
        }
        return model;
    }
    
    public NeuronModel createNormalNeuron() {
        NeuronModel model = new NeuronModel();
        model.setType(NeuronTypeEnum.NORMAL);
        return model;
    }
    
    public NeuronModel createWeightNeuron() {
        NeuronModel model = new NeuronModel();
        model.setType(NeuronTypeEnum.WEIGHT);
        return model;
    }
    
    public NeuralLinkModel createNeuralLink() {
        NeuralLinkModel link = new NeuralLinkModel();
        link.setChangeFactor(rand.nextDouble());
        link.setWeight(rand.nextGaussian()/100.0);
        return link;
    }
}
