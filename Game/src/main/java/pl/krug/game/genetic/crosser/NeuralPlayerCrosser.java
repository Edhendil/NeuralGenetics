package pl.krug.game.genetic.crosser;

import java.util.ArrayList;
import java.util.List;
import pl.krug.game.NeuralPlayer;
import pl.krug.genetic.GeneticCrosser;
import pl.krug.genetic.MutationRateProvider;
import pl.krug.neural.genetic.NetworkModelCrosser;
import pl.krug.neural.network.NeuralNetworkFactory;
import pl.krug.neural.network.model.NetworkModel;

public class NeuralPlayerCrosser implements GeneticCrosser<NeuralPlayer> {

    private MutationRateProvider<NeuralPlayer> mutationProvider;
    private NetworkModelCrosser _crosser = new NetworkModelCrosser();

    public NeuralPlayerCrosser(MutationRateProvider<NeuralPlayer> provider) {
        this.mutationProvider = provider;
    }

    public NeuralPlayerCrosser() {
    }

    @Override
    public List<NeuralPlayer> crossover(List<NeuralPlayer> parents) {
        List<NetworkModel> parentModels = new ArrayList<NetworkModel>(2);
        for (NeuralPlayer pl : parents) {
            parentModels.add(pl.getModel());
        }
        if (mutationProvider != null) {
            _crosser.setMutationRate(mutationProvider.getMutationRate(parents.get(0)));
        }
        List<NetworkModel> childModels = _crosser.crossover(parentModels);

        // here I must create a full Neural Player using the models I just created
        List<NeuralPlayer> result = new ArrayList<NeuralPlayer>();
        NeuralNetworkFactory factory = new NeuralNetworkFactory();

        for (NetworkModel model : childModels) {
            NeuralPlayer player = new NeuralPlayer();
            player.setModel(model);
            player.setNeuralNetwork(factory.createNetwork(model));
            result.add(player);
        }

        return result;
    }
}
