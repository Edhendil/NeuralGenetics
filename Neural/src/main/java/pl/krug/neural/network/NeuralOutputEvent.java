package pl.krug.neural.network;

public class NeuralOutputEvent {

	private double _strenght;

	public NeuralOutputEvent() {}
	
	public NeuralOutputEvent(double strenght) {
		_strenght = strenght;
	}
	
	public double getStrenght() {
		return _strenght;
	}

	public void setStrenght(double strenght) {
		_strenght = strenght;
	}
	
}
