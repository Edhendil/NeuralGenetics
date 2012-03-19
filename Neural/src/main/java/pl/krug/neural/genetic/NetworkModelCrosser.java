package pl.krug.neural.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import pl.krug.genetic.GeneticCrosser;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NeuralLinkModel;
import pl.krug.neural.network.model.NeuronModel;

/**
 * Assumes the number of input and output neurons is the same for both networks
 * 
 * @author edhendil
 * 
 */
public class NetworkModelCrosser implements GeneticCrosser<NetworkModel> {

	// mutator should not delete in/out neurons
	// it'll be easier that way
	private NetworkModelMutator _mutator = new NetworkModelMutator();
	
	Random _randomGenerator = new Random();

	@Override
	public List<NetworkModel> crossover(List<NetworkModel> parents) {
		NetworkModel first = parents.get(0);
		NetworkModel second = parents.get(1);

		NetworkModel fChild, sChild;
		fChild = new NetworkModel();
		sChild = new NetworkModel();

		List<NetworkModel> result = new ArrayList<NetworkModel>();
		result.add(fChild);
		result.add(sChild);

		// neurony sa na single/double crossover
		// puste miejsca ze srodka sa usuwane, ale dopiero na sam koniec
		// krzyzowania

		crossoverNeurons(parents, result);

		// in/out
		// normalny crossover, osobno w stosunku do samych neuronow, jesli
		// neuron nie istnieje na podanej pozycji w sieci wynikowej to
		// NULL

		crossoverInterfacingNodes(parents, result);

		// linki tak jak in/out, jesli brak source/dest to nie ma polaczenia

		crossoverLinks(parents, result);

		_mutator.mutate(fChild);
		_mutator.mutate(sChild);

		finalCleanup(result);

		return result;
	}

	/**
	 * Just to make sure everything is in place, all links have source/dest
	 * neurons and neurons are owned by a network Clears null values from neuron
	 * lists Sets positions as well
	 * 
	 * @param result
	 */
	private void finalCleanup(List<NetworkModel> result) {
		finalCleanup(result.get(0));
		finalCleanup(result.get(1));
	}

	private void finalCleanup(NetworkModel model) {
		int i = 0;
		model.getNeurons().removeAll(Collections.singleton(null)); 
		for (NeuronModel neuron : model.getNeurons()) {
				neuron.setPosition(i);
				i++;
				neuron.setNetwork(model);
		}
	}

	private void crossoverLinks(List<NetworkModel> parents,
			List<NetworkModel> result) {
		int maxSize = Math.max(parents.get(0).getNeurons().size(),
				parents.get(1).getNeurons().size());
		int crossoverIndex = _randomGenerator.nextInt(maxSize);

		List<NeuralLinkModel> forFirstChild = null;
		List<NeuralLinkModel> forSecondChild = null;

		for (int i = 0; i < maxSize; i++) {
			if (i < crossoverIndex) {
				forFirstChild = i < parents.get(0).getNeurons().size()
						&& result.get(0).getNeurons().get(i) != null ? parents
						.get(0).getNeurons().get(i).getLinks() : null;
				forSecondChild = i < parents.get(1).getNeurons().size()
						&& result.get(1).getNeurons().get(i) != null ? parents
						.get(1).getNeurons().get(i).getLinks() : null;
			} else {
				forFirstChild = i < parents.get(1).getNeurons().size()
						&& result.get(1).getNeurons().get(i) != null ? parents
						.get(1).getNeurons().get(i).getLinks() : null;
				forSecondChild = i < parents.get(0).getNeurons().size()
						&& result.get(0).getNeurons().get(i) != null ? parents
						.get(0).getNeurons().get(i).getLinks() : null;
			}

			if (forFirstChild != null)
				for (NeuralLinkModel item : forFirstChild) {
					if (result.get(0).getNeurons()
							.get(item.getDestination().getPosition()) != null) {
						NeuralLinkModel newLink = new NeuralLinkModel(item);
						newLink.setDestination(result.get(0).getNeurons()
								.get(item.getDestination().getPosition()));
						newLink.setSource(result.get(0).getNeurons()
								.get(item.getSource().getPosition()));
						newLink.getSource().addLink(newLink);
					}
				}

			if (forSecondChild != null)
				for (NeuralLinkModel item : forSecondChild) {
					if (result.get(1).getNeurons()
							.get(item.getDestination().getPosition()) != null) {
						NeuralLinkModel newLink = new NeuralLinkModel(item);
						newLink.setDestination(result.get(1).getNeurons()
								.get(item.getDestination().getPosition()));
						newLink.setSource(result.get(1).getNeurons()
								.get(item.getSource().getPosition()));
						newLink.getSource().addLink(newLink);
					}
				}
		}

	}

	private void crossoverInterfacingNodes(List<NetworkModel> parents,
			List<NetworkModel> result) {
		int maxSize = parents.get(0).getInterfacingNodes().size();
		int crossoverIndex = _randomGenerator.nextInt(maxSize);

		NeuronModel forFirstChild;
		NeuronModel forSecondChild;

		for (int i = 0; i < maxSize; i++) {
			if (i < crossoverIndex) {
				// if sensor in parent is not null and its counterpart by
				// position in child is not null
				// then assign this counterpart, else null
				forFirstChild = parents.get(0).getInterfacingNodes().get(i) != null
						&& result
								.get(0)
								.getNeurons()
								.get(parents.get(0).getInterfacingNodes().get(i)
										.getPosition()) != null ? result
						.get(0)
						.getNeurons()
						.get(parents.get(0).getInterfacingNodes().get(i).getPosition())
						: null;
				forSecondChild = parents.get(1).getInterfacingNodes().get(i) != null
						&& result
								.get(1)
								.getNeurons()
								.get(parents.get(1).getInterfacingNodes().get(i)
										.getPosition()) != null ? result
						.get(1)
						.getNeurons()
						.get(parents.get(1).getInterfacingNodes().get(i).getPosition())
						: null;
			} else {
				forFirstChild = parents.get(1).getInterfacingNodes().get(i) != null
						&& result
								.get(0)
								.getNeurons()
								.get(parents.get(1).getInterfacingNodes().get(i)
										.getPosition()) != null ? result
						.get(0)
						.getNeurons()
						.get(parents.get(1).getInterfacingNodes().get(i).getPosition())
						: null;
				forSecondChild = parents.get(0).getInterfacingNodes().get(i) != null
						&& result
								.get(1)
								.getNeurons()
								.get(parents.get(0).getInterfacingNodes().get(i)
										.getPosition()) != null ? result
						.get(1)
						.getNeurons()
						.get(parents.get(0).getInterfacingNodes().get(i).getPosition())
						: null;
			}

			result.get(0).getInterfacingNodes().add(forFirstChild);
			result.get(1).getInterfacingNodes().add(forSecondChild);
		}

	}

	private void crossoverNeurons(List<NetworkModel> parents,
			List<NetworkModel> result) {
		int maxSize = Math.max(parents.get(0).getNeurons().size(),
				parents.get(1).getNeurons().size());
		int crossoverIndex = _randomGenerator.nextInt(maxSize);

		NeuronModel forFirstChild;
		NeuronModel forSecondChild;

		for (int i = 0; i < maxSize; i++) {
			if (i < crossoverIndex) {
				forFirstChild = i < parents.get(0).getNeurons().size() ? parents
						.get(0).getNeurons().get(i)
						: null;
				forSecondChild = i < parents.get(1).getNeurons().size() ? parents
						.get(1).getNeurons().get(i)
						: null;
			} else {
				forFirstChild = i < parents.get(1).getNeurons().size() ? parents
						.get(1).getNeurons().get(i)
						: null;
				forSecondChild = i < parents.get(0).getNeurons().size() ? parents
						.get(0).getNeurons().get(i)
						: null;
			}

			if (forFirstChild != null)
				result.get(0).getNeurons().add(new NeuronModel(forFirstChild));
			else 
				result.get(0).getNeurons().add(null);
			if (forSecondChild != null)
				result.get(1).getNeurons().add(new NeuronModel(forSecondChild));
			else 
				result.get(1).getNeurons().add(null);

		}
	}
}
