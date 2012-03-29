package pl.krug.genetic;

import java.util.List;

/**
 * To select parent groups from population Probably will use an evaluator but it
 * is not mandatory Can as well be selected randomly Does not change any aspect
 * of population! I don't really care where does this population come from.
 * That's why population parameter is removed.
 *
 * General contract: sets of children will be returned by the engine in the same
 * order as sets of parents were.
 *
 * @author edhendil
 *
 * @param <T>
 */
public interface GeneticSelector<T> {

    public List<List<T>> selectParents();
}
