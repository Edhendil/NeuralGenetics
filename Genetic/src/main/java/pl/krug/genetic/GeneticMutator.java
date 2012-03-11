package pl.krug.genetic;

/**
 * To perform mutation on individuals
 * 
 * @author edhendil
 * 
 * @param <T>
 */
public interface GeneticMutator<T> {

	/**
	 * 
	 * @param individual
	 *            - to be mutated
	 * @return the same object as in arguments but after mutation
	 */
	public T mutate(T individual);

}
