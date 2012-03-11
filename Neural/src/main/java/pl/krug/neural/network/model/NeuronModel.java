package pl.krug.neural.network.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name="Neurons")
public class NeuronModel {

	private Long _id;
	private NeuronType _type;
	// network this neuron is a part of
	private NetworkModel _network;
	// order of neurons in network
	private int _position;
	// links outgoing from this neuron
	private List<NeuralLinkModel> _links = new ArrayList<NeuralLinkModel>();
	
	public NeuronModel() {}
	
	/**
	 * Cloning constructor, leaves id, network, position and links empty
	 * They have to be assigned manually
	 * @param model
	 */
	public NeuronModel(NeuronModel model) {
		_type = model._type;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="n_id")
	public Long getId() {
		return _id;
	}
	public void setId(Long id) {
		_id = id;
	}
	@ManyToOne
	@JoinColumn(name="nt_id")
	public NeuronType getType() {
		return _type;
	}
	public void setType(NeuronType type) {
		_type = type;
	}
	@ManyToOne
	@JoinColumn(name="network_id")
	public NetworkModel getNetwork() {
		return _network;
	}
	public void setNetwork(NetworkModel network) {
		_network = network;
	}
	@Column(name="position")
	public int getPosition() {
		return _position;
	}
	public void setPosition(int position) {
		_position = position;
	}

	@OneToMany(mappedBy="source", orphanRemoval=true)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public List<NeuralLinkModel> getLinks() {
		return _links;
	}

	public void setLinks(List<NeuralLinkModel> links) {
		_links = links;
	}
	
	/**
	 * To maintain both sides of relationship
	 * @param link
	 */
	public void addLink(NeuralLinkModel link) {
		if(link.getSource() != this) {
			link.setSource(this);
		}
		_links.add(link);
	}
	
}
