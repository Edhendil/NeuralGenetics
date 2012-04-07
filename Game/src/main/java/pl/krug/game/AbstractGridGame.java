/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import pl.krug.genetic.MutationRateProvider;

/**
 * Takes care of all basic stuff. Subclasses just have to implement game logic.
 *
 * @author Edhendil
 */
public abstract class AbstractGridGame implements GeneticGame<NeuralPlayer>, MutationRateProvider<NeuralPlayer> {

    private GamePlayersGrid<NeuralPlayer> grid;
    private GameTasksQueue _tasksQueue = new MultiThreadedTasksQueue();

    public AbstractGridGame(int width, int heigth) {
        grid = new GamePlayersGrid<NeuralPlayer>(width, heigth, this);
    }

    protected GamePlayersGrid<NeuralPlayer> getGrid() {
        return grid;
    }

    @Override
    public void addPlayer(NeuralPlayer player) {
        grid.addPlayer(player);
        initializePlayerState(player);
    }

    @Override
    public void addPlayers(Collection<NeuralPlayer> players) {
        for (NeuralPlayer player : players) {
            addPlayer(player);
        }
    }

    @Override
    public void removePlayer(NeuralPlayer player) {
        grid.removePlayer(player);
        destroyPlayerState(player);
    }

    @Override
    public void removePlayers(Collection<NeuralPlayer> players) {
        for (NeuralPlayer player : players) {
            removePlayer(player);
        }
    }

    @Override
    public List<NeuralPlayer> getPlayersList() {
        return grid.getPlayersList();
    }

    @Override
    public List<List<NeuralPlayer>> selectParents() {
        return grid.selectParents();
    }

    @Override
    public void newGenerationCreated(Map<List<NeuralPlayer>, List<NeuralPlayer>> generationMap) {
        List<NeuralPlayer> oldPlayers = getPlayersList();
        for (NeuralPlayer player : oldPlayers) {
            destroyPlayerState(player);
        }
        grid.newGenerationCreated(generationMap);
        for (NeuralPlayer player : getPlayersList()) {
            initializePlayerState(player);
        }
    }

    protected GameTasksQueue getTasksQueue() {
        return _tasksQueue;
    }

    @Override
    public double getMutationRate(NeuralPlayer player) {
        return getGrid().getMutationRate(player);
    }

    protected abstract void initializePlayerState(NeuralPlayer player);

    protected abstract void destroyPlayerState(NeuralPlayer player);
}
