/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.genetic;

/**
 *
 * @author Edhendil
 */
public interface MutationRateProvider<T> {
    
    public double getMutationRate(T player);
    
}
