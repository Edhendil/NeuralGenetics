package pl.krug.genetic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import pl.krug.genetic.GeneticCrosser;
import pl.krug.genetic.GeneticEngine;
import pl.krug.genetic.GeneticSelector;

/**
 * Makes use of a crosser and selector. Any member can mate with any other in
 * the population. There are no restrictions.
 *
 * @author edhendil
 *
 * @param <T>
 */
public class GeneticEngineStandard<T> implements GeneticEngine<T> {

    // helper
    private final GeneticCrosser<T> _crosser;
    private final GeneticSelector<T> _selector;
    // multithreading
    private int _threadNumber;

    public GeneticEngineStandard(GeneticSelector<T> select,
            GeneticCrosser<T> cross) {
        _crosser = cross;
        _selector = select;
        _threadNumber = Runtime.getRuntime().availableProcessors();
    }

    @Override
    public Map<List<T>, List<T>> createNextGeneration() {
        // new population
        Map<List<T>, List<T>> newPopulation = new HashMap<List<T>, List<T>>();

        List<Future<List<T>>> results = new ArrayList<Future<List<T>>>();

        List<List<T>> parents = _selector.selectParents();

        ExecutorService exec = Executors.newFixedThreadPool(_threadNumber);

        try {
            for (List<T> parentSet : parents) {
                results.add(exec.submit(new CrossingProcessingCallable(parentSet)));
            }
            exec.shutdown();
            exec.awaitTermination(15, TimeUnit.MINUTES);
            for (int i = 0; i < parents.size(); i++) {
                newPopulation.put(parents.get(i), results.get(i).get());
            }
        } catch (InterruptedException ex) {
        } catch (ExecutionException ex) {
        }

        // replace old population with a new one
        return newPopulation;
    }

    private class CrossingProcessingCallable implements Callable<List<T>> {

        private List<T> parents;

        public CrossingProcessingCallable(List<T> parents) {
            this.parents = parents;
        }

        @Override
        public List<T> call() throws Exception {
            List<T> result = _crosser.crossover(parents);
            parents = null;
            return result;
        }
    }
}
