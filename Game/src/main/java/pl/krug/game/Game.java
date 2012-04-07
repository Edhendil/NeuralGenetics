package pl.krug.game;

import java.util.Collection;
import java.util.List;

/**
 * Contains all info about the given task.
 *
 * @author edhendil
 *
 */
public interface Game {

    public void addPlayer(NeuralPlayer player);

    public void addPlayers(Collection<NeuralPlayer> players);

    public void removePlayer(NeuralPlayer player);

    public void removePlayers(Collection<NeuralPlayer> players);

    /**
     * Do not assume it is linked the real players collection. Probably even
     * immutable.
     *
     * @return
     */
    public List<NeuralPlayer> getPlayersList();

    /**
     * From start to end. Can be controlled through number of rounds or other
     * condition.
     */
    public void runGame();
}
