package pl.krug.game.labirynth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.krug.game.NeuralPlayer;
import pl.krug.genetic.GeneticEvaluator;

/**
 * 4 directions, 4 actions. One actions moves forward, two do nothing and one
 * moves backward. Input: 4 neighboring squares. 1 if can go, 0 if not. Rather
 * testing than real game.
 * 
 * @author edhendil
 * 
 */
public class LabirynthGame implements GeneticEvaluator<NeuralPlayer> {

	// always
	private int _inputSize = 4;
	private int _actionSize = 4;

	// always
	private List<NeuralPlayer> _players = new ArrayList<NeuralPlayer>();

	// always but different state class
	private Map<NeuralPlayer, LabirynthPlayerState> _states = new HashMap<NeuralPlayer, LabirynthPlayerState>();

	// game specific
	// numbers from 0 to 3, left, up, right, down
	// path you should take to finish this labirynth
	private List<Integer> _labirynth = new ArrayList<Integer>();

	public void registerPlayer(NeuralPlayer player) {
		_players.add(player);
		LabirynthPlayerState state = new LabirynthPlayerState();
		state.setPosition(0);
		_states.put(player, state);
	}

	public void removePlayer(NeuralPlayer player) {
		_players.remove(player);
		_states.remove(player);
	}

	public void runIteration() {
		double[] neuralInput, neuralOutput;
		for (NeuralPlayer player : _players) {
			neuralInput = getPlayerNeuralState(player);
			player.runNetwork(neuralInput, 50);
			neuralOutput = player.getResponse();
			changePlayerState(_states.get(player), neuralOutput);
		}
	}

	// prepares input
	private double[] getPlayerNeuralState(NeuralPlayer player) {
		double[] result = new double[_inputSize];
		LabirynthPlayerState state = _states.get(player);
		// last right direction and new one
		// there are only two directions you can take at any given time
		Integer lastDir = null, nowDir = null;
		for (int i = 0; i < 4; i++) {
			result[i] = 0;
		}
		if (state.getPosition() > 0) {
			lastDir = _labirynth.get(state.getPosition() - 1);
			int idx = lastDir + 2;
			idx = idx > 3 ? idx - 4 : idx;
			result[idx] = 1;
		}
		nowDir = _labirynth.get(state.getPosition());
		result[nowDir] = 1;
		return result;
	}

	// responds to output
	private void changePlayerState(LabirynthPlayerState state,
			double[] output) {
		int actionIdx = 0;
		double maxEnergy = 0;
		for (int i = 0; i < _actionSize; i++) {
			if (output[i] > maxEnergy) {
				maxEnergy = output[i];
				actionIdx = i;
			}
		}
		if (actionIdx == _labirynth.get(state.getPosition()))
			state.setPosition(state.getPosition() + 1);
		else if (state.getPosition() != 0
				&& Math.abs(_labirynth.get(state.getPosition() - 1) - actionIdx) == 2)
			state.setPosition(state.getPosition() - 1);
	}

	@Override
	public double evaluate(NeuralPlayer individual) {
		return _states.get(individual).getPosition() + 1;
	}

}
