package views;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameView extends JFrame{

	public static JFrame frame;
	public static JPanel fPanel;
	
	public GameView()
	{
		frame = GameStart.createFrame();
		frame.setLayout(new BorderLayout());
		
		fPanel = new JPanel();
		fPanel.setBackground(Color.darkGray);
		fPanel.setLayout(new BorderLayout());
		
		frame.add(fPanel);
		
		new GameStart();
		
		frame.setVisible(true);
		GameView.frame.validate();
	}
	
	public static void main(String[] args)
	{
		new GameView();
	}
}
