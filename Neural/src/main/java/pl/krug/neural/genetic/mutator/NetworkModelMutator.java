package pl.krug.neural.genetic.mutator;

import java.util.Random;
import pl.krug.genetic.GeneticMutator;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NetworkModelFactory;
import pl.krug.neural.network.model.NeuralLinkModel;
import pl.krug.neural.network.model.NeuronModel;

/**
 * Changes almost all aspects of the network but not the number of neurons in it
 * 
 * @author edhendil
 * 
 */
public class NetworkModelMutator implements GeneticMutator<NetworkModel> {

	private NetworkModelFactory _factory = new NetworkModelFactory();
	private double _mutationRate = 0.01;

	@Override
	public NetworkModel mutate(NetworkModel individual) {
		Random rand = new Random();
		// neuron mutations
		// add a neuron? not yet implemented
		for (NeuronModel neuron : individual.getNeurons()) {
			// should mutation occur in this neuron?
			if (rand.nextDouble() < _mutationRate) {
				double mutationType = rand.nextDouble();
				// change neuron type?
				if (mutationType < 0.1) {
					// TODO
				} else {
					// mutate links
					double linkRandType = rand.nextDouble();
					if (linkRandType < 0.2) {
						// remove random link if there is any
						if (!neuron.getLinks().isEmpty())
							neuron.getLinks().remove(
									rand.nextInt(neuron.getLinks().size()));
					} else if (linkRandType < 0.4) {
						// add a link to random neuron
						NeuralLinkModel link = _factory.createNeuralLink();
						link.setDestination(individual.getNeurons().get(
								rand.nextInt(individual.getNeurons().size())));
						neuron.addLink(link);
					} else {
						// randomize weight if there is at least one link
						if (!neuron.getLinks().isEmpty()) {
							int randomNumber = rand.nextInt(neuron.getLinks()
									.size());
							neuron.getLinks().get(randomNumber)
									.setWeight(rand.nextGaussian());

						}
					}
				}
			}
		}

		// network - highest level
		// mutate sensors?
		if (rand.nextDouble() < _mutationRate) {
			// replace a random sensor with a random neuron
			individual.getSensors().set(
					rand.nextInt(individual.getSensors().size()),
					individual.getNeurons().get(
							rand.nextInt(individual.getNeurons().size())));
		}
		// mutate effectors?
		if (rand.nextDouble() < _mutationRate) {
			// replace a random effector with a random neuron
			individual.getEffectors().set(
					rand.nextInt(individual.getEffectors().size()),
					individual.getNeurons().get(
							rand.nextInt(individual.getNeurons().size())));
		}

		return individual;
	}

}
