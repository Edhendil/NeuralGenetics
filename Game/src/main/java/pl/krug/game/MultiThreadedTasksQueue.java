/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Does not return any results. Stores tasks and process them on request.
 *
 * @author Edhendil
 */
public class MultiThreadedTasksQueue implements GameTasksQueue {

    private List<Callable<Void>> list = new ArrayList<Callable<Void>>();
    private int threadsNumber;
    private ExecutorService exec;

    public MultiThreadedTasksQueue() {
        this.threadsNumber = Runtime.getRuntime().availableProcessors();
    }

    public MultiThreadedTasksQueue(int threadsNumber) {
        this.threadsNumber = threadsNumber;
    }

    @Override
    public void addTask(Callable<Void> task) {
        list.add(task);
    }

    @Override
    public void addTasks(Collection<Callable<Void>> tasks) {
        for (Callable<Void> task : tasks) {
            addTask(task);
        }
    }

    @Override
    public void processTasksAndWait() {
        exec = Executors.newFixedThreadPool(threadsNumber);
        try {
            exec.invokeAll(list);
            exec.shutdown();
            exec.awaitTermination(15, TimeUnit.MINUTES);
            list.clear();
        } catch (InterruptedException ex) {
        }
    }
}
