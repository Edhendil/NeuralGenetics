package pl.krug.neural.network.model;

import javax.persistence.*;

@Entity
@Table(name="NeuralLinks")
public class NeuralLinkModel {
	
	private Long _id;
	private NeuronModel _source;
	private NeuronModel _destination;
	
	private double _changeFactor;
	private double _weight;
	
	public NeuralLinkModel() {}
	
	/**
	 * Clones link's properties but not the neurons it is connecting
	 * @param model
	 */
	public NeuralLinkModel(NeuralLinkModel model) {
		this._changeFactor = model._changeFactor;
		this._weight = model._weight;
	}
	
	@Id
	@GeneratedValue
	@Column(name="l_id")
	public Long getId() {
		return _id;
	}
	public void setId(Long id) {
		_id = id;
	}
	@ManyToOne
	@JoinColumn(name="source_id")
	public NeuronModel getSource() {
		return _source;
	}
	public void setSource(NeuronModel source) {
		_source = source;
	}
	@ManyToOne
	@JoinColumn(name="dest_id")
	public NeuronModel getDestination() {
		return _destination;
	}
	public void setDestination(NeuronModel destination) {
		_destination = destination;
	}
	@Column(name="sensitivity")
	public double getChangeFactor() {
		return _changeFactor;
	}
	public void setChangeFactor(double changeFactor) {
		_changeFactor = changeFactor;
	}
	@Column(name="weight")
	public double getWeight() {
		return _weight;
	}
	public void setWeight(double weight) {
		_weight = weight;
	}

}
