/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.genetic.selector.util.impl;

import java.util.ArrayList;
import java.util.List;
import pl.krug.genetic.GeneticEvaluator;
import pl.krug.genetic.GeneticSelector;
import pl.krug.utilities.GenericGrid;

/**
 *
 * @author Edhendil
 */
public class GridGeneticSelector<T> implements GeneticSelector<T> {

    private GenericGrid<T> grid;
    private GeneticEvaluator evaluator;

    public GridGeneticSelector(GenericGrid<T> grid, GeneticEvaluator<T> evaluator) {
        this.grid = grid;
        this.evaluator = evaluator;
    }

    /**
     * Central parent is always placed first in parent sublists
     * @return 
     */
    @Override
    public List<List<T>> selectParents() {
        List<List<T>> parentList = new ArrayList<List<T>>();
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                parentList.add(getParents(i, j));
            }
        }
        return parentList;
    }

    private List<T> getParents(int x, int y) {
        List<T> parents = new ArrayList<T>();

        T centerParent = grid.getElement(x, y);

        List<T> candidates = new ArrayList<T>();

        candidates.add(grid.getElement(getNormalizedDimension(x - 1, grid.getWidth()), y));
        candidates.add(grid.getElement(getNormalizedDimension(x + 1, grid.getWidth()), y));
        candidates.add(grid.getElement(x, getNormalizedDimension(y + 1, grid.getHeight())));
        candidates.add(grid.getElement(x, getNormalizedDimension(y - 1, grid.getHeight())));

        T secondParent = candidates.get(0);
        double maxValue = evaluator.evaluate(candidates.get(0));

        // skip the first one, no need to check it again
        // find the best parent
        for (int i = 1; i < 4; i++) {
            double eval;
            if ((eval = evaluator.evaluate(
                    candidates.get(i))) > maxValue) {
                secondParent = candidates.get(i);
                maxValue = eval;
            }
        }

        parents.add(centerParent);
        parents.add(secondParent);

        return parents;
    }

    /**
     * It works only for value in range -max &lt; value &lt; 2*max
     * @param value
     * @param max
     * @return
     */
    private int getNormalizedDimension(int value, int max) {
        if (value >= max) {
            return value - max;
        }
        if (value < 0) {
            return max + value - 1;
        }
        return value;
    }

    public GeneticEvaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(GeneticEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    public GenericGrid<T> getGrid() {
        return grid;
    }

    public void setGrid(GenericGrid<T> grid) {
        this.grid = grid;
    }
}
