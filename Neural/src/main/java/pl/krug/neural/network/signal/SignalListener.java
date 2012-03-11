package pl.krug.neural.network.signal;

public interface SignalListener {

	public void signalReceived(NeuralSignal signal);
	public void consumeSignals();
	
}
