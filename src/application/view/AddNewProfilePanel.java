package application.view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import application.controller.MenuController;

public class AddNewProfilePanel extends JPanel{
	private static final long serialVersionUID = -4920719012159676183L;
	
	private JTextField newProfileName;
	private JButton addButton;
	
	public AddNewProfilePanel() {
		newProfileName = new JTextField("");
		addButton = new JButton(ButtonText.ADD_PROFILE__BUTTON_TEXT);
		
		addButton.setBackground(Colors.BUTTON_DEFAULT_COLOR);
		this.setBackground(Colors.WORLD_GROUND);
		
		newProfileName.setBackground(Colors.MAP_COLOR);
		
		newProfileName.setPreferredSize(new Dimension(200, 30));
		this.add(newProfileName);
		this.add(addButton);
		this.setMaximumSize(new Dimension(800, 100));
	}
	
	public JTextField getNewProfileName() {
		return newProfileName;
	}
	
	public JButton getAddButton() {
		return addButton;
	}
	
	public void addController(MenuController controller) {
		
		addButton.setActionCommand("add profile");
		addButton.addActionListener(controller);
		
		newProfileName.setActionCommand("add profile field");
		newProfileName.addActionListener(controller);
	}

}