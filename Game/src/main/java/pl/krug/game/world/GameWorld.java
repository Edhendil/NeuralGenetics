package pl.krug.game.world;

import pl.krug.game.NeuralPlayer;
import pl.krug.genetic.GeneticEvaluator;

/**
 * Holds info on the state of the world and all of players (their ingame avatars)
 * @author edhendil
 *
 */
public interface GameWorld extends GeneticEvaluator<NeuralPlayer> {

	public void removeAllPlayerStates();

	public void removePlayerState(NeuralPlayer player);

	public void createPlayerState(NeuralPlayer player);

	/**
	 * Next step/round/tick/whateva
	 */
	public void updateWorldState();

	public double[] getInputForPlayer(NeuralPlayer player);

	public void resetWorldState();
	
}
