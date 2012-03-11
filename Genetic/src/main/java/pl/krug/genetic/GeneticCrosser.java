package pl.krug.genetic;

import java.util.List;

/**
 * Performs crossover
 * Creates completely new children objects ready to be used without any additional tweaking
 * 
 * @author edhendil
 *
 * @param <T>
 * @see GeneticMutator
 */
public interface GeneticCrosser<T> {
	
	/**
	 * Takes an arbitrary number of parents and produces an arbitrary number of children 
	 * Performs mutation as well if necessary
	 * @param parents
	 * @return
	 */
	public List<T> crossover(List<T> parents);

}
