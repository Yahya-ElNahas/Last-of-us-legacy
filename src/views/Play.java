package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import engine.Game;
import exceptions.InvalidTargetException;
import exceptions.MovementException;
import exceptions.NoAvailableResourcesException;
import exceptions.NotEnoughActionsException;
import model.characters.Direction;
import model.characters.Explorer;
import model.characters.Fighter;
import model.characters.Hero;
import model.characters.Medic;
import model.collectibles.Vaccine;
import model.world.CharacterCell;
import model.world.CollectibleCell;
import model.world.TrapCell;

public class Play implements ActionListener, KeyEventDispatcher{
	
	static JPanel panel, map, bar, infoPanel, heroConsole, targetConsole, heroPanel, targetPanel, console, endTurnPanel, turnCount;
	static JButton[][] cell;
	JButton attackButton, moveButton, cureButton, useSpecialButton, endTurnButton, exitButton;
	Hero selectedHero;
	Actions selectedAction;
	static int turn;
	Game game;

	public Play(Hero hero)
	{
		game = new Game();
		selectedHero = null;
		selectedAction = null;
		turn = 0;
		Game.startGame(hero);
		selectedHero = hero;
		
		cell = new JButton[Game.map.length][Game.map[0].length];
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.DARK_GRAY);

		map = new JPanel();
		map.setLayout(new GridLayout(Game.map.length,Game.map[0].length,0,0));
		map.setBackground(new Color(50,60,50));
		
		updateMap();
		
		bar = new JPanel();
		bar.setBackground(new Color(50,50,50));
		bar.setLayout(new BorderLayout(10,10));
		bar.setPreferredSize(new Dimension(0, 150));
		bar.setBorder(BorderFactory.createRaisedBevelBorder());
		
		JPanel actionPanel = new JPanel();
		actionPanel.setBackground(new Color(50,50,50));
		actionPanel.setLayout(new GridLayout(2,2,10,10));
		actionPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		endTurnPanel = new JPanel();
		endTurnPanel.setBackground(new Color(50,50,50));
		endTurnPanel.setLayout(new BorderLayout());
		endTurnPanel.setPreferredSize(new Dimension(150,0));
		
		moveButton = createButton("Move");
		moveButton.addActionListener(this);
		
		attackButton = createButton("Attack");
		attackButton.addActionListener(this);
		
		cureButton = createButton("Cure");
		cureButton.addActionListener(this);
		
		useSpecialButton = createButton("Use Special Ability");
		useSpecialButton.addActionListener(this);
		
		endTurnButton = createButton("End Turn");
		endTurnButton.addActionListener(this);
		endTurnButton.setBackground(new Color(100,50,50));
		
		exitButton = GameStart.createButton("Exit");
		exitButton.addActionListener(this);

		console = new JPanel();
		console.setLayout(new BorderLayout());
		console.setBackground(Color.gray);
		console.setBorder(BorderFactory.createEtchedBorder());
		
		heroConsole = new JPanel();
		heroConsole.setLayout(new BorderLayout());
		heroConsole.setBackground(new Color(50,50,50));
		heroConsole.setBorder(BorderFactory.createLoweredBevelBorder());
		heroConsole.setVisible(false);
		
		targetConsole = new JPanel();
		targetConsole.setLayout(new BorderLayout());
		targetConsole.setBackground(new Color(50,50,50));
		targetConsole.setBorder(BorderFactory.createLoweredBevelBorder());
		targetConsole.setVisible(false);
		
		heroPanel = new JPanel();
		heroPanel.setLayout(new BorderLayout());
		heroPanel.setBackground(new Color(50,50,50));
		
		targetPanel = new JPanel();
		targetPanel.setLayout(new BorderLayout());
		targetPanel.setBackground(new Color(50,50,50));
		targetPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
		
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoPanel.setBackground(new Color(50,50,50));
		infoPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		infoPanel.setPreferredSize(new Dimension(250, 0));
		
		heroPanel.add(heroConsole, BorderLayout.CENTER);
		targetPanel.add(targetConsole, BorderLayout.CENTER);

		turnCount = new JPanel();
		turnCount.setBackground(new Color(50,50,50));
		
		JLabel turnLabel = new JLabel("Turn: "+turn,JLabel.CENTER);
		turnLabel.setFont(new Font("DialogInput", Font.BOLD, 20));
		turnLabel.setForeground(Color.white);
		
		turnCount.add(turnLabel, BorderLayout.CENTER);
		
		actionPanel.add(moveButton);
		actionPanel.add(attackButton);
		actionPanel.add(cureButton);
		actionPanel.add(useSpecialButton);
		
		endTurnPanel.add(endTurnButton, BorderLayout.NORTH);
		endTurnPanel.add(turnCount, BorderLayout.CENTER);
		endTurnPanel.add(exitButton, BorderLayout.SOUTH);

		updateHeroConsole(selectedHero);
		
		bar.add(actionPanel, BorderLayout.WEST);
		bar.add(endTurnPanel, BorderLayout.EAST);
		bar.add(console, BorderLayout.CENTER);
		
		infoPanel.add(heroPanel, BorderLayout.NORTH);
		infoPanel.add(targetPanel, BorderLayout.CENTER);

		panel.add(map, BorderLayout.CENTER);
		panel.add(bar, BorderLayout.SOUTH);
		panel.add(infoPanel, BorderLayout.WEST);
		
		KeyboardFocusManager keyboardManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		keyboardManager.addKeyEventDispatcher(this);
		
		GameView.fPanel.add(panel);
		
		GameView.frame.setVisible(true);
	}
	
	public void updateMap()
	{
		map.removeAll();
		for(int i =0;i < Game.map.length;i++)
		{
			for(int j = 0;j < Game.map[i].length;j++)
			{
				JButton cellButton = new JButton();
				cellButton.setBackground(new Color(100,100,100));
				cellButton.setBorder(BorderFactory.createLineBorder(new Color(50,60,50), 1));
				cellButton.setFocusable(false);
				cellButton.setBackground(new Color(50,70,50));
				cellButton.addActionListener(this);

				Image img = null;
				ImageIcon icon = null;
				if(Game.map[i][j] instanceof CharacterCell && ((CharacterCell)Game.map[i][j]).getCharacter() != null)
					if(((CharacterCell)Game.map[i][j]).getCharacter() instanceof Fighter)
						try {
							img = ImageIO.read(getClass().getClassLoader().getResource("resources/Fighter.png"));
							icon = new ImageIcon(img.getScaledInstance(img.getWidth(cellButton), img.getHeight(cellButton)-15, Image.SCALE_SMOOTH));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else if(((CharacterCell)Game.map[i][j]).getCharacter() instanceof Explorer)
						try {
							img = ImageIO.read(getClass().getClassLoader().getResource("resources/Explorer.png"));
							icon = new ImageIcon(img.getScaledInstance(img.getWidth(cellButton), img.getHeight(cellButton)-30, Image.SCALE_SMOOTH));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else if(((CharacterCell)Game.map[i][j]).getCharacter() instanceof Medic)
						try {
							img = ImageIO.read(getClass().getClassLoader().getResource("resources/Medic.png"));
							icon = new ImageIcon(img.getScaledInstance(img.getWidth(cellButton), img.getHeight(cellButton)-15, Image.SCALE_SMOOTH));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else 
						try {
							img = ImageIO.read(getClass().getClassLoader().getResource("resources/Zombie.png"));
							icon = new ImageIcon(img.getScaledInstance(img.getWidth(cellButton)-7, img.getHeight(cellButton)-20, Image.SCALE_SMOOTH));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				else if(Game.map[i][j] instanceof CollectibleCell)
					if(((CollectibleCell)Game.map[i][j]).getCollectible() instanceof Vaccine)
						try {
							img = ImageIO.read(getClass().getClassLoader().getResource("resources/Vaccine.png"));
							icon = new ImageIcon(img.getScaledInstance(img.getWidth(cellButton)-30, img.getHeight(cellButton)-30, Image.SCALE_SMOOTH));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else 
						try {
							img = ImageIO.read(getClass().getClassLoader().getResource("resources/Supply.png"));
							icon = new ImageIcon(img.getScaledInstance(img.getWidth(cellButton)-15, img.getHeight(cellButton)-30, Image.SCALE_SMOOTH));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				if(img != null)
				{
					cellButton.setIcon(icon);
				}
				
				cell[i][j] = cellButton;
				
				cell[i][j].setVisible(Game.map[i][j].isVisible());
				cell[i][j].setEnabled(Game.map[i][j].isVisible());
				
				map.add(cell[i][j]);
			}
		}
		map.validate();
		map.repaint();
		panel.validate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		updateConsole("");
		boolean flag = false;
		if(e.getSource().equals(moveButton))
		{
			if(selectedHero != null)
			{
				selectedAction = Actions.MOVE;
				updateConsole("Move");
			}else 
				updateConsole("You Have To Select a Hero to Move");
			flag = true;
		}else if(e.getSource().equals(attackButton))
		{
			if(selectedHero != null)
			{
				selectedAction = Actions.ATTACK;
				updateConsole("Attack");
			}else 
				updateConsole("You Have To Select a Hero to Attack");
			flag = true;
		}else if(e.getSource().equals(cureButton))
		{
			if(selectedHero != null)
			{
				selectedAction = Actions.CURE;
				updateConsole("Cure");
			}else 
				updateConsole("You Have To Select a Hero to Cure");
			flag = true;
		}else if(e.getSource().equals(useSpecialButton) && selectedHero != null)
		{
			if(selectedHero != null && !(selectedHero instanceof Medic))
			{
				try {
					selectedHero.useSpecial();
					updateConsole("Special Action Used");
				} catch (NoAvailableResourcesException e1) {
					// TODO Auto-generated catch block
					updateConsole(e1.getMessage());
				} catch (InvalidTargetException e1) {
					// TODO Auto-generated catch block
					updateConsole(e1.getMessage());
				}
				selectedAction = null;
				flag = true;
			}else if(selectedHero != null && selectedHero instanceof Medic)
			{
				selectedAction = Actions.USESPECIAL;
				updateConsole("Select Hero to Heal");
			}else 
				updateConsole("You Have To Select a Hero to Use Special Actions");
			flag = true;
		}else if(e.getSource().equals(endTurnButton))
		{
			try {
				Game.endTurn();
				turn++;
				updateConsole("Turn Ended");
			} catch (NotEnoughActionsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			selectedAction = null;
			flag = true;
		}
		else if(e.getSource().equals(Actions.USESPECIAL) && selectedHero == null)
		{
			updateConsole("Select a Hero");
			flag = false;
		}else if(e.getSource().equals(exitButton))
		{
			GameView.frame.dispose();
		}
		for(int i = 0;i < Game.map.length && !flag;i++)
		{
			for(int j = 0;j < Game.map[i].length && !flag;j++)
			{
				if(e.getSource().equals(cell[i][j]))
				{
					flag = true;
					if(selectedAction == null && Game.map[i][j] instanceof CharacterCell && ((CharacterCell)Game.map[i][j]).getCharacter() != null
							&& ((CharacterCell)Game.map[i][j]).getCharacter() instanceof Hero)
					{
						selectedHero = (Hero)((CharacterCell)Game.map[i][j]).getCharacter();
						updateHeroConsole(selectedHero);
						selectedAction = null;
					}
					else if(selectedAction != null && selectedHero != null)
					{
						if(selectedAction.equals(Actions.MOVE))
						{
							if(Game.map[i][j] instanceof TrapCell)
								updateConsole(selectedHero.getName() + " Entered a Trap Cell");
							if(i == selectedHero.getLocation().x+1 && j == selectedHero.getLocation().y)
							{
								try {
									selectedHero.move(Direction.UP);
								} catch (NotEnoughActionsException e1) {
									updateConsole(e1.getMessage());
								} catch (MovementException e1) {
									updateConsole(e1.getMessage());
								}
							}else if(i == selectedHero.getLocation().x-1 && j == selectedHero.getLocation().y)
							{
								try {
									selectedHero.move(Direction.DOWN);
								} catch (NotEnoughActionsException e1) {
									updateConsole(e1.getMessage());
								} catch (MovementException e1) {
									updateConsole(e1.getMessage());
								}
							}else if(i == selectedHero.getLocation().x && j == selectedHero.getLocation().y+1)
							{
								try {
									selectedHero.move(Direction.RIGHT);
								} catch (NotEnoughActionsException e1) {
									updateConsole(e1.getMessage());
									e1.printStackTrace();
								} catch (MovementException e1) {
									updateConsole(e1.getMessage());
								}
							}else if(i == selectedHero.getLocation().x && j == selectedHero.getLocation().y-1)
							{
								try {
									selectedHero.move(Direction.LEFT);
								} catch (NotEnoughActionsException e1) {
									updateConsole(e1.getMessage());
								} catch (MovementException e1) {
									updateConsole(e1.getMessage());
								}
							}
							else 
								updateConsole("Hero Can Only Move In Adjacent Cells and Cannot Move Diagonally");
						}else
							if(selectedAction.equals(Actions.ATTACK))
						{
							if(Game.map[i][j] instanceof CharacterCell && ((CharacterCell)Game.map[i][j]).getCharacter() != null)
							{
								selectedHero.setTarget(((CharacterCell)Game.map[i][j]).getCharacter());
								try {
									selectedHero.attack();
									updateConsole("Attacked");
								} catch (InvalidTargetException e1) {
									// TODO Auto-generated catch block
									updateConsole("Heroes Can Only Attack Zombies");
									e1.printStackTrace();
								} catch (NotEnoughActionsException e1) {
									// TODO Auto-generated catch block
									updateConsole("Not Enough Actions");
									e1.printStackTrace();
								}
							}
						}else if(selectedAction.equals(Actions.CURE))
						{
							if(Game.map[i][j] instanceof CharacterCell && ((CharacterCell)Game.map[i][j]).getCharacter() != null)
							{
								selectedHero.setTarget(((CharacterCell)Game.map[i][j]).getCharacter());
								try {
									selectedHero.cure();
									updateConsole("Cured");
								} catch (NotEnoughActionsException e1) {
									// TODO Auto-generated catch block
									updateConsole("Not Enough Actions");
									e1.printStackTrace();
								} catch (NoAvailableResourcesException e1) {
									// TODO Auto-generated catch block
									updateConsole("Not Enough Vaccines");
									e1.printStackTrace();
								} catch (InvalidTargetException e1) {
									// TODO Auto-generated catch block
									updateConsole("Heroes Can Only Cure Zombies");
									e1.printStackTrace();
								}
								selectedHero.setTarget(null);
							}
						}else if(selectedAction.equals(Actions.USESPECIAL))
						{
							selectedHero.setTarget(((CharacterCell)Game.map[i][j]).getCharacter());
							try {
								selectedHero.useSpecial();
								updateConsole("Special Action Used");
							} catch (NoAvailableResourcesException e1) {
								// TODO Auto-generated catch block
								updateConsole("Not Enough Supplies");
								e1.printStackTrace();
							} catch (InvalidTargetException e1) {
								// TODO Auto-generated catch block
								updateConsole("Invalid Target");
								e1.printStackTrace();
							}
							selectedHero.setTarget(null);
						}
					}else if(selectedHero != null && Game.map[i][j] instanceof CharacterCell && ((CharacterCell)Game.map[i][j]).getCharacter() != null)
					{
						selectedHero.setTarget(((CharacterCell)Game.map[i][j]).getCharacter());
					}else if(selectedHero != null && ((Game.map[i][j] instanceof CharacterCell && ((CharacterCell)Game.map[i][j]).getCharacter() == null)
							|| Game.map[i][j] instanceof CollectibleCell || Game.map[i][j] instanceof TrapCell))
						selectedHero.setTarget(null);
					selectedAction = null;
				}
			}
		}
		updateHeroConsole(selectedHero);
		updateMap();
		this.checkOver();
	}
	
	public static void updateConsole(String str)
	{
		console.removeAll();
		String txt = "<html>"+ str +"</html>";
		JLabel label = new JLabel(txt, JLabel.CENTER);
		label.setFont(new Font("SANS", Font.BOLD, 16));
		console.add(label, BorderLayout.CENTER);
		
		turnCount.removeAll();
		JLabel turnLabel = new JLabel("Turn: "+ turn,JLabel.CENTER);
		turnLabel.setFont(new Font("DialogInput", Font.BOLD, 20));
		turnLabel.setForeground(Color.white);
		
		turnCount.add(turnLabel, BorderLayout.CENTER);
		turnCount.validate();
		
		console.validate();
		bar.validate();
	}
	
	public static void updateHeroConsole(Hero hero)
	{
		heroConsole.removeAll();
		targetConsole.removeAll();
		if(hero != null)
		{
			JLabel heroTitle = new JLabel("Selected Character:", JLabel.CENTER);
			heroTitle.setFont(new Font("DialogInput", Font.BOLD, 20));
			heroTitle.setForeground(Color.white);
			heroPanel.add(heroTitle, BorderLayout.NORTH);
			String info = "HP: " + hero.getCurrentHp() + "\nAvailable Actions: " + 
					hero.getActionsAvailable() + "\nSpecial Action in Use: " + hero.isSpecialAction() + "\nSupplies: " + hero.getSupplyInventory().size()
					+ "\nVaccines: " + hero.getVaccineInventory().size();
			String type = (hero instanceof Fighter?"Fighter":(hero instanceof Explorer?"Explorer":"Medic"));
			JLabel title = new JLabel(hero.getName() +" - ("+ type + ")", JLabel.CENTER);
			title.setFont(new Font("SANS", Font.BOLD, 20));
			title.setForeground(new Color(50,50,150));
			
			JTextArea heroInfo = new JTextArea();
			heroInfo.append(info);
			heroInfo.setFont(new Font("SANS_SERIF", Font.BOLD, 16));
			heroInfo.setEditable(false);
			heroInfo.setBackground(new Color(50,50,50));
			heroInfo.setForeground(new Color(200,200,200));
			
			heroConsole.add(title, BorderLayout.NORTH);
			heroConsole.add(heroInfo, BorderLayout.CENTER);
			heroPanel.setVisible(true);
			heroConsole.setVisible(true);
			
			if(hero.getTarget() != null && hero.getTarget().getCurrentHp() > 0)
			{
				JLabel tgtPTitle = new JLabel("Targeted Character:", JLabel.CENTER);
				tgtPTitle.setFont(new Font("DialogInput", Font.BOLD, 20));
				tgtPTitle.setForeground(Color.white);
				targetPanel.add(tgtPTitle, BorderLayout.NORTH);
				String tgtHp = (hero.getTarget().getCurrentHp() > 0? "HP: " + hero.getTarget().getCurrentHp(): "DEAD");
				
				JLabel tgtTitle = new JLabel(hero.getTarget().getName(), JLabel.CENTER);
				tgtTitle.setFont(new Font("SANS", Font.BOLD, 20));
				Color titleColor = (hero.getTarget() instanceof Hero? new Color(50,50,150): new Color(150,50,50));
				tgtTitle.setForeground(titleColor);
				
				JTextArea tgtInfo = new JTextArea();
				tgtInfo.append(tgtHp);
				tgtInfo.setFont(new Font("DialogInput", Font.BOLD, 16));
				tgtInfo.setEditable(false);
				tgtInfo.setBackground(new Color(50,50,50));
				tgtInfo.setForeground(new Color(200,200,200));
				
				targetConsole.add(tgtTitle, BorderLayout.NORTH);
				targetConsole.add(tgtInfo, BorderLayout.CENTER);
				targetConsole.setVisible(true);
				targetPanel.setVisible(true);
			}else {
				targetPanel.setVisible(false);
			}
		}else {
			heroPanel.setVisible(false);
		}
		heroConsole.validate();
		targetConsole.validate();
		heroPanel.validate();
		targetPanel.validate();
		infoPanel.validate();
	}
	
	public static JButton createButton(String str)
	{
		JButton button = GameStart.createButton(str);
		button.setBackground(new Color(50,50,100));
		button.removeAll();
		
		JLabel label = new JLabel(str, JLabel.CENTER);
		label.setFont(new Font("SANS_SERIF", Font.BOLD, 30));
		label.setForeground(new Color(200,200,200));
		
		button.add(label);
		
		return button;
	}
	
	public void checkOver()
	{
		boolean flag = false;
		if(Game.checkWin() && !flag)
		{
			JOptionPane.showMessageDialog(GameView.frame, "Heros Won");
			flag = true;
		}else if(Game.checkGameOver() && !flag)
		{
			JOptionPane.showMessageDialog(GameView.frame, "Game Over");
			flag = true;
		}
		if(flag)
		{
			GameView.frame.dispose();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		// TODO Auto-generated method stub
		int code = e.getKeyCode();
		if(e.getID()==402) {
			return false;
		}
		if(code == KeyEvent.VK_ESCAPE)
		{
			GameView.frame.dispose();
			return true;
		}else if(code == KeyEvent.VK_T)
		{
			try {
				Game.endTurn();
				turn++;
				updateConsole("Turn Ended");
			} catch (NotEnoughActionsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			selectedAction = null;
		}
		if(selectedHero == null)
			updateConsole("No Hero Selected");
		else 
		{
			switch(code)
			{
				case KeyEvent.VK_W: 
					try {
						selectedHero.move(Direction.DOWN);
					} catch (NotEnoughActionsException e1) {
						updateConsole(e1.getMessage());
					} catch (MovementException e1) {
						updateConsole(e1.getMessage());
					}
				break;
				case KeyEvent.VK_A: 
					try {
						selectedHero.move(Direction.LEFT);
					} catch (NotEnoughActionsException e1) {
						updateConsole(e1.getMessage());
					} catch (MovementException e1) {
						updateConsole(e1.getMessage());
					}
				break;
				case KeyEvent.VK_S: 
					try {
						selectedHero.move(Direction.UP);
					} catch (NotEnoughActionsException e1) {
						updateConsole(e1.getMessage());
					} catch (MovementException e1) {
						updateConsole(e1.getMessage());
					}
				break;
				case KeyEvent.VK_D: 
					try {
						selectedHero.move(Direction.RIGHT);
					} catch (NotEnoughActionsException e1) {
						updateConsole(e1.getMessage());
					} catch (MovementException e1) {
						updateConsole(e1.getMessage());
					}
				break;
				case KeyEvent.VK_Z:
					selectedAction = Actions.ATTACK;
					updateConsole("Attack");
					break;
				case KeyEvent.VK_X:
					selectedAction = Actions.CURE;
					updateConsole("Cure");
					break;
				case KeyEvent.VK_C:
					if(selectedHero != null && !(selectedHero instanceof Medic))
					{
						try {
							selectedHero.useSpecial();
							updateConsole("Special Action Used");
						} catch (NoAvailableResourcesException e1) {
							// TODO Auto-generated catch block
							updateConsole(e1.getMessage());
						} catch (InvalidTargetException e1) {
							// TODO Auto-generated catch block
							updateConsole(e1.getMessage());
						}
						selectedAction = null;
					}else if(selectedHero != null && selectedHero instanceof Medic)
					{
						selectedAction = Actions.USESPECIAL;
						updateConsole("Select Hero to Heal");
					}
					break;
			}
		}
		updateHeroConsole(selectedHero);
		updateMap();
		this.checkOver();
		return true;
	}
}

