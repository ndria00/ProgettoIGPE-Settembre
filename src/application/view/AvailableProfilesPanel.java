package application.view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import application.controller.MenuController;
//gestisce i profili attivi, cioè quelli per cui esistono dei salvataggi nel DB
public class AvailableProfilesPanel extends JPanel{

	private static final long serialVersionUID = -3038732863221089895L;
	JScrollPane scrollPane;
	private JList<String> availableProfiles;
	private DefaultListModel<String> listModel;
	private JButton startGameButton;


	public AvailableProfilesPanel() {

		listModel = new DefaultListModel<String>();
		availableProfiles = new JList<String>(listModel);
		availableProfiles.setBackground(Colors.MAP_COLOR);

		availableProfiles.setFixedCellHeight(50);
		scrollPane = new JScrollPane(availableProfiles);
		scrollPane.setMaximumSize(new Dimension(350, 500));
		scrollPane.setPreferredSize(new Dimension(300, 350));
		
		startGameButton = new JButton(ButtonText.START_GAME_BUTTON_TEXT);
		startGameButton.setMaximumSize(new Dimension(200, 70));
		startGameButton.setPreferredSize(new Dimension(200, 50));
		startGameButton.setBackground(Colors.BUTTON_DEFAULT_COLOR);

		
		this.setBackground(Colors.WORLD_GROUND);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		
		
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		
		this.add(scrollPane);
		this.add(Box.createVerticalGlue());
		this.add(startGameButton);
	}
	
	public JList<String> getAvailableProfiles() {
		return availableProfiles;
	}
	public DefaultListModel<String> getListModel() {
		return listModel;
	}
	
	//aggiorna la lista dei profili (da effettuare quando si aggiunge un profilo)
	public void updateAvailableProfiles(ArrayList<String> profiles) {
		listModel.clear();

		for(int i = 0; i < profiles.size(); ++i) {
			listModel.addElement(profiles.get(i));
		}
	}
	
	public void addController (MenuController controller) {
		startGameButton.setActionCommand("start game");
		startGameButton.addActionListener(controller);

	}
	
	public JButton getStartGameButton() {
		return startGameButton;
	}
}