package pl.krug.genetic.selector.util.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pl.krug.genetic.GeneticEvaluator;
import pl.krug.genetic.GeneticSelector;

/**
 * These selectors will be probably used in many cases but they do not implement anything.
 * This way I can have parameterless interface method and still be able to reuse much of the code.
 * @author Edhendil
 * @param <T> 
 */
public class GeneticSelectorBestOfTwo<T> {

	private GeneticEvaluator<? super T> _evaluator;
	
	public GeneticSelectorBestOfTwo(GeneticEvaluator<? super T> eval) {
		_evaluator = eval;
	}
	
	public List<List<T>> selectParents(List<T> population) {
		List<List<T>> result = new ArrayList<List<T>>();
		int numberOfGroups = population.size()/2;
		int first, second;
		for (int i = 0; i < numberOfGroups; i++) {
			List<T> parents = new ArrayList<T>(2);
			Random rand = new Random();
			//rand.setSeed(System.currentTimeMillis());
			// loop expanded
			// for the first parent
			first = rand.nextInt(population.size());
			second = rand.nextInt(population.size());
			if (_evaluator.evaluate(population.get(first)) > _evaluator.evaluate(population.get(second)))
				parents.add(population.get(first));
			else
				parents.add(population.get(second));
			// for the second parent
			first = rand.nextInt(population.size());
			second = rand.nextInt(population.size());
			if (_evaluator.evaluate(population.get(first)) > _evaluator.evaluate(population.get(second)))
				parents.add(population.get(first));
			else
				parents.add(population.get(second));
			result.add(parents);
		}
		return result;
	}

}
