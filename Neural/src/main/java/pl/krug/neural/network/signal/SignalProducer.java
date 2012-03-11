package pl.krug.neural.network.signal;

public interface SignalProducer {

	public void addSignalListener(SignalListener listener);
	public void removeSignalListener(SignalListener listener);
	public void produceSignals();
	
}
