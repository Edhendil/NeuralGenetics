package pl.krug.neural.network.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NeuronTypes")
public class NeuronType {

	private Long _id;
	private String _name;
	
	public NeuronType() {}
	
	@Id
	@Column(name="nt_id")
	public Long getId() {
		return _id;
	}
	public void setId(Long id) {
		_id = id;
	}
	@Column(name="name")
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}
	
}
