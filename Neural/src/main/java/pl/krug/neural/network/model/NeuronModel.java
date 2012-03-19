package pl.krug.neural.network.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import pl.krug.neural.network.neuron.NeuronTypeEnum;

@Entity
@Table(name = "Neurons")
public class NeuronModel {

    private Long _id;
    private NeuronTypeEnum _type;
    // network this neuron is a part of
    private NetworkModel _network;
    // order of neurons in network
    private int _position;
    // neuron activation level
    private double _activationLevel;
    // links outgoing from this neuron
    private List<NeuralLinkModel> _links = new ArrayList<NeuralLinkModel>();

    public NeuronModel() {
    }

    /**
     * Cloning constructor, leaves id, network, position and links empty They
     * have to be assigned manually
     *
     * @param model
     */
    public NeuronModel(NeuronModel model) {
        _type = model._type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id")
    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }

    @Column(name="nt_id")
    @Enumerated(EnumType.ORDINAL)
    public NeuronTypeEnum getType() {
        return _type;
    }

    public void setType(NeuronTypeEnum type) {
        _type = type;
    }

    @ManyToOne
    @JoinColumn(name = "network_id")
    public NetworkModel getNetwork() {
        return _network;
    }

    public void setNetwork(NetworkModel network) {
        _network = network;
    }

    @Column(name = "position")
    public int getPosition() {
        return _position;
    }

    public void setPosition(int position) {
        _position = position;
    }

    @OneToMany(mappedBy = "source", orphanRemoval = true)
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    public List<NeuralLinkModel> getLinks() {
        return _links;
    }

    public void setLinks(List<NeuralLinkModel> links) {
        _links = links;
    }

    @Column(name="activation")
    public double getActivationLevel() {
        return _activationLevel;
    }

    public void setActivationLevel(double _activationLevel) {
        this._activationLevel = _activationLevel;
    }

    /**
     * To maintain both sides of relationship
     *
     * @param link
     */
    public void addLink(NeuralLinkModel link) {
        if (link.getSource() != this) {
            link.setSource(this);
        }
        _links.add(link);
    }
}
