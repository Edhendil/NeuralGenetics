/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.genetic.selector.util.impl;

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
    
    @Override
    public List<List<T>> selectParents() {
        throw new UnsupportedOperationException("Not supported yet.");
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
