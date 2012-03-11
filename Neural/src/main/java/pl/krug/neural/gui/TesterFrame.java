package pl.krug.neural.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;

import pl.krug.app.App;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TesterFrame extends JFrame {
	
	private App _app = new App();
	
	public TesterFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane mainTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(mainTabbedPane);
		
		JPanel startPanel = new JPanel();
		mainTabbedPane.addTab("Start", null, startPanel, null);
		
		JPanel loadNetPanel = new JPanel();
		mainTabbedPane.addTab("Load", null, loadNetPanel, null);
		loadNetPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel baseNetworks = new JPanel();
		loadNetPanel.add(baseNetworks, BorderLayout.WEST);
		baseNetworks.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Available networks");
		baseNetworks.add(lblNewLabel, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		baseNetworks.add(scrollPane, BorderLayout.CENTER);
		
		JList list = new JList(_app.getNetworkModels().toArray());
		scrollPane.setViewportView(list);
		
		JPanel buttonPanel = new JPanel();
		baseNetworks.add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnAddToPopulation = new JButton("Add to population");
		btnAddToPopulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		buttonPanel.add(btnAddToPopulation);
		
		JPanel loadedNets = new JPanel();
		loadNetPanel.add(loadedNets, BorderLayout.CENTER);
		loadedNets.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel_1 = new JLabel("Population");
		loadedNets.add(lblNewLabel_1, BorderLayout.NORTH);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6240589733008103156L;
}
