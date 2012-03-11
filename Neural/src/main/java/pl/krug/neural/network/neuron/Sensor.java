package pl.krug.neural.network.neuron;

/**
 * To be removed
 * @author edhendil
 *
 */
public class Sensor extends NormalNeuron {

	public void inputReceived(double impulseStrenght) {
		setCurrentEnergy(getCurrentEnergy()+impulseStrenght);
	}

}
