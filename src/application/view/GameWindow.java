package application.view;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import application.controller.GameWindowController;
import application.controller.GameWindowResizeController;
import application.controller.MenuController;

//finestra di gioco, la quale contiene tutti i pannelli necessari a rappresentare il gioco
public class GameWindow extends JFrame{

	private static final long serialVersionUID = -6042846735628806949L;
	private JPanel mainPanel;
	private GamePanel gamePanel;
	private MainMenu mainMenu;
	private ProfilesPanel profilesPanel;
	private SettingsPanel settingsPanel;
	private WindowState windowState;
	private CardLayout cL;
	
	public GameWindow() {
		super();
		windowState = new WindowState();
		mainPanel = new JPanel();
		mainPanel.setFocusable(true);
		gamePanel = new GamePanel();
		mainMenu = new MainMenu();
		profilesPanel = new ProfilesPanel();
		settingsPanel = new SettingsPanel();
		cL = new CardLayout();
		
		mainPanel.setLayout(cL);
		mainPanel.add("game panel", gamePanel);
		mainPanel.add("main menu", mainMenu);
		mainPanel.add("profiles panel", profilesPanel);
		mainPanel.add("settings panel", settingsPanel);
		getContentPane().add(mainPanel);
		
		cL.show(mainPanel, "main menu");
		
		this.setUndecorated(true);
		this.pack();
		this.setVisible(true);
		this.setFocusable(true);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	public void setMenuController(MenuController controller) {
		
		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW) .put(KeyStroke.getKeyStroke("F11"), "full screen");
		this.getRootPane().getActionMap().put("full screen", new AbstractAction() {
			private static final long serialVersionUID = 2338409687208302554L;

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Calling abstract action");
					gamePanel.updateWindowState();
					windowState.switchWindowState();
					//quando nel menù avviene un cambio di risoluzione della schermata di gioco bisogna anche cambiare le dimensioni 
					//al pannello di gioco e alla camera in modo che iniziando a giocare la scelta fatta in precedenza venga mantenuta
					gamePanel.getGameCamera().setWindowDimension(windowState.getWindowWidth(), windowState.getWindowHeight());
					adaptWindowToPanels();
				}
		});
		
		mainMenu.setController(controller);
		profilesPanel.setController(controller);
		settingsPanel.setController(controller);
	}
	
	public void setWindowController(GameWindowController controller) {
		this.addWindowListener(controller);
	}
	
	public void setWindowResizeController(GameWindowResizeController windowResizeController) {
		
		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW) .put(KeyStroke.getKeyStroke("F11"), "full screen");
		this.getRootPane().getActionMap().put("full screen", windowResizeController);
	}
	
	public void switchToPanel(String name) {
		if(name.equals("game panel")){
			System.out.println("Switching to game panel");
			gamePanel.setFocusable(true);
			cL.show(mainPanel, name);
			
			gamePanel.requestFocusInWindow();
		}
		else if(name.equals("main menu")) {
			cL.show(mainPanel, name);
		}
		else if(name.equals("profiles panel")) {
			profilesPanel.setFocusable(true);
			cL.show(mainPanel, name);
		}
		else if(name.equals("settings panel")) {
			cL.show(mainPanel, name);
		}

	}
	
	public GamePanel getGamePanel() {
		return gamePanel;
	}
	
	public MainMenu getMainMenu(){
		return mainMenu;
	}
	
	public ProfilesPanel getProfilesPanel() {
		return profilesPanel;
	}
	
	public SettingsPanel getSettingsPanel() {
		return settingsPanel;
	}
	
	public WindowState getWindowState() {
		return windowState;
	}
	
	public void adaptWindowToPanels() {
		Dimension dim = new Dimension(windowState.getWindowWidth(), windowState.getWindowHeight());
		mainPanel.setPreferredSize(dim);
		gamePanel.setPreferredSize(dim);
		mainMenu.setPreferredSize(dim);
		profilesPanel.setPreferredSize(dim);
		this.setPreferredSize(dim);
		this.pack();
	}
}
