/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * Stores tasks.
 * @author Edhendil
 */
public interface GameTasksQueue {
    
    public void addTask(Callable<Void> task);
    
    public void addTasks(Collection<Callable<Void>> tasks);
    
    public void processTasksAndWait();
    
}
