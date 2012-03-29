package pl.krug.genetic;

import java.util.List;
import java.util.Map;

/**
 * Responsible for creating the next generation
 * 
 * @author edhendil
 * 
 */
public interface GeneticEngine<T> {
	
	public Map<List<T>,List<T>> createNextGeneration();
	
}
