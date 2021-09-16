package application.view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//pannello per visualizzare una impostazione di gioco
public class SettingView extends JPanel{
	private static final long serialVersionUID = 6284604588264823571L;
	
	private JLabel label;
	private JTextField keyTextField;
	private Font labelFont;
	private Font keyTextFont;
	
	public SettingView(String labelText, String currentKeyText) {
		super();
		this.setBackground(Colors.WORLD_GROUND);
		this.setMaximumSize(new Dimension(400, 50));
		label = new JLabel();
		keyTextField = new JTextField();
		labelFont = new Font("Arial", Font.BOLD, 17);
		keyTextFont = new Font("Arial", Font.ITALIC, 20);
		label.setMaximumSize(new Dimension(150, 50));
		label.setFont(labelFont);
		keyTextField.setMaximumSize(new Dimension(130, 50));
		keyTextField.setPreferredSize(new Dimension(130, 50));
		keyTextField.setFont(keyTextFont);
		
		label.setText(labelText);
		keyTextField.setText(currentKeyText);
		keyTextField.setHorizontalAlignment(JTextField.CENTER);
		keyTextField.setBackground(Colors.MAP_COLOR);
		keyTextField.setEditable(false);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(label);
		this.add(Box.createHorizontalGlue());
		this.add(keyTextField);
	}
	
	
	public JTextField getKey() {
		return keyTextField;
	}
	
	public JLabel getLabel() {
		return label;
	}
}