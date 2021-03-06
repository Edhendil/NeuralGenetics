/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pl.krug.genetic.GeneticEvaluator;
import pl.krug.genetic.GeneticGenerationReceiver;
import pl.krug.genetic.GeneticSelector;
import pl.krug.genetic.MutationRateProvider;
import pl.krug.genetic.selector.util.impl.GridGeneticSelector;
import pl.krug.utilities.GenericGrid;

/**
 * Grid in which players are placed, takes care of genetic stuff on game end.
 * Game mechanics don't have to take coordinates in this grid into account.
 * Doesn't know how to evaluate players. Can only select and respond to
 * generation creation.
 *
 * @author Edhendil
 */
public class GamePlayersGrid<T> implements GeneticSelector<T>, GeneticGenerationReceiver<T>, MutationRateProvider<T> {

    private GenericGrid<T> grid;
    private GridGeneticSelector<T> selector;
    private Map<T, IntegerCoordinates> coords;
    private GenericGrid<Double> mutationGrid;

    public GamePlayersGrid(int width, int height, GeneticEvaluator<T> evaluator) {
        grid = new GenericGrid<T>(width, height);
        selector = new GridGeneticSelector<T>(grid, evaluator);
        coords = new HashMap<T, IntegerCoordinates>();
        mutationGrid = new GenericGrid<Double>(width, height);
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                mutationGrid.setElement(i, j, Math.sqrt(Math.pow(2 * i / (double) grid.getWidth(), 2) + Math.pow(2 * j / (double) grid.getHeight(), 2)));
            }
        }
    }

    public void addPlayer(T player, int width, int height) {
        grid.setElement(width, height, player);
        IntegerCoordinates coor = new IntegerCoordinates(width, height);
        coords.put(player, coor);
    }

    public boolean addPlayer(T player) {
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                if (grid.getElement(i, j) == null) {
                    addPlayer(player, i, j);
                    return true;
                }
            }
        }
        return false;
    }

    public void removePlayer(T player) {
        IntegerCoordinates coor = coords.get(player);
        if (coor != null) {
            removePlayer(coor.getX(), coor.getY());
            coords.remove(player);
        }
    }

    public void removePlayer(int width, int height) {
        T player = grid.getElement(width, height);
        grid.setElement(width, height, null);
        coords.remove(player);
    }

    public T getPlayer(int width, int height) {
        return grid.getElement(width, height);
    }

    public void clear() {
        grid.clear();
        coords.clear();
    }

    public int getWidth() {
        return grid.getWidth();
    }

    public int getHeight() {
        return grid.getHeight();
    }

    @Override
    public List<List<T>> selectParents() {
        return selector.selectParents();
    }

    /**
     * Inherited from GenerationReceiver It works only because GridSelector
     * always puts central parent as first one
     *
     * @param generationMap
     */
    @Override
    public void newGenerationCreated(Map<List<T>, List<T>> generationMap) {
        for (List<T> parentSet : generationMap.keySet()) {
            IntegerCoordinates playerCoords = coords.get(parentSet.get(0));
            removePlayer(playerCoords.getX(), playerCoords.getY());
            addPlayer(generationMap.get(parentSet).get(0), playerCoords.getX(), playerCoords.getY());
        }
    }

    public List<T> getPlayersList() {
        return new ArrayList<T>(coords.keySet());
    }

    @Override
    public double getMutationRate(T player) {
        IntegerCoordinates coor = coords.get(player);
        return mutationGrid.getElement(coor.getX(), coor.getY());
    }

    private class IntegerCoordinates {

        private int x;
        private int y;

        public IntegerCoordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
