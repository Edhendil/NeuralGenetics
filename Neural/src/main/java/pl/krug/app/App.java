package pl.krug.app;

import java.util.ArrayList;
import java.util.List;

import pl.krug.neural.dao.NetworkModelDAO;
import pl.krug.neural.network.model.NetworkModel;

/**
 * Main class
 * @author edhendil
 *
 */
public class App {
	
	private List<NetworkModel> _networkModels;
	private NetworkModelDAO _dao = new NetworkModelDAO();
	// fully loaded networks, better keep watch on them
	// if too many oom gonna appear
	private List<NetworkModel> _population = new ArrayList<NetworkModel>();
	
	public App() {
		_networkModels = _dao.getNetworkList();
	}
	
	public List<NetworkModel> getNetworkModels() {
		return _networkModels;
	}

	public void setNetworkModels(List<NetworkModel> networkModels) {
		_networkModels = networkModels;
	}
	
//	public TableModel get
	

}
