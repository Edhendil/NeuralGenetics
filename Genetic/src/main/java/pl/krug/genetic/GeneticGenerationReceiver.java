/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.genetic;

import java.util.List;
import java.util.Map;

/**
 * Can respond to creation of new generation in genetic engine.
 *
 * @author Edhendil
 */
public interface GeneticGenerationReceiver<T> {

    public void newGenerationCreated(Map<List<T>, List<T>> generationMap);
    
}
