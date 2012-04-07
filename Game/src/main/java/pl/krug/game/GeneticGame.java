/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.game;

import pl.krug.genetic.GeneticEvaluator;
import pl.krug.genetic.GeneticGenerationReceiver;
import pl.krug.genetic.GeneticSelector;

/**
 * Convenience interface
 * @author Edhendil
 */
public interface GeneticGame<T> extends Game, GeneticSelector<T>, GeneticEvaluator<T>, GeneticGenerationReceiver<T> {
}
