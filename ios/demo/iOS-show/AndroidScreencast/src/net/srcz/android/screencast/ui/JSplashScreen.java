package net.srcz.android.screencast.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

public class JSplashScreen extends JWindow {
	JLabel label = new JLabel("Loading...", 
			new ImageIcon(JFrameMain.class.getResource("icon.png")),
			(int)JLabel.CENTER_ALIGNMENT);

	public JSplashScreen(String text) {
		initialize();
		setText(text);
	}
	
	public void setText(String text) {
		label.setText(text);
		pack();
		setLocationRelativeTo(null);
	}
	
	private void initialize() {
		setLayout(new BorderLayout());
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		//createLineBorder(Color.BLACK));
		add(label,BorderLayout.CENTER);
	}
	
}
