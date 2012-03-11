package pl.krug.genetic;

import java.util.List;

/**
 * Responsible for creating the next generation
 * 
 * @author edhendil
 * 
 */
public interface GeneticEngine<T> {
	
	public List<T> createNextGeneration();
	
}
