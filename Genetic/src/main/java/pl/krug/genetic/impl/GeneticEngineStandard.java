package pl.krug.genetic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
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
    private List<T> _newPopulation;
    private final GeneticCrosser<T> _crosser;
    private final GeneticSelector<T> _selector;
    // multithreading
    private int _threadNumber;
    private Queue<List<T>> _tasks;
    private Queue<List<T>> _tasksResults;
    private CyclicBarrier _barrier;

    public GeneticEngineStandard(GeneticSelector<T> select,
            GeneticCrosser<T> cross) {
        _crosser = cross;
        _selector = select;
        _threadNumber = Runtime.getRuntime().availableProcessors();
        _barrier = new CyclicBarrier(_threadNumber + 1);
    }

    @Override
    public List<T> createNextGeneration() {
        // new population
        _newPopulation = new ArrayList<T>();
        // for every selected parent group
        _tasks = new ConcurrentLinkedQueue<List<T>>(
                _selector.selectParents());
        _tasksResults = new ConcurrentLinkedQueue<List<T>>();

        // start crossing threads
        for (int i = 0; i < _threadNumber; i++) {
            new Thread(new CrossingProcessingThread()).start();
        }

        // wait for them to finish the job
        try {
            _barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        // process the result
        for (List<T> children : _tasksResults) {
            _newPopulation.addAll(children);
        }

        // replace old population with a new one
        return _newPopulation;
    }

    private class CrossingProcessingThread implements Runnable {

        @Override
        public void run() {
            List<T> parents;
            while ((parents = _tasks.poll()) != null) {
                _tasksResults.add(_crosser.crossover(parents));
            }
            try {
                _barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
