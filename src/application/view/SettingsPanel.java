package application.view;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import application.controller.MenuController;
import application.model.Game;
import application.model.GameSetting;
//pannello utlilizzato per mostrare le impostazioni di gioco
public class SettingsPanel extends JPanel{

	private static final long serialVersionUID = -1092071451575285378L;
	
	private ArrayList<SettingView> settings;
	private JButton backToMenu;
	
	//crea il pannello contenente tutte le impostazioni
	public SettingsPanel() {
		super();
		this.setBackground(Colors.WORLD_GROUND);
		backToMenu= new JButton(ButtonText.BACK_TO_MENU_BUTTON_TEXT);
		backToMenu.setBackground(Colors.BUTTON_DEFAULT_COLOR);
		settings =  new ArrayList<SettingView>();
		GameSetting setting;
		
		for(int i = 0; i < Game.getInstance().getSettings().size(); ++i) {
			setting = Game.getInstance().getSettings().get(i);
			settings.add(new SettingView(setting.getDescription(), setting.getAssociatedKey()));
		 }
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		backToMenu.setMaximumSize(new Dimension(180, 40));
		fillPanel();
		this.setFocusable(true);
	}
	
	public void fillPanel() {
		this.add(Box.createRigidArea(new Dimension(0, 70)));
		for(int i = 0; i < settings.size(); ++i) {
			this.add(settings.get(i));
			this.add(Box.createRigidArea(new Dimension(0, 10)));
			settings.get(i).setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		this.add(Box.createRigidArea(new Dimension(0, 60)));
		this.add(backToMenu);
		backToMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	public ArrayList<SettingView> getSettings() {
		return settings;
	}
	public void setController(MenuController controller) {
		backToMenu.setActionCommand("back to menu");
		backToMenu.addActionListener(controller);
		
	}
	
	public JButton getBackToMenu() {
		return backToMenu;
	}
}
