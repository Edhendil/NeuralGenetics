/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Does not return any results.
 *
 * @author Edhendil
 */
public class MultiThreadedTasksQueue implements GameTasksQueue {

    private ConcurrentLinkedQueue<Callable<Void>> queue = new ConcurrentLinkedQueue<Callable<Void>>();
    private int threadsNumber;
    private CyclicBarrier barrier;

    public MultiThreadedTasksQueue(int threadsNumber) {
        this.threadsNumber = threadsNumber;
        this.barrier = new CyclicBarrier(threadsNumber + 1);
    }

    @Override
    public void addTask(Callable<Void> task) {
        queue.add(task);
    }

    @Override
    public void addTasks(Collection<Callable<Void>> tasks) {
        for (Callable<Void> task : tasks) {
            addTask(task);
        }
    }

    @Override
    public void processTasksAndWait() {
        barrier.reset();
        for (int i = 0; i < threadsNumber; i++) {
            new Thread(new TaskProcessingThread()).start();
        }
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private class TaskProcessingThread implements Runnable {

        @Override
        public void run() {
            Callable<Void> task;
            while ((task = queue.poll()) != null) {
                try {
                    task.call();
                } catch (Exception ex) {
                    Logger.getLogger(MultiThreadedTasksQueue.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
