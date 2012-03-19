package pl.krug.neural.network.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Representation of neural network for genetic and persistence purposes, not
 * for execution. Events are not used here.
 * IntefacingNodes - list of nodes communicating with the outside world.
 * Exactly like the tables for sensors and effectors.
 * 
 * @author edhendil
 * 
 */
@Entity
@Table(name = "Networks")
public class NetworkModel {
    
	private Long _id;
	private List<NeuronModel> _neurons = new ArrayList<NeuronModel>();
	private List<NeuronModel> _interfacingNodes = new ArrayList<NeuronModel>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "network_id")
	public Long getId() {
		return _id;
	}

	public void setId(Long id) {
		_id = id;
	}

	@OneToMany(mappedBy = "network", orphanRemoval = true)
	@OrderBy("position ASC")
	public List<NeuronModel> getNeurons() {
		return _neurons;
	}

	public void setNeurons(List<NeuronModel> neurons) {
		_neurons = neurons;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "InterfacingNodes", joinColumns = @JoinColumn(name = "network_id", unique = false), inverseJoinColumns = @JoinColumn(name = "n_id", unique = true))
	@OrderColumn(name = "orderIdx")
	public List<NeuronModel> getInterfacingNodes() {
		return _interfacingNodes;
	}

	public void setInterfacingNodes(List<NeuronModel> interfacingNodes) {
		_interfacingNodes = interfacingNodes;
	}
	
	@Override
	public String toString() {
		return _id+"";
	}

}
