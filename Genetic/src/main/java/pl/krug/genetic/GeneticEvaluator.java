package pl.krug.genetic;

/**
 * Generic interface for evaluating members of population for genetic purposes
 * Task specific
 * @author edhendil
 * 
 * @param <T>
 *
 */
public interface GeneticEvaluator<T> {

	public double evaluate(T individual);
	
}
