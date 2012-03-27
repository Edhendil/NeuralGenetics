package pl.krug.neural.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import pl.krug.neural.network.model.NetworkModel;
import pl.krug.neural.network.model.NeuralLinkModel;
import pl.krug.neural.network.model.NeuronModel;

/**
 * 
 * @author edhendil
 * 
 */
public class NetworkModelDAO {

	protected EntityManager _em;

	public NetworkModelDAO() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("neural");
		_em = emf.createEntityManager();
	}

	public NetworkModel getNetwork(long id) {
		return _em.find(NetworkModel.class, id);
	}

	public List<NetworkModel> getNetworkList() {
		return _em.createQuery("from NetworkModel", NetworkModel.class).getResultList();
	}
	
	/**
	 * Ta metoda powinna zostac poprawiona, na pewno zapisuje za pierwszym
	 * razem, ale update?
	 * 
	 * @param network
	 * @return
	 */
	public NetworkModel saveNetwork(NetworkModel network) {
		if (network.getId() == null) {
			saveNewNetwork(network);
			return network;
		} else {
			_em.getTransaction().begin();
			NetworkModel result = _em.merge(network);
			_em.getTransaction().commit();
			return result;
		}
	}
	
	public void saveNetworks(List<NetworkModel> networks) {
		for (NetworkModel model : networks) {
			saveNetwork(model);
		}
		_em.clear();
	}

	public void removeNetwork(NetworkModel network) {
		_em.getTransaction().begin();
		for (NeuronModel neuron : network.getNeurons())
			for (NeuralLinkModel link : neuron.getLinks())
				_em.remove(link);
		for (NeuronModel neuron : network.getNeurons())
			_em.remove(neuron);
		_em.remove(network);
		_em.getTransaction().commit();
	}

	/**
	 * Parts of network must be saved in proper order neurons first, then links,
	 * and whole network at the end Otherwise there are nulls instead of ids
	 */
	private void saveNewNetwork(NetworkModel network) {
		_em.getTransaction().begin();
		_em.persist(network);
		for (NeuronModel neuron : network.getNeurons())
			_em.persist(neuron);
		for (NeuronModel neuron : network.getNeurons())
			for (NeuralLinkModel link : neuron.getLinks())
				_em.persist(link);
		_em.getTransaction().commit();
	}

}
