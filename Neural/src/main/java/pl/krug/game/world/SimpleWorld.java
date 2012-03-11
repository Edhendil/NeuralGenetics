package pl.krug.game.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.krug.game.NeuralPlayer;

/**
 * Simplest world
 * Input: {1,0}, {0,1}
 * Output: {1}, {0}
 * The bigger the difference the lower the score
 * Not used now
 * @author edhendil
 *
 */
public class SimpleWorld implements GameWorld {

	// world state
	private int _inputValue;
	private double[] _neuralInput;

	// players states, every player has to have a state or at least an entry in
	// this map
	// here the state is composed only of a double value
	private final Map<NeuralPlayer, Double> _states = new HashMap<NeuralPlayer, Double>();

	public SimpleWorld() {
		startNextRound();
	}

	/**
	 * 
	 * @param individual
	 * @return
	 */
	public double evaluate(NeuralPlayer individual) {
		double result = _states.get(individual);
		System.out.println("Network eval: " + result);
		return result;
	}

	/**
	 * Resets world state and players state but does not remove players from the
	 * world
	 */
	@Override
	public void resetWorldState() {
		for (NeuralPlayer player : _states.keySet()) 
			createPlayerState(player);
		startNextRound();
	}

	/**
	 * Creates input for player based on his and worlds state
	 * 
	 * @param player
	 * @return
	 */
	@Override
	public double[] getInputForPlayer(NeuralPlayer player) {
		return _neuralInput;
	}

	/**
	 * Takes all responses from players, update their states and the worlds
	 * Single tick/round
	 */
	@Override
	public void updateWorldState() {
		// players state
		for (NeuralPlayer player : _states.keySet()) {
			double[] neuralOutput = player.getResponse();
			_states.put(
					player,
					_states.get(player)
							- Math.abs(_inputValue - neuralOutput[0]));
		}
		// world state
		startNextRound();
	}

	/**
	 * Sets all important aspects
	 */
	private void startNextRound() {
		Random rand = new Random();
		_inputValue = rand.nextInt(2);
		_neuralInput = new double[] { _inputValue, 1 - _inputValue };
	}
	
	@Override
	public void createPlayerState(NeuralPlayer player) {
		_states.put(player, 0.0);
	}
	
	@Override
	public void removePlayerState(NeuralPlayer player) {
		_states.remove(player);
	}
	
	@Override
	public void removeAllPlayerStates() {
		_states.clear();
	}

}
