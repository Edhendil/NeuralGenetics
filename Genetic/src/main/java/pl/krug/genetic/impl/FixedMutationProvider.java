/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.krug.genetic.impl;

import pl.krug.genetic.MutationRateProvider;

/**
 *
 * @author Edhendil
 */
public class FixedMutationProvider<T> implements MutationRateProvider<T> {

    private double mutationRate;

    public FixedMutationProvider(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public double getMutationRate(T player) {
        return this.mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
}
