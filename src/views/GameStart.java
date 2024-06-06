package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameStart implements ActionListener{

	JPanel panel, cPanel, wPanel, ePanel, sPanel;
	JButton start, exit;
	
	public GameStart()
	{
		panel = new JPanel();
		panel.setBackground(new Color(50,50,100));
		panel.setLayout(new BorderLayout());
		
		cPanel = new JPanel();
		cPanel.setBackground(Color.DARK_GRAY);
		cPanel.setLayout(new GridLayout(2,1,0,50));
		cPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		
		wPanel = new JPanel();
		wPanel.setBackground(Color.DARK_GRAY);
		wPanel.setPreferredSize(new Dimension(200,0));
		sPanel = new JPanel();
		sPanel.setBackground(Color.DARK_GRAY);
		sPanel.setPreferredSize(new Dimension(0,130));
		ePanel = new JPanel();
		ePanel.setBackground(Color.DARK_GRAY);
		ePanel.setPreferredSize(new Dimension(200,0));
		
		JLabel title = new JLabel("The Last Of Us - Legacy", JLabel.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		title.setForeground(Color.white);
		title.setFont(new Font("DialogInput", Font.ITALIC,40));
		
		
		start= createButton("Start Game");
		start.addActionListener(this);
		exit = createButton("Exit");
		exit.addActionListener(this);
		
		cPanel.add(start);
		cPanel.add(exit);
		
		panel.add(wPanel, BorderLayout.WEST);
		panel.add(ePanel, BorderLayout.EAST);
		panel.add(sPanel, BorderLayout.SOUTH);
		panel.add(title, BorderLayout.NORTH);
		panel.add(cPanel, BorderLayout.CENTER);
		
		GameView.fPanel.add(panel, BorderLayout.CENTER);
		GameView.frame.validate();
	}
	
	public static JButton createButton(String text)
	{
		JButton button = new JButton();
		button.setLayout(new BorderLayout());
		button.setBackground(Color.gray);
		button.setFocusable(false);
		button.setBorder(BorderFactory.createRaisedBevelBorder());
		
		JLabel label = new JLabel(text, JLabel.CENTER);
		label.setFont(new Font("SANS_SERIF", Font.BOLD, 30));
		label.setForeground(new Color(100,50,50));
		button.add(label);
		
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(start))
		{
			GameView.fPanel.remove(panel);
			GameView.frame.repaint();
			new SelectHero();
		}
		else if(e.getSource().equals(exit))
			GameView.frame.dispose();
	}
	
	public static JFrame createFrame()
	{
		JFrame frame = new JFrame();
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(250,50,1000,700);;
		frame.setTitle("The Last Of Us - Legacy");
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		return frame;
	}
}
