package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import engine.Game;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;

public class SelectHero implements ActionListener{

	JPanel mPanel, wPanel, tPanel, heroPanel, heroInfo;
	JTextArea hInfo;
	JButton[] buttons;
	JButton back, start;
	Hero selectedHero = null;
	
	public SelectHero()
	{
		mPanel = new JPanel();
		mPanel.setBackground(Color.DARK_GRAY);
		mPanel.setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Select Starting Hero", JLabel.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		title.setForeground(Color.white);;
		title.setFont(new Font("DialogInput", Font.ITALIC,40));
		
		tPanel = new JPanel();
		tPanel.setBackground(new Color(50,50,100));
		tPanel.setLayout(new BorderLayout());
		
		back = GameStart.createButton("");
		JLabel goBack = new JLabel("Go Back");
		goBack.setFont(new Font("SANS", Font.BOLD,20));
		back.add(goBack);
		back.setPreferredSize(new Dimension(90,30));
		back.addActionListener(this);
		
		wPanel = new JPanel();
		wPanel.setBackground(new Color(50,50,100));
		wPanel.setLayout(new BorderLayout());
		
		wPanel.add(back, BorderLayout.NORTH);
		
		tPanel.add(wPanel, BorderLayout.WEST);
		
		tPanel.add(title);
		
		mPanel.add(tPanel, BorderLayout.NORTH);
		
		try {
			Game.loadHeroes("Heroes.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		heroPanel = new JPanel();
		heroPanel.setBackground(Color.DARK_GRAY);
		heroPanel.setLayout(new GridLayout(0,1,0,10));
		heroPanel.setBorder(BorderFactory.createEmptyBorder(10, 200, 5, 200));
		
		buttons = new JButton[Game.availableHeroes.size()];
		
		for(Hero hero : Game.availableHeroes)
		{
			String type = (hero instanceof Fighter? "Fighter": hero instanceof Medic? "Medic": "Explorer");
			JButton button = GameStart.createButton("");
			JLabel heroName = new JLabel(hero.getName() + " - " + type,JLabel.CENTER);
			heroName.setForeground(new Color(50,50,80));
			heroName.setFont(new Font("SANS_SERIF", Font.BOLD, 30));
			button.add(heroName);
			buttons[Game.availableHeroes.indexOf(hero)] = button;
			buttons[Game.availableHeroes.indexOf(hero)].addActionListener(this);
			heroPanel.add(button);
		}
		
		heroInfo = new JPanel();
		heroInfo.setBackground(new Color(30,30,30));
		heroInfo.setPreferredSize(new Dimension(200, 0));
		heroInfo.setLayout(new BorderLayout());
		heroInfo.setBorder(BorderFactory.createLoweredBevelBorder());
		
		start = GameStart.createButton("Start Game");
		start.setEnabled(false);
		start.addActionListener(this);
		
		heroInfo.setVisible(false);
		
		heroInfo.add(start, BorderLayout.SOUTH);
		
		mPanel.add(heroPanel);
		mPanel.add(heroInfo, BorderLayout.WEST);
		
		GameView.fPanel.add(mPanel);
		GameView.frame.setVisible(true);
		GameView.frame.validate();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(back))
		{
			GameView.fPanel.remove(mPanel);
			GameView.frame.repaint();
			new GameStart();
			GameView.frame.setVisible(true);
		}
		else if(e.getSource().equals(start))
		{
			GameView.fPanel.remove(mPanel);
			GameView.frame.repaint();
			new Play(selectedHero);
			GameView.frame.setVisible(true);
		}else
		{
			boolean flag = false;
			for(int i = 0;i < buttons.length && !flag;i++)
			{
				if(e.getSource().equals(buttons[i]))
				{
					selectedHero = Game.availableHeroes.get(i);
					flag = !flag;
				}
			}
			String type = (selectedHero instanceof Fighter? "Fighter": selectedHero instanceof Medic? "Medic": "Explorer");
			String heroData = "Name: " + selectedHero.getName() + "\nType: " + type + "\nHp: " + selectedHero.getMaxHp() +
					"\nMax Actions: " + selectedHero.getMaxActions() + "\nAttack Damage: " + selectedHero.getAttackDmg();
			
			hInfo = new JTextArea(heroData);
			hInfo.setFont(new Font("SANS_SERIF", Font.BOLD, 16));
			hInfo.setEditable(false);
			hInfo.setBackground(new Color(30,30,30));
			hInfo.setForeground(new Color(200,200,200));
			
			heroInfo.remove(heroInfo);
			heroInfo.add(hInfo, BorderLayout.NORTH);
			heroInfo.setVisible(true);
			start.setEnabled(true);
			heroInfo.validate();
		}
	}
}









