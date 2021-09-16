package application.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import application.controller.MenuController;
//pannello che visualizza i profili del gioco 
public class ProfilesPanel extends JPanel{
	private static final long serialVersionUID = 137467373963602147L;
	
	private AvailableProfilesPanel availableProfilesPanel;
	private AddNewProfilePanel addNewProfilePanel;
	private JButton backToMenu;
	
	public ProfilesPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		availableProfilesPanel = new AvailableProfilesPanel();
		addNewProfilePanel = new AddNewProfilePanel();
		backToMenu = new JButton(ButtonText.BACK_TO_MENU_BUTTON_TEXT);
		backToMenu.setMaximumSize(new Dimension(180, 80));
		backToMenu.setPreferredSize(new Dimension(180, 50));
		backToMenu.setBackground(Colors.BUTTON_DEFAULT_COLOR);
		
		backToMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
		availableProfilesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		addNewProfilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.add(availableProfilesPanel);
		this.add(Box.createVerticalGlue());
		this.add(addNewProfilePanel);
		this.add(Box.createVerticalGlue());
		this.add(backToMenu);
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		
		this.setBackground(Colors.WORLD_GROUND);
		this.setFocusable(true);
	}
	

	public void setController(MenuController controller) {
		availableProfilesPanel.addController(controller);
		addNewProfilePanel.addController(controller);
		
		backToMenu.setActionCommand("back to menu");
		backToMenu.addActionListener(controller);
	}
	
	public AvailableProfilesPanel getAvailableProfilesPanel() {
		return availableProfilesPanel;
	}
	
	public AddNewProfilePanel getAddNewProfilePanel() {
		return addNewProfilePanel;
	}
	
	public JButton getBackToMenu() {
		return backToMenu;
	}
}